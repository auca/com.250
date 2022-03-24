Practice #2 (Dungeon)
=====================

## Requirements

Create a game app for the Android or iOS platform that allows a player to navigate a 2D maze by switching from one screen of an app to another one. In addition, the app should allow undoing the steps for the user by going back in the navigation stack.

* You can use the following map with `@` representing the entrance and `$` representing the goal. The `#` symbol should be used to outline walls, and the ` ` should be used to trace the walkable area.

```
######
#@   #
#### #
#    #
# ####
#   $#
######
```

* You can use any other maze if you want.

* The player should be represented with arrows (in Unicode or ASCII). The arrows should illustrate the current direction of the player.

* The app should allow the player to navigate with the buttons to move forward, turn right, and turn left.

* The app should allow undoing steps by going back in the navigation stack.

* The app should target a common phone form factor. You may limit your app to work only in that mode in the project's configurations.

* The app should target the portrait screen mode. You may limit your app to work only in that mode in the project's configurations.

* The app should be of acceptable visual quality.

* A certain level of creative freedom is acceptable here as long as you have all the components and logic outlined here and during the classes.

## Submission

Put the root project directory under the folder `lab-2` in your private course repository on GitHub. If you don't have a private repository on GitHub for this course, ask the instructor for instructions on how to create one. Ensure that you have proper `.gitignore` files in your project folders to ignore intermediate and compiled files. We do not need them in your repository. Finally, commit and push the work to GitHub. The last commit ID to a snapshot representing your final work must be submitted to Canvas before the deadline.

You have to create only one app for one platform of your choice. If you have decided to develop apps for both platforms, put the projects under subfolders `android` and `ios` in your `lab-2` directory.

### Android

* [Navigation with Jetpack Compose](https://developer.android.com/jetpack/compose/navigation)

### iOS

* [NavigationView in SwiftUI](https://developer.apple.com/documentation/swiftui/navigationview)
