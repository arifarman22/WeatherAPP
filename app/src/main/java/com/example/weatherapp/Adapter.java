package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherModel> weatherModelArrayList;

    public Adapter(Context context, ArrayList<WeatherModel> weatherModelArrayList) {
        this.context = context;
        this.weatherModelArrayList = weatherModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item view layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_rv_dem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        WeatherModel weatherModel = weatherModelArrayList.get(position);

        holder.windTV.setText(weatherModel.getWindSpeed() + " Kmph");
        holder.temperatureTV.setText(weatherModel.getTemperature() + " Celsius");
        holder.timeTV.setText(weatherModel.getTime());
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try {
            Date t = input.parse(weatherModel.getTime());
            if (t != null) {
                holder.timeTV.setText(output.format(t));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Load image using Picasso
        Picasso.get()
                .load("https:" + weatherModel.getIcon())
                .placeholder(R.drawable.card_back) // Placeholder image while loading
                .error(R.drawable.card_back) // Image to display if loading fails
                .into(holder.conditionTV);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the data set
        return weatherModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV, temperatureTV, timeTV;
        private ImageView conditionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views within the ViewHolder
            windTV = itemView.findViewById(R.id.idTVWindSpeed);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionTV = itemView.findViewById(R.id.idTVCondition);
        }
    }
}
