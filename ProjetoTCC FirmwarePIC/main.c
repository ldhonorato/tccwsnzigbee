//FIRMWARE TCC WSN ZIGBEE - LEANDRO HONORATO
/*
*
*
*/

#include <18F4550.h>  // inclui arquivo de bibliotecas do dispositivo
#device adc = 10      // essa diretiva precisa vir imediatamente abaixo do include do arquivo do processador
#fuses HS, NOWDT, PUT, NOLVP, DEBUG, NOPROTECT, NOBROWNOUT
#use delay(clock=20000000)
#use rs232(baud=9600, xmit=PIN_C6, rcv=PIN_C7, PARITY = N, ERRORS, bits = 8, stop = 1) //Configurações da Serial

#include "Rotinas_LCD.c"
#include "ADC_Read.c"
#include "BibliotecaZigBee.c"
#include <string.h>
#include <stdlib.h>

#define TAXA_ATUALIZACAO_DESTINO 100

//////////////////////////////////////////////////////////////////
//VARIÁVEIS GLOBAIS//
//////////////////////////////////////////////////////////////////
int8 enderecoH[10];
int8 enderecoL[10];
int8 stringOK[3];
int8 stringDestinationNodeName[5];
int8 keyPassword[10];
int8 quantidadePacotesEnviados;
Pacote pacoteStruct;
int32 valorLeituraAD;
int8 valorStringLeituraAD[10];
//////////////////////////////////////////////////////////////////
//FIM DAS VARIÁVEIS GLOBAIS//
//////////////////////////////////////////////////////////////////
//----------------------------------------------------------------------
//////////////////////////////////////////////////////////////////
//CÓDIGO DE INTERRUPÇÃO SERIAL//
//////////////////////////////////////////////////////////////////
#int_rda
void reception ()
{
   interrupcao_serial(); //Na interrupção da serial basta chamar a função da Biblioteca ZigBee
}
//////////////////////////////////////////////////////////////////
//FIM DO CÓDIGO DE INTERRUPÇÃO SERIAL//
//////////////////////////////////////////////////////////////////
//----------------------------------------------------------------------
//////////////////////////////////////////////////////////////////
//FUNÇÕES UTILIZADAS PELO MAIN//
//////////////////////////////////////////////////////////////////
void modoComando()
{
   do
   {
      clearLCD();
      lcd_gotoxy(1,1);
      printf(lcd_putc,"Modo comando...");
      delay_ms(1000);

      irParaModoComando();
      esperaPacote();

      clearLCD();
      lcd_gotoxy(1,1);
      printf(lcd_putc,"Resp Recebida");
      lcd_gotoxy(1,2);
      printf(lcd_putc,"%s", pacoteRecebido);
      delay_ms(1000);
   }while ( strcmp( pacoteRecebido, stringOK) != 0 );
}

void modoTransparente()
{
   clearLCD();
   lcd_gotoxy(1,1);
   printf(lcd_putc,"Modo transparente...");
   delay_ms(1000);

   irParaModoTransparente();
   esperaPacote();

   clearLCD();
   lcd_gotoxy(1,1);
   printf(lcd_putc,"Resp Recebida");
   lcd_gotoxy(1,2);
   printf(lcd_putc,"%s", pacoteRecebido);
   delay_ms(1000);
   if ( strcmp( pacoteRecebido, stringOK) != 0 )
   {
      delay_ms(2000); //se nao recebeu resposta vai para modo transparente por delay
   }
}
void setarDesstinoConcentrador()
{
  modoComando();

  do
  {
     clearLCD();
     lcd_gotoxy(1,1);
     printf(lcd_putc,"Setando Destino...");
     delay_ms(1000);

     zigbee_destinationNode(stringDestinationNodeName);
     esperaPacote();

     clearLCD();
     lcd_gotoxy(1,1);
     printf(lcd_putc,"Destino Setado...");
     lcd_gotoxy(1,2);
     printf(lcd_putc,"%s", pacoteRecebido);
     delay_ms(1000);

  }while ( strcmp( pacoteRecebido, stringOK) != 0 );

}

