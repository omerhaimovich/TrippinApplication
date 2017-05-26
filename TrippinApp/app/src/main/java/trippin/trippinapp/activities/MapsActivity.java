package trippin.trippinapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import trippin.trippinapp.R;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.Enums.AttractionType;
import trippin.trippinapp.serverAPI.RequestHandler;
import trippin.trippinapp.services.UpdateLocationService;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ImageButton button = ((ImageButton) findViewById(R.id.btnProfile));

        RequestManager requestManager = Glide.with(getApplicationContext());

        if (requestManager != null) {

            User currUser = User.getCurrentUser();

            if (currUser != null) {

                String imageURL = currUser.getImageUrl();

                if (imageURL != null &&
                        imageURL.isEmpty() == false) {

                    DrawableTypeRequest<String> imaDrawableTypeRqst = requestManager.load(imageURL);

                    if (imaDrawableTypeRqst != null &&
                            imaDrawableTypeRqst.asBitmap() != null) {
                        imaDrawableTypeRqst.asBitmap().centerCrop().into(new BitmapImageViewTarget(button) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(
                                                getApplicationContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                button.setBackground(circularBitmapDrawable);
                            }
                        });
                    }
                }
            }
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    public void ShowProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void StartTrippin(View view) {

        ((Button)findViewById(R.id.mapLayout_startTrippinBtn)).setVisibility(View.INVISIBLE);
        //int[] chosenAttractionType = getResources().getIntArray(R.array.attraction_types_values);
        User currUser = User.getCurrentUser();


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selections = sharedPref.getStringSet("attraction_types",null);
        String[] selected = selections.toArray(new String[] {});

        //String bla = "bareliah@gmail.com";

        ArrayList<AttractionType> selectedAttractionTypes = new ArrayList<AttractionType>();
        for (int i=0 ; i<selected.length;i++) {
            selectedAttractionTypes.add(AttractionType.values()[Integer.parseInt(selected[i])]);
        }

        try {
            Location location = RequestHandler.getLocation();
            //RequestHandler.createTrip(bla,32.075842,34.889338,selectedAttractionTypes);
            RequestHandler.createTrip(currUser.getEmail(),location.getLatitude(),location.getLongitude(),selectedAttractionTypes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void EditSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int fineLocationPermission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 9);

        }

        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 9);
        }

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Intent intent = new Intent(this, UpdateLocationService.class);
            startService(intent);
        }

        // Add a marker in Sydney and move the camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(attractionPos1));
    }

    public void btnStartTrippin(View view) {
//        mMap.addMarker(markerOptions5);

    }
}
