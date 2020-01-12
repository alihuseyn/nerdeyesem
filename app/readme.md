# Nerde Yesem Android Application

The application contains 4 activity. All the required application content described in the project are completed. All the svg and png are taken from [flaticon](https://www.flaticon.com/).

The fingerprint authentication is tested and the optional skip button is appended for user whom device not support.

**Note:** The Zomato detects location in Izmir and show restaurants according to the address when you are Guzelyurt, Manisa.

## Splash Activity
![Splash Activity](https://i.ibb.co/68CDgSz/2020-01-12-2.png)

## Fingerprint Authentication
![Auth Activity](https://i.ibb.co/ncfCJ3G/2020-01-12-4.png)

## Restaurants' List Activity (Main)
![Main Activity](https://i.ibb.co/DzryJq1/2020-01-12-1.png)

Empty Restaurant's Placeholder

![Main Activity Empty Placeholder](https://i.ibb.co/qytpK8R/2020-01-12-3.png)

## Restaurant Activity
![Restaurant Activity](https://i.ibb.co/CVCLTPn/2020-01-12.png)


## Project Description

App Name: Nerde Yesem
Build System: Build Gradle
Create an android application that;

* Gets restaurants whose location are close to the user and user selects one of them. **[V]**
* List at list 5 restaurants. **[V]**
* Selected restaurant information should be shown in a different page when the user clicked to a restaurant. **[V]**
    * To get user location, use mobile phone location. **[V]**
* The application should contain at least two pages, the "BACK" key must be implemented as "HistoryBack". **[V]**
* The application should be entered via Fingerprint **[V]**
* (https://developer.android.com/reference/android/hardware/fingerprint/FingerprintManager)
* The application should know its state when the user pushes it at the background. For example, **[V]**
    * I found a restaurant and opened restaurant details in the "Nerde Yesem"
    * I received a Whatsapp message and opened Whatsapp message
    * I then returned to "Nerde Yesem", the state should be the same when I left "Nerde Yesem".

Some keywords: Retrofit, RXJava, JSON, Location Manager, Fragment, Fingerprint, Java, Kotlin

UX/UI: It is up to the developer, there is no specific requirement but it is a bonus to create a fancy UI.

Restaurant API: https://developers.zomato.com/api