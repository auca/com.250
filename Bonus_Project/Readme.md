Bonus Project
=============

![Chart of Temperature](https://i.imgur.com/0etgDLL.png)

## Requirements

Create an app that displays information about temperature and humidity from an IoT device equipped with a temperature and humidity sensor. This app will enable users to monitor the environment of interest conveniently from a mobile application. The app may use any chart library to visualize the data. It should display the data in real time for the last 3 hours, refreshing the chart every 10 seconds. The latest value should be prominently displayed in a large font at the center of the screen.

* The app should maintain an acceptable visual quality.
* A certain level of creative freedom is permitted, provided all the components and logic outlined here and during the classes are incorporated.

## Backend Description

An IoT device, based on the [ESP8266](https://www.espressif.com/en/products/socs/esp8266) SOC, with a connected [DHT11](https://www.adafruit.com/product/386) temperature and humidity sensor, is configured to report data to a time series database, [InfluxDB](https://www.influxdata.com), approximately every 10 seconds for storage. This database can be queried and monitored through an HTTP API endpoint, created using the popular monitoring solution [Grafana](https://grafana.com). All services are deployed, configured, and orchestrated via a Docker containerization system on our `auca.space` server. The Grafana instance is accessible on port `3000`, and you will be primarily interested in its `/api/ds/query` API endpoint, which provides time and temperature data. The description of this API endpoint is available [here](https://grafana.com/docs/grafana/latest/developers/http_api/data_source/). Below is a sample CURL command to query the data for the last 3 hours:

```bash
curl --location 'http://auca.space:3000/api/ds/query'                                  \
     --header   'Content-Type: application/json'                                       \
     --header   'Accept: application/json'                                             \
     --header   'Authorization: Bearer glsa_2JTFbJruTx7YMCdcIXBcVQqfT1Am8VB3_5f8b1c2e' \
     --data     '{
                     "queries": [{
                         "datasource": {
                             "type": "influxdb",
                             "uid": "ebfaf006-f8aa-460f-a47a-bf3530dc9bf4"
                         },
                         "query": "from(bucket:\"sensors\")\n    |> range(start: -3h)\n    |> filter(fn: (r) => r[\"_measurement\"] == \"temperature\")"
                     }],
                     "from": "now-3h"
                 }'
```

The output of the CURL command can be piped into Python (`python3 -m json.tool`) for more readable output. The Grafana token and data source UID, publicly available on GitHub, are configured on the server with read-only permissions.

The format of the reply is outlined below:

```json
{
    "results": {
        "A": {
            "status": 200,
            "frames": [
                {
                    "schema": {
                        "name": "temperature",
                        "...": "..."
                    },
                    "data": {
                        "values": [
                            [
                                1703207436309,
                                1703207437586,
                                1703207438888,

                            ],
                            [
                                31.1,
                                31.1,
                                31.1,

                            ]
                        ]

                    }
                }
            ]
        }
    }
}
```

Focus on the values JSON array, which consists of two arrays of equal length. The first array contains a list of timestamps in milliseconds since the UNIX epoch, and the second array lists temperature values in degrees Celsius.

## Frontend Description

Build your own custom component to render the chart, or preferably use a library. You have complete freedom regarding which library to use. Research popular libraries independently. The chart should update every 10 seconds with the latest data from the past three hours. The latest value should be prominently displayed in a large font at the center of the screen. During startup, the app should display a loading indicator or message.

### Android

* [Network Operations Overview](https://developer.android.com/training/basics/network-ops)
* [Retrofit](https://square.github.io/retrofit/)

### iOS

* [iOS Networking](https://developer.apple.com/documentation/network)
* [Alamofire](https://github.com/Alamofire/Alamofire)

### Flutter

* [Navigation in Flutter](https://docs.flutter.dev/cookbook/navigation/navigation-basics)
* [Flutter Networking](https://docs.flutter.dev/data-and-backend/networking)
