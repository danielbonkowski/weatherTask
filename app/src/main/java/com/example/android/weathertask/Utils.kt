package com.example.android.weathertask

import android.content.Context
import com.example.android.weathertask.Data.WeatherForecast
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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
            var celciusFormatResourceId = R.string.format_temperature_celsius
            return String.format(context.getString(celciusFormatResourceId), temperature)
        }
    }



}