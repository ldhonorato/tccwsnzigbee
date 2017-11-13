/*
*  Biblioteca de Funções para o Módulo ZigBee XBee
*/
#include <string.h>
#include <stdlib.h>

#define MAX_BUFFER 50 //Tamanho do Buffer de recepção Serial
#define MAX_PACOTE 30 //Tamanho do Array onde as respostas são armazenadas

int8 buffer_resposta_zigbee [MAX_BUFFER]; //Buffer que recebe os dados e comandos pela serial
int pacoteRecebido [MAX_PACOTE]; //array para armazenar os comandos recebidos na medida que forem retirados do buffer

int contador_escrita; //Variável para saber onde escrever no Buffer de recepção Serial (resposta_zigbee)
int contador_leitura; //Variável para saber onde ler no Buffer de recepção Serial (resposta_zigbee)

char comandoREADR[7];
char comandoSET_NAME[7];
char comandoREMOTO[7];

//enum com os possíveis pacotes
typedef enum _TipoPacote
{
   READR,
   SET_NAME,
   COMANDO_REMOTO,
   PACOTE_NAO_IDENTIFICADO
}TipoPacote;

//Estrutura que define um pacote
typedef struct _pacote
{
   TipoPacote tipoPacote;
   char informacao[15];
} Pacote;

/**
 * Esta função deve ser chamada sempre que chegar dados pela serial
 * Armazena os dados que estão chegando no array resposta_zigbee
 */
void interrupcao_serial()
{
   int8 temp;
   while( kbhit() )
   {
         temp = getc();
         buffer_resposta_zigbee[contador_escrita] = temp;
         contador_escrita = (contador_escrita+1)%MAX_BUFFER; //Incremento circular: quando chegar no final do array, volta a escrever no início
   }
}

/**
 * Esta função reseta o buffer pacoteRecebido
 */
void zerarBufferPacoteRecebido()
{
   int contador;
   for(contador = 0; contador < MAX_PACOTE; contador++)
   {
      pacoteRecebido[contador] = 0;
   }
}

/**
 * Esta função verifica se chegou pacote
 * se chegou pacote o retorno é 1 caso contrário, o retorno é 0
 */
int1 chegouPacote()
{
   int8 i;
   int8 temp_pacote[MAX_PACOTE];
   int8 contadorPacote = 0;


   for(i = contador_leitura; buffer_resposta_zigbee[i] != 0 ; i=(i+1)%MAX_BUFFER)
   {
      if( buffer_resposta_zigbee[i] == '\r' )
      {
         buffer_resposta_zigbee[i] = 0; //Finaliza a string
         temp_pacote[contadorPacote] = 0;

         contador_leitura = (i + 1)%MAX_BUFFER;

         zerarBufferPacoteRecebido();

         strcopy(pacoteRecebido, temp_pacote);


         return 1;
      }
      temp_pacote[contadorPacote] = buffer_resposta_zigbee[i];
      contadorPacote++;
   }
   return 0;
}

/**
 * Função para inicializar as variáveis de controle globais.
 *
 */
void inicializar_variaveis(){
   contador_leitura = 0;
   contador_escrita = 0;

   sprintf(comandoREADR, "#READR");
   sprintf(comandoSET_NAME, "#SETNA");
   sprintf(comandoREMOTO, "#CREMO");
}

/**
 * Função para limpar o buffer de recepção serial (resposta_zigbee)
 *
 */
void limpar_buffer(){
   int contador;
   for(contador = 0; contador < MAX_BUFFER; contador++)
   {
      buffer_resposta_zigbee[contador] = 0;
   }
   contador_leitura = 0;
   contador_escrita = 0;
}

/**
 * Função Restore Defaults
 * Restaura os parâmetros do módulo para as cofigurações de fábrica. Não reseta o parâmetro ID.
 *
 */
void zigbee_restoreDefault()
{
   printf("AT RE"); //Envia o comando de reset
   putc(0x0d); //CR
}

/**
 * Função Software Reset
 * Reseta o módulo. O módulo responde com um "OK" e realiza o reset por volta de 2 segundos depois. A camada de rede também pode ser resetada em alguns casos.
 *
 */
void zigbee_softwareReset()
{
   printf("AT FR");
   putc(0x0d);
}

