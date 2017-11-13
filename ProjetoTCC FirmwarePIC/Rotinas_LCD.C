//Rotinas LCD

#define enable    PIN_B3
#define rs        PIN_B5
#define rw        PIN_B4

void lcd_putc( char c);
int inverte_bits(int numero);
void lcd_gotoxy( BYTE x, BYTE y);

#define lcd_line_two 0x40

void lcd_send_nibble( BYTE n ) {


      n = inverte_bits(n);
      output_d(n);

      delay_ms(1);

      output_high(enable);
      delay_cycles(1);


      output_low(enable);
      delay_cycles(1);
}


void lcd_send_byte( BYTE address, BYTE n ) {


      output_low(rs);

      if ( address == 1 )
      {
         output_high(rs);
      }
      delay_cycles(1);

      output_low(enable);
      delay_cycles(1);

      output_low(enable);
      lcd_send_nibble(n >> 4);
      lcd_send_nibble(n & 0x0F);
}


void lcd_init() {

  output_low(rs);
  output_low(rw);
  output_d(0x00);
  delay_ms(200);


   output_d(0x00);
   output_d(0x00);

   output_high(enable);

   delay_ms(10);
   output_low(enable);
   delay_ms(10);
   output_d(0b01000000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0b01000000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0b00010000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0x00);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0b11110000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0x00);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0b10000000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0x00);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   output_d(0b01100000);
   output_high(enable);
   delay_ms(10);
   output_low(enable);
   delay_ms(10);

   lcd_gotoxy(1,1);

}


void lcd_gotoxy( BYTE x, BYTE y) {
   BYTE address;

   if(y!=1)
     address=lcd_line_two;
   else
     address=0;
   address+=x-1;
   lcd_send_byte(0,0x80|address);
}

void lcd_putc( char c) {
   switch (c) {
     case '\f'   : lcd_send_byte(0,1);
                   delay_ms(2);
                                           break;
     case '\n'   : lcd_gotoxy(1,2);        break;
     case '\b'   : lcd_send_byte(0,0x10);  break;
     default     : lcd_send_byte(1,c);     break;
   }
}



int inverte_bits(int numero){
   int byte_invertido;
   int count;
   int temp = 8;
   for(count = 0; count <8; count++){
   temp--;
   if(bit_test(numero,count))
   {
      bit_set(byte_invertido,temp);
   }else
      bit_clear(byte_invertido,temp);
   }

   return byte_invertido;
}

void clearLine1()
{
   lcd_gotoxy(1,1);
   printf(lcd_putc,"                ");
}

void clearLine2()
{
   lcd_gotoxy(1,2);
   printf(lcd_putc,"                ");
}

void clearLCD()
{
   clearLine1();
   clearLine2();
}


