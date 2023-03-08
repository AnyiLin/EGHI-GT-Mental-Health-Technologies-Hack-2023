--![Release](https://jitpack.io/v/Mentalab-hub/explore-android.svg)


Overview
==================

Explore Android API is Mentalab's open-source biosignal acquisition API for use with Mentalab Explore devices. The code can also be adapted to use in Android ecosystems. Among others, it provides the following key features: :


* Real-time streaming of ExG, orientation and environmental data
* Connect, pair and search Explore device via Bluetooth
* Record data in csv format
* Push data to Lab Streaming Layer(LSL)
* Change device settings
* Measure impedance
* Filter and stream raw data 

Please note that the API is not guranteed in all Android versions. The API functionality is tested in Pixel phones with Android 13 OS.

Requirements
==================

* Android Studio with SDK bundle from this link: <https://developer.android.com/studio>
* Android device with at least Android Lollipop(OS version 5.1)


Quick installation
==================

To add the library to your project:

* In your project’s build.gradle add the following line
```
maven { url ‘https://jitpack.io’ }
```

![alt text](https://github.com/Mentalab-hub/explore-android/blob/master/screenshots/maven.png?raw=true)

* Add the following dependency in your app level build.gradle file
```
implementation 'com.github.Mentalab-hub:explore-android:V_1.0'
```

* Add the following permisions in your android manifest:
```
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.INTERNET" />
```
* Sync gradle and Mentlab API is ready to use!


Please check troubleshooting section of this document in case of issues.


Documentation
=============

For the full documentation of the API, please refer to javadoc folder in code repository.

Troubleshooting
===============

* If your phone is not recognized by Android Studio, make sure that USB debugging is turned on on your Android device.
* Make sure to pair the Explore device with your phone at first.

You can also create a new issue in the GitHub repository.

Authors
=======

* [Salman Rahman](https://github.com/salman2135)
* [Alex Platt](https://github.com/Nujanauss)
* [Florian Sesser](https://github.com/hacklschorsch)


License
=======
This project is licensed under the MIT license at <https://github.com/Mentalab-hub/explore-android/blob/master/LICENSE>. You can reach us at contact@mentalab.com.
