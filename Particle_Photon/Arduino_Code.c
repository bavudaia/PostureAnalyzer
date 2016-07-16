#include "MQTT/MQTT.h"
#include "math.h"
void callback(char* topic, byte* payload, unsigned int length);

/**
 * if want to use IP address,
 * byte server[] = { XXX,XXX,XXX,XXX };
 * MQTT client(server, 1883, callback);
 * want to use domain name,
 * MQTT client("www.sample.com", 1883, callback);
 **/
 //54.187.143.223
int FSR_Pin = A0;
int FSR_Pin1 = A1;
int FSR_Pin2 = A2;
int FSR_Pin3 = A3;
int distancePin = A4;
int temp_pin = A5;
byte ip[] = {54,218,1,206}; 
MQTT client(ip, 2880, callback);
//54.218.1.206

// recieve message
void callback(char* topic, byte* payload, unsigned int length) {
    //char p[length + 1];
    //memcpy(p, payload, length);
//    p[length] = NULL;
   /* String message(p);

    if (message.equals("RED"))    
        RGB.color(255, 0, 0);
    else if (message.equals("GREEN"))    
        RGB.color(0, 255, 0);
    else if (message.equals("BLUE"))    
        RGB.color(0, 0, 255);
    else    
        RGB.color(255, 255, 255);
    delay(1000);*/
}


void setup() {
  //  RGB.control(true);
    
    // connect to the server
    client.connect("spark sample");
    Particle.publish("Test4");
    // publish/subscribe
    //pinMode(temp_pin, OUTPUT);
    if (client.isConnected()) {#include "MQTT/MQTT.h"
#include "math.h"
void callback(char* topic, byte* payload, unsigned int length);

/**
 * if want to use IP address,
 * byte server[] = { XXX,XXX,XXX,XXX };
 * MQTT client(server, 1883, callback);
 * want to use domain name,
 * MQTT client("www.sample.com", 1883, callback);
 **/
 //54.187.143.223
int FSR_Pin = A0;
int FSR_Pin1 = A1;
int FSR_Pin2 = A2;
int FSR_Pin3 = A3;
int distancePin = A4;
int temp_pin = A5;
byte ip[] = {54,218,1,206}; 
MQTT client(ip, 2880, callback);
//54.218.1.206

// recieve message
void callback(char* topic, byte* payload, unsigned int length) {
    //char p[length + 1];
    //memcpy(p, payload, length);
//    p[length] = NULL;
   /* String message(p);

    if (message.equals("RED"))    
        RGB.color(255, 0, 0);
    else if (message.equals("GREEN"))    
        RGB.color(0, 255, 0);
    else if (message.equals("BLUE"))    
        RGB.color(0, 0, 255);
    else    
        RGB.color(255, 255, 255);
    delay(1000);*/
}


void setup() {
  //  RGB.control(true);
    
    // connect to the server
    client.connect("spark sample");
    Particle.publish("Test4");
    // publish/subscribe
    //pinMode(temp_pin, OUTPUT);
    if (client.isConnected()) {
        Particle.publish("Test1");
        client.publish("/econ/temp","102Celcius");
        //client.subscribe("/econ/test");
    }
}
int randomnum(int maxRand)
{
    return rand() % maxRand + 1;
}
int Thermistor(int RawADC) {
  double Temp;
  Temp = log(10000.0*((1024.0/RawADC-1))); 
  Temp = 1 / (0.001129148 + (0.000234125 + (0.0000000876741 * Temp * Temp ))* Temp );
  Temp = Temp - 273.15;            // Convert Kelvin to Celcius
   //Temp = (Temp * 9.0)/ 5.0 + 32.0; // Convert Celcius to Fahrenheit
   return (int)Temp;
}
void loop() {
  //  Particle.publish("Test3");
    int FSRReading = analogRead(FSR_Pin);
    int FSRReading1 = analogRead(FSR_Pin1);
    int FSRReading2 = analogRead(FSR_Pin2);
    int FSRReading3 = analogRead(FSR_Pin3);
    int Temp = analogRead(temp_pin);
    int Distance = analogRead(distancePin);
    Temp = Thermistor(Temp);
    char FSR[255];
    int pressure1 = (FSRReading > 200)? 1 : 0;
    int pressure2 = (FSRReading1 > 3000)? 1 : 0;
    int pressure3 = (FSRReading2 > 250)? 1 : 0;
    int pressure4 = (FSRReading3 > 70)? 1 : 0;
    sprintf(FSR , "{\"pressure1\":%d,\"pressure2\":%d,\"pressure3\":%d,\"pressure4\":%d,\"temperature\":%d,\"Distance\":%d}",pressure1,pressure2,pressure3, pressure4,Temp,Distance);
    if (client.isConnected())
    {
        client.publish("/smartchair/data/spark",FSR);
        client.loop();
    }
    delay(2000);
}

        Particle.publish("Test1");
        client.publish("/econ/temp","102Celcius");
        //client.subscribe("/econ/test");
    }
}
int randomnum(int maxRand)
{
    return rand() % maxRand + 1;
}
int Thermistor(int RawADC) {
  double Temp;
  Temp = log(10000.0*((1024.0/RawADC-1))); 
  Temp = 1 / (0.001129148 + (0.000234125 + (0.0000000876741 * Temp * Temp ))* Temp );
  Temp = Temp - 273.15;            // Convert Kelvin to Celcius
   //Temp = (Temp * 9.0)/ 5.0 + 32.0; // Convert Celcius to Fahrenheit
   return (int)Temp;
}
void loop() {
  //  Particle.publish("Test3");
    int FSRReading = analogRead(FSR_Pin);
    int FSRReading1 = analogRead(FSR_Pin1);
    int FSRReading2 = analogRead(FSR_Pin2);
    int FSRReading3 = analogRead(FSR_Pin3);
    int Temp = analogRead(temp_pin);
    int Distance = analogRead(distancePin);
    Temp = Thermistor(Temp);
    char FSR[255];
    int pressure1 = (FSRReading > 200)? 1 : 0;
    int pressure2 = (FSRReading1 > 3000)? 1 : 0;
    int pressure3 = (FSRReading2 > 250)? 1 : 0;
    int pressure4 = (FSRReading3 > 70)? 1 : 0;
    sprintf(FSR , "{\"pressure1\":%d,\"pressure2\":%d,\"pressure3\":%d,\"pressure4\":%d,\"temperature\":%d,\"Distance\":%d}",pressure1,pressure2,pressure3, pressure4,Temp,Distance);
    if (client.isConnected())
    {
        client.publish("/smartchair/data/spark",FSR);
        client.loop();
    }
    delay(2000);
}
