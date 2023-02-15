# ForecastApp
## Forecast is an android application which will help you to track weather forecasts in cities around the world, Based on Kotlin. 
Forecast app built with MVVM architecture consuming [visualcrossing API](https://www.visualcrossing.com) to show the following:
- The current weather data of the user location or the desired city like temperature, wind speed, humidity and visibility
- A summary of the weather forecast for the next seven days is displayed in a list
- Detailed weather forecast for every single day, As shown in these screenshots:
<div>
  <img src="https://user-images.githubusercontent.com/24722589/218619897-736a27bc-f861-4626-92cb-4e57b2e9e2d9.jpg" width= 150 height= 250>
  <img src="https://user-images.githubusercontent.com/24722589/218620158-7e6aba0c-d7a0-4b23-9162-197108a7a6b6.jpg" width= 150 height= 250>
  <img src="https://user-images.githubusercontent.com/24722589/218622759-dd7d20a2-cdc7-4d4c-8927-eecff30d5181.jpg" width= 150 height= 250>
</div>

## Architecture and Design
By going deeper into the application we find it built with MVVM architecture to achieve a more structured code and less tight coupling between components depending on three main layers:

#### Data layer

Responsible for getting data from any source, in our case we implemented the remote data source [visualcrossing API](https://www.visualcrossing.com)
with Retrofit to get weather data and store it in Room Database as shown below:

1.`Parsing Json:` the response from [visualcrossing API](https://www.visualcrossing.com) is in Json format so first we need to create equivalent java classes to store these data,
The Gson library is responsible for converting Json files to our Java classes

2.`Retrofit:` now it's time to implement Retrofit to get data from the server through the API interface with a client object to setup or build the Retrofit Api interface every call or request

3.`Weather network data source`: a sub-layer responsible for fetching the weather data from the client by retrieving it in Live Data.

4.`ForecastDB:` here is the Room Database contains three entities `CurrentWeatherEntry`, `FutureWeatherEntry` , `WeatherLocation` where the retrieved data is stored, its DAOs:
  `CurrentWeatherDAO`, `FutureWeatherDAO`,and `WeatherLocationDAO` 
  
5.`Providers:` is a separate package in the data layer responsible for providing us with the selected unit and the data from users like their locations or the desired city they entered to process them and retrieve the matched data in the user interface the goal is to make the code clean, readable and make the repository layer cleaner, So it is considered as a sub-package for the `Repository`.

6.`Repository:` is an important layer between the Room Database and View Model and responsible for most of the application logic, observes the Live Data "`downloadedCurrentWeather` and `downloadedFutureWeather`" from `Weather network data source` to fetch data if there are changes then store it in `ForecastDB` "by these functions `persistCurrentWeather()`, `persistFutureWeather()`",
also gets data from `ForecastDB` Depending on user entries then sends it to the view model layer as Live Data through interface `WeatherRepository`, updates database so if there are changes in data, these changes happen from user "Changes like unit system, user location or the desired city" or the last fetching time "If the last fetching time was from 30 minutes, then fetching data called automatically".
Considering the asynchronization by coroutines scopes with suspended functions.

#### View Model layer

Responsible for getting data from `Repository` in the Data layer and then sending it to the user interface, in our Forecast app the base view model is `WeatherViewModel` has a parameter from the `WeatherRepository` type so we can get all data we need from `Repository` and two general functions `getWeatherLocation()`, `getDeviceLocationName(latitude, longitude)`.
its sub-view models:

1.`CurrentWeatherViewModel:` has a parameter from the `WeatherRepository` and sends it to the base view model through inheritance to use the two functions above, and one more function `getCurrentWeather()`

2.`FutureWeatherViewModel:` It doesn't much different from `CurrentWeatherViewModel`, the one more function is `getFutureWeather()` to get the list of the next 7 days' weather forecast.

3.`FutureWeatherDetailsViewModel:` this view model has one more parameter of type `LocalDate` and also one more function `getDetailsOfFutureWeatherByDate()` to get the detailed weather data for one day of the 7 days with the same `LocalDate`, we send this parameter by the factory as it's changeable.

> **Note:** Every sub-view model is in the same package with its fragment.

#### User Interface (UI)

Screens representing user-visible data in the Forecast app that have 4 fragments with one activity designed using Navigation Components.
The `MainActivity` is responsible for Managing user location through the class `LifeCycleBoundLocationManager` that was created to keep `MainActivity` clean
and has two functions "`startLocationUpdates()`, `removeLocationUpdates()`" the first one is called in the `onResume()` phase in the lifecycle to get the updated user location, the second one is called in the `onDestroy()` phase in the lifecycle to remove location updates, and also managing location permissions.
It also has a navigation host to contain its fragments:

1.`CurrentWeatherFragment:` Responsible for displaying the current weather data which retrieved through `CurrentWeatherViewModel` by binding it in its views through the function `bindUI()`.
  
2.`FutureWeatherFragment:` Responsible for displaying the list of the next 7 days' weather forecast.

3. `FutureWeatherDetailsFragment:` Responsible for displaying weather forecasts in detail for every single day in the list.

4.`SettingsFragment:` lets users interact with the application by entering and selecting data. As shown below in the screenshots

<div>
  <img src="https://user-images.githubusercontent.com/24722589/218919348-ba049650-76a9-4a9f-a4bd-0ed0d96b1de7.PNG" width= 150 height= 250>
  <img src="https://user-images.githubusercontent.com/24722589/218919498-e6369760-60a2-4e93-9011-435e299b5ec0.PNG" width= 150 height= 250>
  <img src="https://user-images.githubusercontent.com/24722589/218919616-078530be-2669-4015-822c-d115d8181b6c.PNG" width= 150 height= 250>
</div>

#### Internal Package 

Separate package contains:
Glide Module for loading weather icon from the icon URL provided by [visualcrossing API](https://www.visualcrossing.com).

The exceptions file has `NoConnectivityException` that happens in case of No Internet Connection, and `LocationPermissionGrantedException` happens in case of missing permission.

`TasktoDeferred` file has function casts `lastLocatoin` in `getLastDeviceLocation()` "in LocationProviderImpl class" from task to deferred

Enum class `UnitSystem` has two unit types `METRIC` and `US`.

## Technologies

- MVVM Architecture Pattern
- Retrofit
- Room Database
- Kotlin Coroutines
- Kodein Dependency Injection: Kodein is a very useful library that allows us to:

   Lazily instantiate dependencies when needed.

   Stop caring about dependency initialization order through passing `instance()` and it's intelligent enough to know the type of this instance.

   Easily bind classes or interfaces to their instance, provider, or factory. As shown in Kodein class `ForecastApplication`

- Navigation Components
- Glide: This is an image-loading library for android that simplifies the process of loading and displaying images from external sources. in our case we use the icon URL provided by [visualcrossing API](https://www.visualcrossing.com).
- Gson
- Google Play Services for Location: Provides us with user location data by `FusedLocationProviderClient` through it we get the last location and location updates.
- Geocoder: Is used to handle geocoding and reverse geocoding. Geocoding is a process in which street address is converted into a coordinate (latitude, longitude). Reverse geocoding is a process in which a coordinate (latitude, longitude) is converted into an address. 
in our case we used it to translate a latitude and longitude value into an address (reverse geocoding) through the function `getDeviceLocationName(latitude, longitude)` this is because the [visualcrossing API](https://www.visualcrossing.com) sometimes returns the latitude and longitude of users location and doesn't recognize the location's name.
- Groupie RecyclerView: Is a simple, flexible library for complex RecyclerView layouts, It removes boilerplate code for creating viewholders, adapters
- View Binding

## Installation

Download the file or clone the repo then open it in the Android Studio

