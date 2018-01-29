package jmmacbook.android.sunnyday;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import jmmacbook.android.sunnyday.fragments.ExtraDetailsFragment;
import jmmacbook.android.sunnyday.fragments.MainDetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    DetailsPagerAdapter detailsPagerAdapter;
    ViewPager viewPager;
    Toolbar toolbarDetails;

    String cityName;
    boolean imperial;
    String unitsSelectionLetter;
    String windUnits;

    public static final String MY_APP_ID = "2871c4820d07059cf27d2b2f55511e2d";
    public static final String UNITS_ID = "UNITS_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //initialize as imperial
        imperial = true;
        unitsSelectionLetter = "F";
        windUnits = "MPH";

        toolbarDetails = (Toolbar) findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);

        detailsPagerAdapter =
                new DetailsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.vPager);
        viewPager.setAdapter(detailsPagerAdapter);

        PagerSlidingTabStrip tabStrip = findViewById(R.id.pagerSlidingTabs);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(Color.WHITE);

        cityName = getIntent().getStringExtra("CITY_NAME");
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(cityName);
    }

    public class DetailsPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public DetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment newFragment = null;
            switch (position) {
                case 0:
                    newFragment = MainDetailsFragment.newInstance(cityName, MY_APP_ID);
                    registeredFragments.put(0, newFragment);
                    break;
                case 1:
                    newFragment = ExtraDetailsFragment.newInstance(cityName, MY_APP_ID);
                    registeredFragments.put(1, newFragment);
                    break;
            }
            return newFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General";
                case 1:
                    return "Detailed";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.unitsToggle) {
            if (imperial) {
                item.setTitle("Metric");
                unitsSelectionLetter = "C";
                windUnits = "MPS";
                imperial = false;
                refresh();

            }
            else {
                item.setTitle("Imperial");
                unitsSelectionLetter = "F";
                windUnits = "MPH";
                imperial = true;
                refresh();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Refreshes fragments if units are changed with a new retrofit call
    public void refresh() {
        for (int i = 0; i < detailsPagerAdapter.getCount(); i++) {
            switch (i) {
                case 0:
                    MainDetailsFragment fragMain = (MainDetailsFragment) (detailsPagerAdapter.getRegisteredFragment(i));
                    fragMain.createRetrofitCallback();
                    break;
                case 1:
                    ExtraDetailsFragment fragDets = (ExtraDetailsFragment) (detailsPagerAdapter.getRegisteredFragment(i));
                    fragDets.createRetrofitCallback();
                    break;
            }
        }
    }

    public int getIcon(String iconNumber) {
        switch (iconNumber) {
            case "01d":
            case "01n":
                return R.drawable.sunny_realistic;
            case "02d":
            case "02n":
                return R.drawable.light_clouds_realistic;
            case "03d":
            case "03n":
                return R.drawable.partly_cloudy_realistic;
            case "04d":
            case "04n":
                return R.drawable.cloudy_realistic;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
                return R.drawable.heavy_rain_realistic;
            case "11d":
            case "11n":
                return R.drawable.thunderstorm_realistic;
            case "13d":
            case "13n":
                return R.drawable.snow_realistic;
            case "50d":
            case "50n":
                return R.drawable.light_rain_realistic;
        }
        return -1;
    }

    public String getUnitsSelectionLetter() {
        return unitsSelectionLetter;
    }

    public String getWindUnits() {
        return windUnits;
    }

    public String getUnits() {
        if (imperial) {
            return "Imperial";
        }
        else {
            return "Metric";
        }
    }
}
