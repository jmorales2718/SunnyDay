package jmmacbook.android.sunnyday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class ExtraDetailsFragment extends Fragment {
    public static final String MY_API_ID = "myApiID";
    public static final String CITY_NAME = "cityName";

    private TextView tvTemperatureExtra;
    private TextView tvDescriptionExtra;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private ImageView ivIconExtra;

    private String currentCityName;
    private String myAppId;

    private WeatherAPI weatherAPI;


    public static ExtraDetailsFragment newInstance(String cityName, String myAppId) {
        ExtraDetailsFragment extraDetails = new ExtraDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        args.putString(MY_API_ID, myAppId);
        extraDetails.setArguments(args);
        return extraDetails;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_extra_details, container, false);

        Bundle b = this.getArguments();
        currentCityName = b.getString(CITY_NAME);
        myAppId = b.getString(MY_API_ID);

        tvTemperatureExtra = layoutView.findViewById(R.id.tvTemperatureExtra);
        tvDescriptionExtra = layoutView.findViewById(R.id.tvDescriptionExtra);
        tvHumidity = layoutView.findViewById(R.id.tvHumidity);
        tvWindSpeed = layoutView.findViewById(R.id.tvWindSpeed);
        ivIconExtra = layoutView.findViewById(R.id.ivWeatherIconExtra);

        Retrofit retrofitExtraDetails = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        weatherAPI = retrofitExtraDetails.create(WeatherAPI.class);

        if (currentCityName != null && !currentCityName.isEmpty()) {
            createRetrofitCallback();
        }
        return layoutView;
    }

    public void createRetrofitCallback() {
        Call<CurrentWeatherData> weatherTest = weatherAPI.getCurrentWeatherData(currentCityName,
                ((DetailsActivity) getActivity()).getUnits(),
                myAppId);
        weatherTest.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {

                tvTemperatureExtra.setText(String.valueOf(response.body().getMain().getTemp()) +
                        (char) 176 + ((DetailsActivity) getActivity()).getUnitsSelectionLetter());
                tvDescriptionExtra.setText(String.valueOf(response.body().getWeather().get(0).getDescription()));
                tvHumidity.setText("HUMIDITY " + String.valueOf(response.body().getMain().getHumidity()) + '%');
                tvWindSpeed.setText("WIND SPEED " + String.valueOf(response.body().getWind().getSpeed()) + " " + ((DetailsActivity) getActivity()).getWindUnits());
                ivIconExtra.setImageResource(
                        ((DetailsActivity) getActivity()).getIcon(response.body().getWeather().get(0).getIcon()
                        ));
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
