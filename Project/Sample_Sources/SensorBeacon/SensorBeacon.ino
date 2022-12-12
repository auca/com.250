#if defined(ESP32)
  #include <WiFiMulti.h>
  #define DEVICE "ESP32"
  WiFiMulti wifiMulti;
#elif defined(ESP8266)
  #include <ESP8266WiFiMulti.h>
  #define DEVICE "ESP8266"
  ESP8266WiFiMulti wifiMulti;
#endif
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include <InfluxDbClient.h>

#include "Params.h"

InfluxDBClient client(INFLUXDB_URL, INFLUXDB_ORG, INFLUXDB_BUCKET, INFLUXDB_TOKEN);
Point temperatureSensor(INFLUX_TEMP_SENSOR_NAME);
Point humiditySensor(INFLUX_HUMIDITY_SENSOR_NAME);

DHT_Unified dht(DHT_PIN, DHT_TYPE);
uint32_t dhtDelay;

void setup() {
  Serial.begin(SERIAL_SPEED);

  Serial.println("The Temperature Beacon is starting...");
  Serial.print("Connecting to WiFi...");
  WiFi.mode(WIFI_STA);
  wifiMulti.addAP(WIFI_SSID, WIFI_PASS);
  WiFi.config(IPAddress(192, 168, 0, 200), IPAddress(192, 168, 0, 1), IPAddress(255, 255, 255, 0));
  while (wifiMulti.run() != WL_CONNECTED) {
    Serial.print(".");
    delay(WIFI_DELAY);
  }
  Serial.println();

  temperatureSensor.addTag("device", DEVICE);
  humiditySensor.addTag("device", DEVICE);

  if (client.validateConnection()) {
    Serial.print("Connected to InfluxDB: ");
    Serial.println(client.getServerUrl());
  } else {
    Serial.print("InfluxDB connection failed: ");
    Serial.println(client.getLastErrorMessage());
  }

  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  dhtDelay = sensor.min_delay / 1000;
}

void loop() {
  delay(dhtDelay);

  if (wifiMulti.run() != WL_CONNECTED) {
    Serial.println("Wifi connection lost. Attempting to reconnect...");
    return;
  }

  sensors_event_t event;
  dht.temperature().getEvent(&event);
  if (isnan(event.temperature)) {
    Serial.println("Error reading temperature data.");
  } else {
    Serial.print("Temperature: ");
    Serial.print(event.temperature);
    Serial.println("C");

    temperatureSensor.clearFields();
    temperatureSensor.addField("data", event.temperature);
    Serial.print("Attempting to send: ");
    Serial.println(client.pointToLineProtocol(temperatureSensor));
    if (!client.writePoint(temperatureSensor)) {
      Serial.print("InfluxDB write failed: ");
      Serial.println(client.getLastErrorMessage());
    }
  }

  dht.humidity().getEvent(&event);
  if (isnan(event.relative_humidity)) {
    Serial.println("Error reading humidity data.");
  } else {
    Serial.print("Relative humidity: ");
    Serial.print(event.relative_humidity);
    Serial.println("%");

    humiditySensor.clearFields();
    humiditySensor.addField("data", event.relative_humidity);
    Serial.print("Attempting to send: ");
    Serial.println(client.pointToLineProtocol(humiditySensor));
    if (!client.writePoint(humiditySensor)) {
      Serial.print("InfluxDB write failed: ");
      Serial.println(client.getLastErrorMessage());
    }
  }
}
