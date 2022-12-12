SensorBeacon
============

SensorBeacon is a system to record temperature and humidity data from DHT11 or 22 sensor into InfluxDB, visualize it on desktop machines with Graphana and on mobile with a custom app.

## Prerequisites

* ESP8266 or ESP-32 NodeMCU-compatible board
* Arduino IDE or PlatformIO with ESP\* and Adafruit DHT libraries installed
* Docker to start InfluxDB and Graphana or InfluxDB and Graphana installed on their own
* Xcode and Android Studio to build and deploy the apps

## Usage

1. Install Arduino IDE or Platform.IO.
2. Install ESP8266 or ESP-32 platform inside the selected IDE.
3. Install the Adafruit DHT [library](https://github.com/adafruit/DHT-sensor-library) and its dependencies.
4. Install InfluxDB client [library](https://github.com/tobiasschuerg/InfluxDB-Client-for-Arduino) for Arduino.
5. Install the board Serial-to-USB drivers (such as [CP210x](https://www.silabs.com/developers/usb-to-uart-bridge-vcp-drivers?tab=downloads) or [CH340](https://sparks.gogo.co.nz/ch340.html))
6. Wire your device in the following way

    ![ESP8266 Wiring to DHT11](https://hackster.imgix.net/uploads/attachments/607649/dh11_fiz_MEDBu8aQAn.png)

7. Open the source code in the IDE
8. Change the "Params.h" file specifying your parameters and secrets.
9. Connect the board to your IDE.
10. Compile and upload the code.
11. Deploy InfluxDB and Graphana manually or through Docker by running `docker compose up` from the source folder.
12. Configure InfluxDB and Graphana.
13. Open the iOS or Android project depending on your personal preferences.
14. Compile and deploy the code to your device.
