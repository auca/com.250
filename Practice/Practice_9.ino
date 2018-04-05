#include <ESP8266WiFi.h> 

const char* ssid = "";
const char* password = "";
int ledPin = 13; 

WiFiServer server(80);

void setup()
{
  Serial.begin(115200);
  
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW); 

  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("");
  Serial.println("WiFi connected");
  
  server.begin();
  
  Serial.println("Server started");
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/"); 
}

void loop()
{
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  
  Serial.println("new client");
  while(!client.available()){
    delay(1);
  } 
  
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();
  
  int value = LOW;
  if (request.indexOf("/led=on") != -1)  {
    digitalWrite(ledPin, HIGH);
    value = HIGH;
  }
  if (request.indexOf("/led=off") != -1)  {
    digitalWrite(ledPin, LOW);
    value = LOW;
  }
  
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println(""); 
  client.println("");
  client.println("");
  client.print("Led is : "); 
  if(value == HIGH) {
    client.print("On");
  } else {
    client.print("Off");
  }
}
