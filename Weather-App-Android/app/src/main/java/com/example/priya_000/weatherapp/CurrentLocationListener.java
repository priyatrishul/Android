package com.example.priya_000.weatherapp;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public final class CurrentLocationListener implements LocationListener {

    private final Context mContext;

    public boolean isGPSEnabled = false;
    public boolean isNetwork = false;
    public boolean isEnabled = false;

    Location location;
    double latitude;
    double longitude;



    protected LocationManager locationManager;

    public CurrentLocationListener(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled  && !isNetwork ) {

            }else {
                isEnabled=true;
                if (isNetwork) {
                    location=null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                else if (isGPSEnabled) {
                    location = null;

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }



    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(CurrentLocationListener.this);
        }
    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }



    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}