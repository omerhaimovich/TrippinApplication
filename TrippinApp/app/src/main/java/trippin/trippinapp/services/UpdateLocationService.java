package trippin.trippinapp.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bumptech.glide.RequestManager;

import trippin.trippinapp.helpers.MySQLHelper;
import trippin.trippinapp.serverAPI.RequestHandler;

public class UpdateLocationService extends Service implements LocationListener {

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    // The minimum distance to change Updates in meters
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        updateLocation();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location changedLocation ) {

        if (changedLocation != null) {
            RequestHandler.getInstance().setLocation(changedLocation);

            MySQLHelper mySQLHelper = new MySQLHelper(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        if (provider.compareTo(LocationManager.GPS_PROVIDER) == 0) {
            // getting GPS status
            isGPSEnabled = true;
        }
        else if (provider.compareTo(LocationManager.NETWORK_PROVIDER) == 0) {
            // getting network status
            isNetworkEnabled = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

        if (provider.compareTo(LocationManager.GPS_PROVIDER) == 0) {
            // getting GPS status
            isGPSEnabled = false;
        }
        else if (provider.compareTo(LocationManager.NETWORK_PROVIDER) == 0) {
            // getting network status
            isNetworkEnabled = false;
        }
    }

    private void updateLocation() {

        try {

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            String providerName = null;

            // First get location from Network Provider
            if (isNetworkEnabled) {
                providerName = LocationManager.NETWORK_PROVIDER;
            }

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                providerName = LocationManager.GPS_PROVIDER;
            }

            if (providerName != null) {
                canGetLocation = true;
                checkLocationWithProvider(providerName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLocationWithProvider(String networkProvider) {

        LocationListener locationListener = this;

        locationManager.requestLocationUpdates(networkProvider, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

        if (locationManager != null) {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                RequestHandler.getInstance().setLocation(location);
            }
        }
    }
}
