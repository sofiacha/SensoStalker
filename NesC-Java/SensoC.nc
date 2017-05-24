#include <Timer.h>
#include <stdio.h>
#include <string.h>

module SensoC{ 
	uses
	{
		//General interfaces
		interface Boot;
		interface Timer<TMilli>;
		interface Leds;
		
		//Read
		interface Read<uint16_t> as TempRead;
		interface Read<uint16_t> as LightRead;
		interface Read<uint16_t> as HumRead;
	}
}
implementation{
 
 	uint16_t temp;
 	uint16_t lumos;
 	uint16_t hum1, hum;
 //	FILE *fp;
 //	fp = fopen("/tmp/text.txt","w+");
 	
	event void Boot.booted(){
		call Timer.startPeriodic(1000); //1sec = 1000milliseconds
		call Leds.led0On();
		call Leds.led1On();
		call Leds.led2On();
	}

	event void Timer.fired(){
		if (call TempRead.read() == SUCCESS)
		{
			call Leds.led0Toggle();
		}
		else
		{
			call Leds.led0On();
		}
		
		
			if (call LightRead.read() == SUCCESS)
		{
			call Leds.led1Toggle();
		}
		else
		{
			call Leds.led1On();
		}
		
					
			if (call HumRead.read() == SUCCESS)
		{
			call Leds.led2Toggle();
		}
		else
		{
			call Leds.led2On();
		}
	}

	event void TempRead.readDone(error_t result, uint16_t val){
		if(result == SUCCESS)
		{
			temp = (-39.60 + 0.01 *val);
			printf("Current temp is: %d \r \n", val);
			// fprintf(fp, "This is testing for fprintf...\n");
		}
		else
		{
			printf("Something went terribly wrong!");	
		}
	}

	event void LightRead.readDone(error_t result, uint16_t val){
			if(result == SUCCESS)
		{
			lumos = 2.5 * (val / 4096.0) * 6250.0;
			printf("Current light is: %d \r \n", lumos);
		}
		else
		{
			printf("Something went terribly wrong!");	
		}
	}

	event void HumRead.readDone(error_t result, uint16_t val){
				if(result == SUCCESS)
		{
			 hum1 = -4 + 0.0405*val + (-2.8 * pow(10,-6) * pow(val,2) );
			 hum = (temp - 25) * (0.01 + 0.00008*val) + hum1;
			 //hum = 2.5 * val;
			printf("Current humidity is: %d \r \n", val);
		}
		else
		{
			printf("Something went terribly wrong!");	
		}
	}
}
