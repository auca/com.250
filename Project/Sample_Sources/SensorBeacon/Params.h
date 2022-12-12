#ifndef PARAMS_H
#define PARAMS_H

#define SERIAL_SPEED 9600

#define WIFI_SSID "?"
#define WIFI_PASS "?"
#define WIFI_DELAY 500

#define INFLUXDB_URL "http://192.168.0.?:8086"
#define INFLUXDB_TOKEN "?"
#define INFLUXDB_ORG "AUCA"
#define INFLUXDB_BUCKET "SensorData"
#define INFLUX_TEMP_SENSOR_NAME "temperature"
#define INFLUX_HUMIDITY_SENSOR_NAME "humidity"

#define DHT_PIN 4
#define DHT_TYPE DHT11

#endif // PARAMS_H
