/*
 * Copyright (c) 2006 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */

/**
 * Oscilloscope demo application. Uses the demo sensor - change the
 * new DemoSensorC() instantiation if you want something else.
 *
 * See README.txt file in this directory for usage instructions.
 *
 * @author David Gay
 */
configuration OscilloscopeAppC { }
implementation
{
  components OscilloscopeC, MainC, ActiveMessageC, LedsC,
    new TimerMilliC(), 
    new AMSenderC(AM_OSCILLOSCOPE), new AMReceiverC(AM_OSCILLOSCOPE);

  OscilloscopeC.Boot -> MainC;
  OscilloscopeC.RadioControl -> ActiveMessageC;
  OscilloscopeC.AMSend -> AMSenderC;
  OscilloscopeC.Receive -> AMReceiverC;
  OscilloscopeC.Timer -> TimerMilliC;
 // OscilloscopeC.Read -> Sensor;
  OscilloscopeC.Leds -> LedsC;
	//components SerialPrintfC;
	
	//Temperature Component
	components new SensirionSht11C() as Sensor;
	
	
	OscilloscopeC.TempRead -> Sensor.Temperature;
	
	//Light Component
	components new HamamatsuS10871TsrC() as LightSensor; 
	//components new HamamatsuS1087ParC() as PhotoPar;
	
	OscilloscopeC.LightRead -> LightSensor;
//	OscilloscopeC.Phot -> PhotoPar;
	//Humidity Component
	//components new SensirionSht11C() as HumSensor;
	
	OscilloscopeC.HumRead -> Sensor.Humidity;
  
}
