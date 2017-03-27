Practice #6
===========

![Example](http://i.imgur.com/zi0NXL6.jpg)

## Task #1: Converter

Create a converter application. The application should allow to convert from one
unit of measurement to another. The application should include at least length,
mass, and currency conversion capabilities.

The program should contain three separate activities

* An activity for a central hub/menu with buttons leading to other activites
* The lenght and mass conversion activity
* The currency conversion acitivity

Use `Intent` objects and the `startActivity` method to switch to another
activities.

UI widgets that you need to use

* `EditText`
* `TextView`
* `Button`
* `Spinner`

Try to use JSON (`JSONObject`) and XML (`getResources().obtainTypedArray()`) to
store conversion factors and unit/currency names.

Use `SharedPreferences` to load and save the state of the application
(specifically values from all input fields).

Find out how to get access to files from the `raw` group under `res` with the
`getResources().openRawResource(...)` call.

## Task #2: Currency

Make the currency conversion activity to fetch currency rates from a 3-rd party
provider such as <http://fixer.io>. Do the work on a background thread. Update
UI on the main UI thread. Do not forget to add appropriate permissions to the
manifest file. Ensure that the application still continues fetching data even
during configuration change events (e.g., a device orientation is changing).

Use 3-rd party libraries to simplify your networking code. You can consider
using the following popular libraries

* [Retrofit](https://github.com/square/retrofit)
* [RoboSpice](https://github.com/stephanenicolas/robospice)

Consider separating you calculation logic away from the presentation layer.

You can find a sample project for this task on the instructor's GitHub account.

## Reading

### Android Documentation

* [App Fundamentals](http://developer.android.com/guide/components/fundamentals.html)
* [Intents and Intent Filters](http://developer.android.com/guide/components/intents-filters.html)
* [App Resources](http://developer.android.com/guide/topics/resources/providing-resources.html)
* [Storage Options](http://developer.android.com/guide/topics/data/data-storage.html)
* [Processes and Threads](http://developer.android.com/guide/components/processes-and-threads.html)
* [App Manifest](http://developer.android.com/guide/topics/manifest/manifest-intro.html)
* [UI Overview](http://developer.android.com/guide/topics/ui/overview.html)
* [Input Controls](http://developer.android.com/guide/topics/ui/controls.html)
* [Input Events](http://developer.android.com/guide/topics/ui/ui-events.html)
* [RelativeLayout](http://developer.android.com/guide/topics/ui/layout/relative.html)
* [Spinners](http://developer.android.com/guide/topics/ui/controls/spinner.html)
* [Styles and Themes](http://developer.android.com/guide/topics/ui/themes.html)
* [JSONObject](http://developer.android.com/reference/org/json/JSONObject.html)
* [Networking Basics](http://developer.android.com/training/basics/network-ops/connecting.html)

### Java Documentation

* [Basic I/O](https://docs.oracle.com/javase/tutorial/essential/io)

### JSON

* [JSON](http://www.json.org)

