# Permissions Demo

## Overview

Many (possibly most) apps require one or more permissions to be granted. These are specified via `<use-permission>` elements in `AndroidManifest.xml`. For some permissions, the grants occur on installation; for others&mdash;considered "dangerous" permissions&mdash;the user must explicitly grant the permission while the app is running. Requesting permission from the user isn't difficult, but implementing the interaction can be tricky. The [`PermissionsService`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/service/PermissionsService.java) class in this project implements a basic, but fairly typical, interaction flow. It may be dropped (along with at least one required `string` value resource) directly into an Android Jetpack-based project; alternatively, it may be used as a starting point for implementing a different permission interaction flow.

The project includes not only the [`PermissionsService`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/service/PermissionsService.java) class (and the required `string` resource named `permissions_dialog_title`), but also an example of its use, in `MainActivity`.

## Requirements

* Jetpack libraries (`androidx.*`)
    * `androidx.appcompat:appcompat`
    * `androidx.lifecycle:lifecycle-livedata` (This is  also included in the `androidx.lifecycle:lifecycle-extensions` aggregator library, through version 2.2.0.)
* Java 8 language level 
* Android API 24+

## Usage

The "active" portions of [`PermissionsService`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/service/PermissionsService.java) (those dealing with requesting permission from the user) are intended to be consumed by an `Activity`. In general, this should be the primary activity of an app&mdash;either the main activity, or (if, for example, the main activity is a splash or login activity), the activity hosting the main navigation controls of an app.

### Getting a reference to an instance of `PermissionsService`

[`PermissionsService`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/service/PermissionsService.java) is implemented as a _singleton_, since the "passive" portion of its functionality may be consumed by multiple fragments or activities in the app. This passive portion is an accessor method exposing a `LiveData<Set<String>>` instance, which can be observed to get the set of permissions specified in `AndroidManifest.xml` that have been granted (implicitly or explicitly) by the user.

To get a reference to the [`PermissionsService`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/service/PermissionsService.java) singleton, invoke the `PermissionsService.getInstance()` method. An example of this is seen in the value assigned to the `permissionsService` field of [`MainActivity`](app/src/main/java/edu/cnm/deepdive/permissionsdemo/controller/MainActivity.java) in this example app.

### Checking/requesting permissions

To check &amp; request (as necessary) permissions with the `PermissionsService`, invoke its `checkPermissions(AppCompatActivity activity, int requestCode)` method.

* The value of the first parameter will typically be `this`, since it is expected that the method will be invoked from an `AppCompatActivity` subclass instance.

* The `int requestCode` parameter value is entirely up to the consumer. Whatever the value used, the activity's `onRequestPermissionsResult` should check for the same value in the `requestCode` parameter of that method.

An example invocation of `PermissionsService.checkPermissions` is in the `MainActivity.checkPermissionsOnce` method, which is itself invoked by`MainActivity.onCreate`.  

### Receiving permission grant results

If `PermissionsService.checkPermissions` determines that one or more permissions must be requested from the user, the standard Android permissions dialog will be be displayed (possibly following the display of an information dialog, stating the rationale for any such permission previously denied by the user). After the user selects the desired action (**Deny** or **Allow**) for all of the permissions in question, the activity's `onRequestPermissionsResult` method is invoked. In general, all that method will need to do is check the value of the `requestCode` parameter, and&mdash;if it matches the value passed in the `checkPermissions` invocation&mdash;invoke the `PermissionsService.updatePermissions` method.

See the `MainActivity.onRequestPermissionsResult` method for an example.


### Observing the set of granted permissions.

`PermissionsService` includes a `getPermissions()` method that returns `LiveData<Set<String>>`. The `LiveData.observe` method can be invoked on this return value, to specify an `Observer` of these permissions. Such an observer could hide or display controls in the UI, display additional messages (as `Toast`, `Snackbar`, or `AlertDialog` instances), or even stop the app completely (e.g. using `finishAndRemoveTask()`) if the granted permissions are insufficient for normal operation of the app.

An example of such an `Observer` (written as a lambda) can be seen in `MainActivity.observePermissions`, invoked from `MainActivity.onCreate`. In this example, the set of granted permissions are used to populate an `ArrayAdapter<String>`, which in turn supplies the contents of a `ListView`.

### Avoiding repeated permission requests on configuration changes

By default, when a configuration change occurs (e.g. an orientation change after device rotation), the current activity (and any hosted fragments) will be destroyed and recreated. If the activity's `onCreate` method invokes `PermissionsService.checkPermissions` (directly or indirectly) unconditionally, then permissions will be re-checked every time the device is rotated; in general, this would (rightfully) be considered excessive.

One approach for reducing unnecessary permissions checks is to use a lifecycle-aware Boolean-valued flag (e.g. `LiveData<Boolean>`), tied to the lifecycle of the main navigation activity, or the entire process, to record that permissions have been checked; by observing such a flag, redundant permissions checks can be avoided.

An example of this approach can be seen in the `MainActivity.checkPermissionsOnce` method.

## License information

_Permissions Demo_ (including `PermissionsService`) was written by Nick Bennett.

Copyright 2020 Deep Dive Coding/CNM Ingenuity, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

For copyright &amp; license information on the libraries incorporated into _Sliding Tiles_, see [_Notice_](docs/notice.md).