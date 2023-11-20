Practice #1 (Kaboom!)
=====================

## Requirements

<img src="https://i.imgur.com/euHEqlL.png" width="200">

<img src="https://i.imgur.com/c21riRf.png" width="200">

Create a game app for the Android or iOS platform that generates random instructions for the user to perform with standard UI components on the screen such as text and password fields, toggles, buttons, and sliders to disarm an imaginary bomb in the game.

* Your app MUST include at least the following components on iOS:
  * TextField
  * SecureField
  * Button
  * Toggle
  * Stepper
  * Slider

* Your app MUST include at least the following components on Android:
  * TextField
  * TextField used as a Password Field
  * Button
  * Switch
  * RadioButton
  * Checkbox
  * Slider

* If the instructions are followed correctly by the user and the disarm button is pressed, show a vector image with any appropriate success symbol covering the screen. Hide the disarm button.

* If just a single instruction was NOT followed correctly by the user, show a raster image of an explosion covering the screen. Hide the disarm button.

* It is up to you to select the images, but ensure they are not offensive. Ensure that the success image is vector-based and the explosion image is raster-based. Both of them must be of an acceptable resolution and size.

* The app should contain a button to restart/reset the game at any point in the game logic.

* The app should target a common phone form factor. You may limit your app to work only in that mode in the project's configurations.

* The app should target the portrait screen mode. You may limit your app to work only in that mode in the project's configurations.

* The app should be of acceptable visual quality.

* A certain level of creative freedom is acceptable here as long as you have all the components and logic outlined here and during the classes.

## Submission

Put the root project directory under the folder `lab-01` in your private course repository on GitHub. If you don't have a private repository on GitHub for this course, ask the instructor for instructions on how to create one. Ensure that you have proper `.gitignore` files in your project folders to ignore intermediate and compiled files. We do not need them in your repository. Finally, commit and push the work to GitHub. The last commit ID to a snapshot representing your final work must be submitted to Canvas before the deadline.

You have to create only one app for one platform of your choice. If you have decided to develop apps for both platforms, put the projects under subfolders `android` and `ios` in your `lab-01` directory.

### Android

* [Android Studio](https://developer.android.com/studio)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)

### iOS

* [Xcode](https://developer.apple.com/xcode)
* [SwiftUI](https://developer.apple.com/documentation/swiftui)

### Flutter

* [Flutter](https://flutter.dev)
* [Dart](https://dart.dev)
