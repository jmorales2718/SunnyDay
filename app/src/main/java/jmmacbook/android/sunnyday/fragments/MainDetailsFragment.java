package jmmacbook.android.sunnyday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jmmacbook.android.sunnyday.DetailsActivity;
import jmmacbook.android.sunnyday.R;
import jmmacbook.android.sunnyday.data.weather_data.CurrentWeatherData;
import jmmacbook.android.sunnyday.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class MainDetailsFragment extends Fragment {
    public static final String MY_APP_ID = "myAppID";
    public static final String CITY_NAME = "cityName";

    private TextView tvTemperature;
    private TextView tvDescription;
    private ImageView ivIcon;

    private String currentCityName;
    private String myAppId;
    private Retrofit retrofitMain;

    private WeatherAPI weatherAPI;

    public static MainDetailsFragment newInstance(String cityName, String appId) {
        MainDetailsFragment mainDetails = new MainDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        args.putString(MY_APP_ID, appId);
        mainDetails.setArguments(args);
        return mainDetails;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainLayout = inflater.inflate(R.layout.fragment_main_details, container, false);
        Bundle b = this.getArguments();
        currentCityName = b.getString(CITY_NAME);
        myAppId = b.getString(MY_APP_ID);

        tvTemperature = mainLayout.findViewById(R.id.tvTemperature);
        tvDescription = mainLayout.findViewById(R.id.tvDescription);
        ivIcon = mainLayout.findViewById(R.id.ivWeatherIcon);

        retrofitMain = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        weatherAPI = retrofitMain.create(WeatherAPI.class);

        if (currentCityName != null && !currentCityName.isEmpty()) {
            createRetrofitCallback();
        }

        return mainLayout;
    }

    public void createRetrofitCallback() {
        Log.d("CURRENT_CITY", currentCityName);

        Call<CurrentWeatherData> weatherTest = weatherAPI.getCurrentWeatherData(currentCityName, ((DetailsActivity) getActivity()).getUnits(),
                myAppId);
        weatherTest.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                if (response.body().getName().equals(currentCityName)) {
                    tvTemperature.setText(String.valueOf(response.body().getMain().getTemp()) +
                            (char) 176 + ((DetailsActivity) getActivity()).getUnitsSelectionLetter());
                    tvDescription.setText(String.valueOf(response.body().getWeather().get(0).getDescription()));
                    ivIcon.setImageResource(
                            ((DetailsActivity) getActivity()).getIcon(response.body().getWeather().get(0).getIcon()
                            ));
                }

            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                Log.d("FAILED::", "Time to Debug: " + call);
            }
        });
    }
}
