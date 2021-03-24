package com.example.android.weathertask


import com.example.android.weathertask.Data.HourlyTemp
import com.example.android.weathertask.Data.WeatherForecast
import com.example.android.weathertask.Utils.Utils
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.math.roundToInt

class UtilsTest {

    @Test
    fun `celsius to Fahrenheit`(){
        val result = Utils.celsiusToFahrenheit(100.0)

        assertThat(result).isEqualTo(212.0)
    }

    @Test
    fun `get weather icon returns sunny resource`(){
        val weatherForecast = WeatherForecast("", "sunny", List(0)
        { HourlyTemp(0.0, 0.0) })
        val result = Utils.getWeatherIcon(weatherForecast)

        assertThat(result).isEqualTo(R.drawable.ic_sunny)
    }

    @Test
    fun `get weather description capitalized`(){
        val weatherForecast = WeatherForecast("", "snowy", List(0)
        { HourlyTemp(0.0, 0.0) })
        val result = Utils.getWeatherDescription(weatherForecast)

        assertThat(result).isEqualTo("Snowy")
    }

    @Test
    fun `calculate average daily temperature for single city`(){
        val weatherForecast = WeatherForecast("Warsaw", "rainy",
            listOf(HourlyTemp(-2.0, 0.0), HourlyTemp(-2.0, 4.0),
                HourlyTemp(0.5, 8.0), HourlyTemp(2.0, 12.0),
                HourlyTemp(3.0, 16.0), HourlyTemp(-1.0, 20.0)))
        var result = Utils.calculateAverageDailyTemperatureForSingleCity(weatherForecast)
        result =  (result * 1000.0).roundToInt() / 1000.0

        assertThat(result).isEqualTo(0.083)
    }

    @Test
    fun `find lowest temperature for single city`(){
        val weatherForecast = WeatherForecast("Warsaw", "rainy",
            listOf(HourlyTemp(-2.0, 0.0), HourlyTemp(-2.0, 4.0),
                HourlyTemp(0.5, 8.0), HourlyTemp(2.0, 12.0),
                HourlyTemp(3.0, 16.0), HourlyTemp(-1.0, 20.0)))
        val result = Utils.findLowestTempForSingleCity(weatherForecast)

        assertThat(result).isEqualTo(-2.0)
    }

    @Test
    fun `find highest temperature for single city`(){
        val weatherForecast = WeatherForecast("Warsaw", "rainy",
            listOf(HourlyTemp(-2.0, 0.0), HourlyTemp(-2.0, 4.0),
                HourlyTemp(0.5, 8.0), HourlyTemp(2.0, 12.0),
                HourlyTemp(3.0, 16.0), HourlyTemp(-1.0, 20.0)))
        val result = Utils.findHighestTempForSingleCity(weatherForecast)

        assertThat(result).isEqualTo(3.0)
    }

    @Test
    fun `find lowest temperature in all cities`(){
        val warsaw = WeatherForecast("Warsaw", "rainy",
            listOf(HourlyTemp(-2.0, 0.0), HourlyTemp(-2.0, 4.0),
                HourlyTemp(0.5, 8.0), HourlyTemp(2.0, 12.0),
                HourlyTemp(3.0, 16.0), HourlyTemp(-1.0, 20.0)))
        val paris = WeatherForecast("Paris", "cloudy",
            listOf(HourlyTemp(11.0, 0.0), HourlyTemp(14.0, 4.0),
                HourlyTemp(18.0, 8.0), HourlyTemp(22.0, 12.0),
                HourlyTemp(15.0, 16.0), HourlyTemp(13.0, 20.0)))
        val cities = listOf(warsaw, paris).toTypedArray()
        val result = Utils.findLowestTempInAllCities(cities)

        assertThat(result).isEqualTo(-2.0)
    }

    @Test
    fun `find city with lowest average daily temperature`(){
        val warsaw = WeatherForecast("Warsaw", "rainy",
            listOf(HourlyTemp(-2.0, 0.0), HourlyTemp(-2.0, 4.0),
                HourlyTemp(0.5, 8.0), HourlyTemp(2.0, 12.0),
                HourlyTemp(3.0, 16.0), HourlyTemp(-1.0, 20.0)))
        val berlin = WeatherForecast("Berlin", "cloudy",
            listOf(HourlyTemp(-6.0, 0.0), HourlyTemp(-4.0, 4.0),
                HourlyTemp(2.0, 8.0), HourlyTemp(4.0, 12.0),
                HourlyTemp(5.5, 16.0), HourlyTemp(3.0, 20.0)))
        val cities = listOf(warsaw, berlin).toTypedArray()
        val result = Utils.findCityWithLowestAverageDailyTemperature(cities)

        assertThat(result).isEqualTo("Warsaw")
    }
}