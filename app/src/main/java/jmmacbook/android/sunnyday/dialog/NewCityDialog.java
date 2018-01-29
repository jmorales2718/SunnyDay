package jmmacbook.android.sunnyday.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import jmmacbook.android.sunnyday.CitiesActivity;
import jmmacbook.android.sunnyday.R;
import jmmacbook.android.sunnyday.data.weather_data.CurrentWeatherData;
import jmmacbook.android.sunnyday.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jmmacbook on 1/27/18.
 */

public class NewCityDialog extends DialogFragment {

    public static final String MY_APP_ID = "2871c4820d07059cf27d2b2f55511e2d";
    public static final String CITY_NAME_ID = "CITY_NAME_ID";

    private boolean invalidCityName = false;

    public NewCityDialog() {

    }

    public static NewCityDialog newInstance() {
        NewCityDialog newCityFragment = new NewCityDialog();
        return newCityFragment;
    }

    private OnNewCityCreatedListener listener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnNewCityCreatedListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + "Must implement OnNewCityCreatedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog((CitiesActivity) getActivity());
        dialog.setContentView(R.layout.new_city_fragment);

        final EditText etNewCityDialog = dialog.findViewById(R.id.etNewCityName);
        Button btnCancelNewCityDialog = dialog.findViewById(R.id.btnCancelNewCityDialog);
        btnCancelNewCityDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        Button btnAddNewCityDialog = dialog.findViewById(R.id.btnAddNewCityDialog);
        btnAddNewCityDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfCityExists(etNewCityDialog.getText().toString())) {
                    listener.onNewCityCreated(etNewCityDialog.getText().toString());
                    dialog.dismiss();
                }
                else {
                    Toast.makeText((CitiesActivity) getActivity(),
                            "Entered city does not exist",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        return dialog;
    }

    private boolean checkIfCityExists(String cityName) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        final Call<CurrentWeatherData> weatherTest = weatherAPI.getCurrentWeatherData(cityName, "Imperial",
                MY_APP_ID);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<CurrentWeatherData> response = weatherTest.execute();
                    if (response.isSuccessful()) {
                        invalidCityName = false;
                    }
                    else {
                        invalidCityName = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    invalidCityName = true;
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            invalidCityName = true;
        }

        return !invalidCityName;
    }

    public interface OnNewCityCreatedListener {
        void onNewCityCreated(String cityName);
    }

}
