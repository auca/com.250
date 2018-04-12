#include <ESP8266WiFi.h> 

static const char *WIFI_SSID = "AU-Guest";
static const char *WIFI_PASSWORD = "";

WiFiServer Server(80);

void setup()
{
    Serial.begin(115200);

    pinMode(LED_BUILTIN, OUTPUT);
    digitalWrite(LED_BUILTIN, LOW);

    Serial.print("Connecting to ");
    Serial.println(WIFI_SSID);

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("connected");

    Server.begin();

    Serial.print("Server started at ");
    Serial.print("http://");
    Serial.print(WiFi.localIP());
    Serial.println("/");
}

void loop()
{
    WiFiClient client = Server.available();
    if (!client) {
        return;
    }

    while(!client.available()){
        delay(1);
    }

    String request = client.readStringUntil('\r');
    Serial.println(request);
    client.flush();

    int value = LOW;
    if (request.indexOf("/led=on") != -1)  {
        digitalWrite(LED_BUILTIN, HIGH);
        value = HIGH;
    }
    if (request.indexOf("/led=off") != -1)  {
        digitalWrite(LED_BUILTIN, LOW);
        value = LOW;
    }

    client.println("HTTP/1.1 200 OK");
    client.println("Content-Type: text/html");
    client.println("");
    client.println("");
    client.println("");
    if(value == HIGH) {
        client.print("on");
    } else {
        client.print("off");
    }
}

