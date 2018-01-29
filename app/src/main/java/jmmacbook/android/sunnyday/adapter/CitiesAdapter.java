package jmmacbook.android.sunnyday.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import jmmacbook.android.sunnyday.CitiesActivity;
import jmmacbook.android.sunnyday.DetailsActivity;
import jmmacbook.android.sunnyday.R;
import jmmacbook.android.sunnyday.data.City;

/**
 * Created by jmmacbook on 1/27/18.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    public static final String CITY_NAME = "CITY_NAME";
    private Context context;
    private List<City> cities = new ArrayList<>();

    public CitiesAdapter(Context context) {
        this.context = context;
        cities = City.listAll(City.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.btnCity.setText(cities.get(holder.getAdapterPosition()).getCityName());

        holder.btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityClicked(holder);
            }
        });

        holder.btnInvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityClicked(holder);
            }
        });

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityClicked(holder);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCity(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

//    public int getCityPosition(String cityName) {
//        for (int i = 0; i < getItemCount(); i++) {
//            if (cityName.equals(cities.get(i).getCityName())) {
//                return i;
//            }
//        }
//        return -1;
//    }

    public void onCityClicked (ViewHolder holder) {
        Intent detailsIntent = new Intent(context, DetailsActivity.class);
        detailsIntent.putExtra(CITY_NAME, cities.get(holder.getAdapterPosition()).getCityName());
        (context).startActivity(detailsIntent);
    }

    public void addCity(City city) {
        int finalIndex = cities.size();
        cities.add(finalIndex, city);
        city.save();
        notifyItemInserted(finalIndex);
    }

    public void removeCity(int position) {
        cities.get(position).delete();
        cities.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Button btnCity;
        private Button btnInvisible;
        private Button btnDetails;
        private ImageButton btnDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            btnCity = itemView.findViewById(R.id.btnCity);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnInvisible = itemView.findViewById(R.id.btnInvisible);
            btnDelete = itemView.findViewById(R.id.btnDeleteCity);
        }
    }
}
