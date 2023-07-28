package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView citynameTV, temperatureTV, conditionTV;
    private TextInputEditText city;
    private ImageView backIV, iconIV, searchIcon;
    private RecyclerView weatherRV;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private Adapter adapter;
    private LocationManager locationManager;
    private static final int PERMISSION_CODE = 1;
    private String cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idLoading);
        citynameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        city = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idTVIcon);
        searchIcon = findViewById(R.id.idIVSearch);
        weatherRV = findViewById(R.id.idRVWeather);

        weatherModelArrayList = new ArrayList<>();
        adapter = new Adapter(this, weatherModelArrayList);
        weatherRV.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                cityname = getCityName(latitude, longitude);
                if (cityname.equals("Not found")) {
                    Toast.makeText(this, "User city not found", Toast.LENGTH_SHORT).show();
                } else {
                    city.setText(cityname);
                    getWeatherInfo(latitude, longitude);
                }
            }
        }

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = city.getText().toString();
                if (cityName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                } else {
                    citynameTV.setText(cityName);
                    // Replace 10 and 20 with the actual latitude and longitude values for the entered city
                    getWeatherInfo(10, 20);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide the Permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double latitude, double longitude) {
        String cityName = "Not found";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                cityName = address.getLocality();
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error getting city name", e);
        }
        return cityName;
    }

    private void getWeatherInfo(double latitude, double longitude) {
        // Call your API or perform weather information retrieval using latitude and longitude
        String apiKey = "http://api.weatherapi.com/v1/forecast.json?key=357b4ce688534354a30103647232402 &q=Dhaka&days=1&aqi=no&alerts=no"; // Replace this with your actual API key
        String url = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + latitude + "," + longitude + "&days=1&aqi=no&alerts=no";
        // Rest of your code to fetch weather information

        // Example usage of Picasso library to load an image into the iconIV ImageView
        Picasso.get()
                .load("https://cdn.weatherapi.com/weather/64x64/day/119.png")  // Replace with the actual URL of the weather icon
                .into(iconIV);
    }
}
