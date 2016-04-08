Practice #5
===========

![sample](http://i.imgur.com/CPePyJW.png)

## Task #1: Image Analyzer

Create an image analysis application that utilizes the [Google Cloud Vision
API](https://cloud.google.com/vision) service.

Your app should use implicit intents to start a system activity to [snap a
photo](http://developer.android.com/guide/topics/media/camera.html). On success
your should start an activity (this time with an explicit intent) to present the
selected image. The activity should at least contain an `ImageView` UI widget to
show the photo. You can also add an `EditText` control to present debug
information.  The app should send the selected photo to the [Google Cloud Vision
API](https://cloud.google.com/vision) service for analysis. On success, the
[JSON reply](https://cloud.google.com/vision/docs/requests-and-responses) from
Google with a list of image features should be parsed and presented in some
interesting way on top of the photo by utilizing the [Android 2D-drawing
APIs](http://developer.android.com/guide/topics/graphics/2d-graphics.html). You
can use different font sizes or colors (opacity levels) to present the
likelyhood of a certain feature. You can also draw primitive shapes to show
recognized feature boundaries.

For all API requests to the [Google Cloud
Vision](https://cloud.google.com/vision/docs/auth-template/cloud-api-auth)
service you can use the following [API
key](https://docs.google.com/a/auca.kg/document/d/1ecsmknQBhigu2JzBJwMQa-5t27itGBBvZH6ypmO_KdM)
(use your AUCA account to access the document).

You can send request parameters as a JSON string, but you need to parse reply
data with a `JSONObject`.

UI Widgets that you need to use

* `Button`
* `EditText`
* `ImageView`

Activities that you need to create

* Menu
  - `Button` (opens a system activity to snap a photo)
* Result
  - `ImageView` (a photo with feature labels drawn on top of it)
  - `EditText` (debug information such as service reply)

## Reading

### Android Documentation

* [App Fundamentals](http://developer.android.com/guide/components/fundamentals.html)
* [Intents and Intent Filters](http://developer.android.com/guide/components/intents-filters.html)
* [Common Intents](http://developer.android.com/guide/components/intents-common.html)
* [App Resources](http://developer.android.com/guide/topics/resources/providing-resources.html)
* [Storage Options](http://developer.android.com/guide/topics/data/data-storage.html)
* [Processes and Threads](http://developer.android.com/guide/components/processes-and-threads.html)
* [Camera](http://developer.android.com/guide/topics/media/camera.html)
* [Connecting to the Network](http://developer.android.com/training/basics/network-ops/connecting.html)
* [JSONObject](http://developer.android.com/reference/org/json/JSONObject.html)
* [Canvas and Drawables](http://developer.android.com/guide/topics/graphics/2d-graphics.html)

### Java Documentation

* [Basic I/O](https://docs.oracle.com/javase/tutorial/essential/io)

### JSON

* [JSON](http://www.json.org)

### Google Cloud Vision API

* [Service Overview](https://cloud.google.com/vision)
* [API Documentation](https://cloud.google.com/vision/docs/getting-started)
