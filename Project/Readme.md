Course Project (SensorBeacon)
=============================

## Requirements

In this project you will have to create a software system using the NodeMCU board to collect time series data from various sensors. The collected data will be recorded to InfluxDB, a time series database, and visualized on the web using Graphana. Additionally, students will also build a custom mobile app for iOS or Android to display the sensor data.

The project will involve writing the Arduino code for the NodeMCU board to communicate with the sensors and collect the data. The mobile app will be built using the appropriate tools for either iOS or Android. The app will be able to display the sensor data in real-time, providing a convenient way for users to monitor their sensors on the go.

Using the InfluxDB database, the collected data can be stored and accessed for further analysis. The Graphana visualization tool will be used to display the data in an easy-to-understand format, allowing users to quickly and easily interpret the data.

Overall, this project will provide students with a comprehensive understanding of the technologies and techniques involved in mobile and IoT development, including the use of NodeMCU boards, mobile app development, and time series data management and visualization.

1. Set up the NodeMCU board and connect it to the appropriate sensors.
2. Write the Arduino code for the NodeMCU board to collect data from the sensors and store it in InfluxDB.
3. Use InfluxDB and Graphana to set up storage and visualizations for the sensor data.
4. Build the mobile app for iOS or Android using the appropriate tools.
5. Integrate the mobile app with the NodeMCU board and Graphana to display the sensor data in real-time. Use some popular charting library.
6. Test the system to ensure that it is functioning properly and that the sensor data is being collected and visualized correctly.
7. Make any necessary adjustments or improvements to the system based on the test results.
8. Write a Readme.md file describing the steps to deploy your system.

## Submission

Put the root project directory under the folder `project-01` in your private course repository on GitHub. If you don't have a private repository on GitHub for this course, ask the instructor for instructions on how to create one. Ensure that you have proper `.gitignore` files in your project folders to ignore intermediate and compiled files. We do not need them in your repository. Finally, commit and push the work to GitHub. The last commit URL to a snapshot representing your final work must be submitted to Canvas before the deadline.

You have to create only one app for one platform of your choice. If you have decided to develop apps for both platforms, put the projects under subfolders `android` and `ios` in your `project-01` directory.

### Android

* [Android Studio](https://developer.android.com/studio)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)

### iOS

* [Xcode](https://developer.apple.com/xcode)
* [SwiftUI](https://developer.apple.com/documentation/swiftui)
