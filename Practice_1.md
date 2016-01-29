Practice #1
===========

## Task #1: Development Environment

Prepare a [development environment](https://github.com/auca/com.250/blob/master/Environment.md) on your machine.

## Task #2: Hello

Create a new Android Studio project for an application to show a "hello, world" message.

## Task #3: Emulators and Real Devices

Create and start a new Android Virtual Device (AVD). Deploy and start your application on it.

Use `x86` or `x86-64` Android emulator images with the Intel Hardware Accelerated Execution Manager (HAXM). Lab machines have a memory limit of one gigabyte for the HAXM driver. Ensure that the RAM size of your virtual device is not higher than this limit.

Connect your device to the machine. On Windows, ensure that the [debug driver](http://developer.android.com/tools/extras/oem-usb.html) is installed. Don't forget to [enable debugging](http://developer.android.com/tools/device.html) on the device itself. Deploy and start your application on a real device.

## Task #4: Debugging

Open the Java code of your main activity. Place a break point inside the `onCreate` method. Start the application in debugging mode on your device or inside the emulator. Ensure that everything works by stepping through code inside your IDE.

## Task #5: Say Hello

Add a button to the main activity with a label "Say Hello". Remove text from the text view and add an `onClick` listener to the button to show a greeting.

## Reading

[Android Development Tools](http://developer.android.com/sdk/index.html)