void enviarLeitura()
{
   int8 canal;
   canal = atoi(pacoteStruct.informacao);
   canal = canal-1;
   ler_canal_AD_para_puint8( canal, valorStringLeituraAD );
   clearLCD();
   lcd_gotoxy(1,1);
   printf(lcd_putc,"VOU ENVIAR %d...", canal);
   delay_ms(1000);

   enviar_pacote_Leitura(enderecoH, enderecoL, canal, valorStringLeituraAD);

   clearLCD();
   lcd_gotoxy(1,1);
   printf(lcd_putc,"ENVIEI...");
   delay_ms(1000);

   quantidadePacotesEnviados = (quantidadePacotesEnviados+1);
   if(quantidadePacotesEnviados == TAXA_ATUALIZACAO_DESTINO)
   {
      quantidadePacotesEnviados = 0;
      setarDesstinoConcentrador();
   }
}
void setarNomeDevice()
{
   modoComando();
   do
   {
     clearLCD();
     lcd_gotoxy(1,1);
     printf(lcd_putc,"Setando Nome...");
     delay_ms(1000);

     zigbee_NodeIdentifier(pacoteStruct.informacao);
     esperaPacote();

     clearLCD();
     lcd_gotoxy(1,1);
     printf(lcd_putc,"Nome Setado...");
     lcd_gotoxy(1,2);
     printf(lcd_putc,"%s", pacoteRecebido);
     delay_ms(1000);

  }while ( strcmp( pacoteRecebido, stringOK) != 0 );

  do
     {
        clearLCD();
        lcd_gotoxy(1,1);
        printf(lcd_putc,"Gravando dados...");
        delay_ms(1000);

        zigbee_Write();
        esperaPacote();

        clearLCD();
        lcd_gotoxy(1,1);
        printf(lcd_putc,"Resp Recebida");
        lcd_gotoxy(1,2);
        printf(lcd_putc,"%s", pacoteRecebido);
        delay_ms(1000);
     }while ( strcmp( pacoteRecebido, stringOK) != 0 );

  delay_ms(2000); // para ir para modo transparente
}
void enviarComandoRemoto()
{
   char respostaComando[15];
   modoComando();
   printf("%s",pacoteStruct.informacao);
   putc(0x0d);//envia o '/r'
   esperaPacote();
   strcpy(respostaComando, pacoteRecebido);
   modoTransparente();
   printf("#RESPRC*%s",respostaComando);
}

void imprimirLeiturasLCD()
{
   clearLCD();
   lcd_gotoxy(1,1);
   printf(lcd_putc,"LEITURAs");
}

//////////////////////////////////////////////////////////////////
//FIM DAS FUNÇÕES UTILIZADAS PELO MAIN//
//////////////////////////////////////////////////////////////////
//----------------------------------------------------------------------
//////////////////////////////////////////////////////////////////
//MAIN//
//////////////////////////////////////////////////////////////////
void main(void){ //while(1) ;}
  //--------------INICIALIZAÇÃO-------------------//
  quantidadePacotesEnviados = 0;

  enable_interrupts(GLOBAL); //Habilita interrupção da serial
  enable_interrupts(int_rda);

  inicializar_variaveis(); //Inicializa variáveis de controle da recepção serial
  limpar_buffer(); //Limpa o Buffer de recepção serial

  lcd_init();
  clearLCD();
  lcd_gotoxy(1,1);
  printf(lcd_putc,"Inicializando*.*");
  delay_ms(5000); //espera a inicialização do modulo
  lcd_gotoxy(1,1);

  sprintf(stringOK, "OK");
  sprintf(stringDestinationNodeName, "SINK");
  sprintf(keyPassword, "senha");
  //--------------FIM DA INICIALIZAÇÃO-------------------//

  modoComando();

  //limpar_buffer();
  zigbee_SerialNumberHigh();
  delay_ms(500);
  esperaPacote();
  strcopy( enderecoH, pacoteRecebido );

  //limpar_buffer();
  zigbee_SerialNumberLow();
  delay_ms(500);
  esperaPacote();
  strcopy( enderecoL, pacoteRecebido );

  clearLCD();
  lcd_gotoxy(1,1);
  printf(lcd_putc,"%s", enderecoH);
  lcd_gotoxy(1,2);
  printf(lcd_putc,"%s", enderecoL);
  delay_ms(1000);

  modoTransparente( );

  setarDesstinoConcentrador();

  init_ADC();

  clearLCD();
  lcd_gotoxy(1,1);
  printf(lcd_putc,"LEITURAs");
  delay_ms(1000);

  while(1)
  {
      clearLine2();
      ler_canal_AD_para_puint8 (0, valorStringLeituraAD);
      lcd_gotoxy(1,2);
      printf(lcd_putc,"%s -", valorStringLeituraAD);
      ler_canal_AD_para_int32 (1, &valorLeituraAD);
      printf(lcd_putc,"- %Lu", valorLeituraAD);

      if (chegouPacote() == 1)
      {
         tratarPacote(&pacoteStruct);
         switch (pacoteStruct.tipoPacote)
         {
            case READR:
               enviarLeitura();
               imprimirLeiturasLCD();
               break;
            case SET_NAME:
               setarNomeDevice();
               imprimirLeiturasLCD();
               break;
            case COMANDO_REMOTO:
               enviarComandoRemoto();
               imprimirLeiturasLCD();
               break;
            default:
               //faz nada
         }
      }
      if(valorLeituraAD < 500)
      {
         enviarLeitura();
         imprimirLeiturasLCD();
      }

      delay_ms(1000);

  }

}
//////////////////////////////////////////////////////////////////
//FIM DO MAIN//
//////////////////////////////////////////////////////////////////
