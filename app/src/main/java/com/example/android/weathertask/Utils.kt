package com.example.android.weathertask

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.android.weathertask.Data.WeatherForecast
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object Helpers{

        fun decodeJsonToModel(jsonString: String) : Array<WeatherForecast>{
            return Json.decodeFromString(jsonString)
        }

        fun findLowestTempInAllCities(weatherForecastArray: Array<WeatherForecast>): Double{
            var lowestTemp = Double.MAX_VALUE

            for(weatherForcast in weatherForecastArray){
                for(hourlyTemp in weatherForcast.hourlyTemp){
                    if(hourlyTemp.temp < lowestTemp){
                        lowestTemp = hourlyTemp.temp
                    }
                }
            }
            return lowestTemp
        }

        fun findHighestTempForSingleCity(weatherForecast: WeatherForecast): Double{
            var highestTemp = Double.MIN_VALUE

            for(hourlyTemp in weatherForecast.hourlyTemp){
                if(hourlyTemp.temp > highestTemp){
                    highestTemp = hourlyTemp.temp
                }
            }
            return highestTemp
        }

        fun findLowestTempForSingleCity(weatherForecast: WeatherForecast): Double{
            var lowestTemp = Double.MAX_VALUE
            for(hourlyTemp in weatherForecast.hourlyTemp){
                if(hourlyTemp.temp < lowestTemp){
                    lowestTemp = hourlyTemp.temp
                }
            }
            return lowestTemp
        }

        fun findCityWithLowestAverageDailyTemperature(weatherForecastArray: Array<WeatherForecast>): String{
            var city = ""
            var lowestAverageTemp = Double.MAX_VALUE

            for(weatherForcast in weatherForecastArray){
                val averageTemp = calculateAverageDailyTemperatureForSingleCity(weatherForcast)
                if(averageTemp < lowestAverageTemp){
                    lowestAverageTemp = averageTemp
                    city = weatherForcast.city
                }
            }
            return city
        }

        fun calculateAverageDailyTemperatureForSingleCity(weatherForecast: WeatherForecast) : Double{
            var result = 0.0;

            for(hourlyTemp in weatherForecast.hourlyTemp){
                result += hourlyTemp.temp
            }
            return result / weatherForecast.hourlyTemp.size
        }

        fun formatTemperature(context: Context, temperature: Double): String{

            var resourceId = -1
            var temperatureToDisplay = temperature
            if(isMetric(context)) {
                resourceId = R.string.format_temperature_celsius
            } else {
                resourceId = R.string.format_temperature_fahrenheit
                temperatureToDisplay = Utils.celsiusToFahrenheit(temperature)
            }

            return String.format(context.getString(resourceId), temperatureToDisplay)
        }

        fun getWeatherDescription(weatherForecast: WeatherForecast): String {
            return weatherForecast.weather.capitalize()
        }

        fun getWeatherIcon(weatherForecast: WeatherForecast): Int {
            return when(weatherForecast.weather){
                "cloudy" -> R.drawable.ic_cloudy
                "sunny" -> R.drawable.ic_sunny
                "rainy" -> R.drawable.ic_rainy
                else -> R.drawable.ic_snowy
            }
        }

        fun getTimeDescription(time: Double): String{
            return when(time){
                0.0 -> "Midnight"
                4.0 -> "Early morning"
                8.0 -> "Morning"
                12.0 -> "Noon"
                16.0 -> "Afternoon"
                else -> "Evening"
            }
        }

        fun isMetric(context: Context): Boolean{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val metricUnit = context.getString(R.string.pref_units_metric)
            val keyForUnits = context.getString(R.string.key_temperature_units)
            val preferredUnits = preferences.getString(keyForUnits, metricUnit)

            return metricUnit == preferredUnits
        }

        fun celsiusToFahrenheit(celsiusTemp: Double): Double{
            return celsiusTemp * 1.8 + 32
        }
    }



}