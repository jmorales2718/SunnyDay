package jmmacbook.android.sunnyday.data;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by jmmacbook on 4/25/16.
 */
public class City
        extends SugarRecord
        implements Serializable {
    private String cityName;

    // Needed for SugarORM, don't delete
    public City() {
    }

    public City(String name) {
        cityName = name;
    }

    public String getCityName() {
        return cityName;
    }
}