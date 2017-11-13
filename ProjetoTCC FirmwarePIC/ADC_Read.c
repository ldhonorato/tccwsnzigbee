#include <stdlib.h>

void ler_canal_AD_para_int32 (int8 canal, int32 * p_valor)
{
   set_adc_channel(canal);  //escolhe canal
   delay_ms(50);             //espera um tempo para a tens�o se estabilizar
   *p_valor = read_adc();      //inicia a convers�o espera ela ser concluida e retorna o valor convertido
}

void ler_canal_AD_para_puint8 (int8 canal, int8 * p_valorChar)
{

   int32 valorLeitura;
   set_adc_channel(canal);  //escolhe canal
   delay_ms(50);  //espera um tempo para a tens�o se estabilizar
   valorLeitura = read_adc();      //inicia a convers�o espera ela ser concluida e retorna o valor convertido
   itoa(valorLeitura,10,p_valorChar);
}

void init_ADC()
{
   setup_adc(ADC_CLOCK_DIV_32);   //configura conversor AD
   setup_adc_ports(AN0_TO_AN10);   //escolhe pinos de entradas anal�gicas
}
