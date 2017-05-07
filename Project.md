Course Project
==============

## Task

In this project we will create a simple drawing application in several steps.
The application allows to create, preview, edit, an delete simple doodle
drawings on Android. The application can also store the list of all drawings
created by the user. The application presents different layouts for phones and
tablets.

### Labs

1. A user can doodle a simple picture on a custom view through the Canvas
   objects.

2. A user can manage a list of doodles. The interface should be adapted to
   phones and tablets with the use of fragments. The application should be based
   on the master–detail porject template. The drawings should be stored as files
   in the application's local directory.

3. Allow to store drawings or metadata (title, creation time, file path) in a
   SQLite database.

## Bonus Task

Modify the drawing application to send doodles to the
[monet](https://github.com/toksaitov/monet) system to transfer a style from a
different picture to the user's drawing. The application should be able to
report the progress of the style transfer operation and present intermediate or
final images to the user.

## Monet, an Image Generation Service

![Architecture](http://i.imgur.com/DbMzzpQ.png)

*monet* is a distributed image generation system for the
[neural-doodle](https://github.com/alexjc/neural-doodle) project.

* [monet](https://github.com/toksaitov/monet)
  * [monet-api](https://github.com/toksaitov/monet-api)
  * [monet-agent](https://github.com/toksaitov/monet-agent)

### Supporting Services

The following images are required to run the
[monet](https://github.com/toksaitov/monet) system.

* [neural-doodle-cpu](https://github.com/toksaitov/neural-doodle-cpu)
* [neural-doodle-gpu](https://github.com/toksaitov/neural-doodle-gpu)

