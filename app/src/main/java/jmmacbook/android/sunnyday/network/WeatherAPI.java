package jmmacbook.android.sunnyday.network;

import jmmacbook.android.sunnyday.data.weather_data.CurrentWeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jmmacbook on 1/28/18.
 */

public interface WeatherAPI
{
        @GET("weather")
        Call<CurrentWeatherData> getCurrentWeatherData(@Query("q") String q,
                                                       @Query("units") String units,
                                                       @Query("appid") String appid
        );
}