/**
 * Função Network Reset
 * Reseta a camada de rede de um ou mais nós dentro de uma PAN (Personal Area Network). O módulo responde imediantamente com um "OK" ou "ERROR".
 * Com o reset da camada de rede todos as configurações de rede e informações de roteamento são perdidas.
 * Se parametro = "0" - O reset na camada de rede acontece apenas no módulo que está executando a função
 *    parametro = "1" - Envia uma transmissão broadcast para resetar os parametros da camada de rede em todos os nós da PAN.
 */
void zigbee_NetworkReset(char *parametro)
{
   printf("AT NR %s", parametro);
   putc(0x0d);
}

/**
 * Função Write
 * Escreve os parametros em uma memória não volátil (depois de um reset os parametros continuam os memsmos).
 * Este comando responde com um "OK\r"
 *
 */
void zigbee_Write()
{
   printf("AT WR");
   putc(0x0d);
}

/**
 * Função para ler os 32 bits mais altos do endereço de destino
 * Este comando responde com os 32 bits mais altos do endereço de destino
 *
 */
void zigbee_getDestinationAddressHigh()
{
   printf("AT DH");
   putc(0x0d);
}

/**
 * Função para setar os 32 bits mais altos do endereço de destino
 * Este comando responde com "OK" ou "ERROR".
 *
 */
void zigbee_setDestinationAddressHigh(char* enderecoAlto)
{
   printf("AT DH %s", enderecoAlto);
   putc(0x0d);
}

/**
 * Função para ler os 32 bits mais baixos do endereço de destino
 * Este comando responde com os 32 bits mais baixos do endereço de destino
 *
 */
void zigbee_getDestinationAddressLow()
{
   printf("AT DL");
   putc(0x0d);
}

/**
 * Função para setar os 32 bits mais baixos do endereço de destino
 * Este comando responde com "OK" ou "ERROR".
 *
 */
void zigbee_setDestinationAddressLow(char* enderecoBaixo)
{
   printf("AT DL %s", enderecoBaixo);
   putc(0x0d);
}

/**
 * Função ler o endereço de rede de 16-bits
 * Este comando responde com o endereço do módulo
 */
void zigbee_MY()
{
   printf("AT MY");
   putc(0x0d);
}

/**
 * Função que retorna o endereço de rede de 16-bits do pai do nó.
 * Este comando responde com o endereço do módulo pai
 */
void zigbee_MP()
{
   printf("AT MP");
   putc(0x0d);
}

/**
 * Função que retorna a parte alta (32bits) do identificador único do módulo
 *
 */
void zigbee_SerialNumberHigh(){
   printf("AT SH");
   putc(0x0d);
}

/**
 * Função que retorna a parte baixa (32bits) do identificador único do módulo
 *
 */
void zigbee_SerialNumberLow(){
   printf("AT SL");
   putc(0x0d);
}

/**
 * Função que retorna a parte baixa (32bits) do identificador único do módulo
 *
 */
void zigbee_EnableEncryption(){
   printf("AT EE 1");
   putc(0x0d);
}

/**
 * Função que retorna a parte baixa (32bits) do identificador único do módulo
 *
 */
void zigbee_setKY(int8 *key){
   printf("AT KY %s", key);
   putc(0x0d);
}

/**
 * Função para armazenar uma string identificadora do nó. O nome apenas pode conter caracteres ASCII imprimíveis.
 * O tamanho máximo do nome é de 20Bytes.
 *
 */
void zigbee_NodeIdentifier(char *nome){
   printf("AT NI %s", nome);
   putc(0x0d); //CR
}

/**
 * Função para ler o canal que o módulo está operando. Se o módulo não estiver associado em nenhuma rede o retorno desta função é "0" (zero).
 *
 */
void zigbee_operatingChanel(){
   printf("AT CH");
   putc(0x0d); //CR
}

/**
 * Função para setar o endereço PAN. Quando o módulo iniciar ele vai procurar uma PAN com o endereço setado para se associar.
 * Se pan = 0xFFFF -> o módulo entra em qualquer PAN.
 * Mudanças nesse parâmetro devem ser salvas através da função Write.
 *
 */
void zigbee_setPANID(char *pan){
   printf("AT ID %s", pan);
   putc(0x0d); //CR
}

