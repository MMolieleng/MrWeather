# MrWeather

Location based weather app. 

## Installation
Clone the project using Android Studio 3.3.1(latest android studio version)


## Used Libraries
1. [Lombok for android](https://projectlombok.org/setup/android)
2. [Joda Time](https://github.com/dlew/joda-time-android) for date conversions and formatting
3. [OkHttp3](https://github.com/square/okhttp) for API calls
4. JUnit for unit testing

## Approach
In this project I used MVVM design pattern to implement my solution. Because of limitted number of icon assets i was given, I used the sun icon to indicate Sunny days and used the partly cloudy icon to indicate rainy and cloudy days in the forecast list.

## Possible Improvements
* Show user's location (city)
* Display weather icons provided by the api using Glide or Picasso library
* Add some animations to make the app fun to use
* Add UI tests using Expresso library

## Screenshots
|![alt text](https://github.com/MMolieleng/MrWeather/blob/master/cloudy.jpeg) | ![alt text](https://github.com/MMolieleng/MrWeather/blob/master/rainy.jpeg) | 
