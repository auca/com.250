Practice #5
===========

![Example](http://i.imgur.com/xgGgB5I.jpg)

## Task #1: Primitive Calculator

Create a primitive calculator application. The application should contain two
input fields for operand values, four radio boxes to select a mathematical
operation (`Add`, `Subtract`, `Multiply`, and `Divide`), a button to perform
calculations, and an output text field to show results.

UI widgets that you need to use

* `EditText`
* `RadioGroup` and `RadioButton`
* `Button`
* `TextView`

Set `InputType` for `EditText` field to `numberDecimal`.

Check for invalid input data or attempts to divide by zero. Show `Toast`
popups with user-friendly error messages for such cases.

Round results of the calculations to two decimal places.

Consider separating you calculation logic away from the presentation layer.

Ensure that the application can survive orientation change events.

## Task #2: Event Listeners

Remove the button. You application should automatically recalculate results for
every change inside the input fields. Changes to selection in the radio group
should also trigger the recalculation.

## Reading

### Android Documentation

* [App Fundamentals](http://developer.android.com/guide/components/fundamentals.html)
* [UI Overview](http://developer.android.com/guide/topics/ui/overview.html)
* [Input Controls](http://developer.android.com/guide/topics/ui/controls.html)
* [Input Events](http://developer.android.com/guide/topics/ui/ui-events.html)
* [Toasts](http://developer.android.com/guide/topics/ui/notifiers/toasts.html)