/**
 * Função para ler o endereço PAN. Quando o módulo iniciar ele vai procurar uma PAN com o endereço setado para se associar.
 * Se pan = 0xFFFF -> o módulo entra em qualquer PAN.
 * Mudanças nesse parâmetro devem ser salvas através da função Write.
 *
 */
void zigbee_getPANID(){
   printf("AT ID");
   putc(0x0d); //CR
}

/**
 * Função para setar o Node Discovery Timeout.
 * Seta a quantidade de tempo que o nó vai gastar descobrindo outros nós quando o comando DN (ver mais a frente).
 * time = de 0x20 - 0xFF [x 100msec]
 */
void zigbee_setNT(char *time){
   printf("AT NT %s", time);
   putc(0x0d); //CR
}

/**
 * Função para ler o Node Discovery Timeout.
 * Seta a quantidade de tempo que o nó vai gastar descobrindo outros nós quando o comando DN (ver mais a frente).
 */
void zigbee_getNT(){
   printf("AT NT");
   putc(0x0d); //CR
}

/**
 * Função Destination Node - Resolve um nome em um endereço físico.
 * No modo de comandos AT, quando o comando é realizado, os parâmetros de Destino (tanto a parte alta quanto a parte baixa - SH e SL)
 * recebem o endereço do nó cujo nome é igual ao parametro nomeDestino.
 */
void zigbee_destinationNode(char *nomeDestino){
   printf("AT DN %s", nomeDestino);
   putc(0x0d); //CR
}


/**
 * Função para enviar uma resposta de requisição de leitura
 * O módulo deve estar no modo transparente
 */
void enviar_pacote_Leitura(int8* enderecoH, int8* enderecoL, int8 canalLido, int8* valorLido){ //TESTADO
   printf( "#READV*%s*%s*%d*%s\r",enderecoH,enderecoL,(canalLido+1), valorLido );
   //putc(0x0d); //CR
}

/**
 * Função para deixar o módulo no modo de comando
 */
void irParaModoComando()
{
   delay_ms(1000);
   printf( "+++" );
   delay_ms(1000);
}

/**
 * Função para deixar o módulo no modo transparente
 */
void irParaModoTransparente()
{
   printf("ATCN\r");
   //delay_ms(100);
}

/**
 * Função para ficar esperando um pacote
 * essa função termina quando chegar um pacote ou ocorrer um timeout.
 */
void esperaPacote()
{
   int8 i = 0;
   while(chegouPacote() != 1 && i < 200)
   {
      ;
      delay_ms(100);
      i++;
   }
}

/**
 * Função para decodificar um pacote recebido
 * essa função preenche a estrutura Pacote passada por argumento
 */
void tratarPacote(Pacote *pacoteRecebidoStruct)
{
     char pacoteRecebidoAux[10];
     int i;
     int indexInformacao = 0;

     for(i = 0; i < 10 ; i++)
     {
         pacoteRecebidoAux[i] = 0;
     }


     for(i = 0; i < 10 ; i++)
     {
         if (pacoteRecebido[i] == 0)
         {
            pacoteRecebidoStruct->tipoPacote = PACOTE_NAO_IDENTIFICADO;
            return;
         }
         else if (pacoteRecebido[i] == '*')
         {
            pacoteRecebidoAux[i] = '\0';
            i++;
            break;
         }else
         {
            pacoteRecebidoAux[i] = pacoteRecebido[i];
         }
     }

     if(strcmp(pacoteRecebidoAux, comandoREADR) == 0)
     {
         pacoteRecebidoStruct->tipoPacote = READR;
     }else if(strcmp(pacoteRecebidoAux, comandoSET_NAME) == 0)
     {
         pacoteRecebidoStruct->tipoPacote = SET_NAME;
     }else if(strcmp(pacoteRecebidoAux, comandoREMOTO) == 0)
     {
         pacoteRecebidoStruct->tipoPacote = COMANDO_REMOTO;
     }else
     {
         pacoteRecebidoStruct->tipoPacote = PACOTE_NAO_IDENTIFICADO;
         return;
     }


     for(; i < MAX_PACOTE; i++)
     {
         if (pacoteRecebido[i] == '\r' || pacoteRecebido[i] == 0)
         {
            return;
         }else
         {
            pacoteRecebidoStruct->informacao[indexInformacao] =  pacoteRecebido[i];
            indexInformacao++;
         }
     }
}

