package trippin.trippinapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import trippin.trippinapp.R;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.Enums.AttractionType;
import trippin.trippinapp.serverAPI.RequestHandler;

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

        // TODO: 13-May-17 Hila - check why doesn't work
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

        String bla = "bareliah@gmail.com";

        ArrayList<AttractionType> selectedAttractionTypes = new ArrayList<AttractionType>();
        for (int i=0 ; i<selected.length;i++) {
            selectedAttractionTypes.add(AttractionType.values()[Integer.parseInt(selected[i])]);
        }

        try {
            RequestHandler.createTrip(bla,32.075842,34.889338,selectedAttractionTypes);
            //RequestHandler.createTrip(currUser.getEmail(),32.075842,34.889338,selectedAttractionTypes);
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

        // TODO: 06-May-17 Hila - check this with real attractions from server API
        String attractionName1 = ("Charing Cross Underground Station");
        String attractionName2 = ("Sherlock Holmes");

        LatLng attractionPos1 = new LatLng(51.507852, -0.127252);
        LatLng attractionPos2 = new LatLng(51.507345, -0.12534);

        Attraction attraction1 = new Attraction("gdsfg",
                attractionName1, 1, attractionPos1, null, null);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.title(attractionName1);
        markerOptions1.position(attractionPos1);
        markerOptions1.icon(attraction1.getImage());
        mMap.addMarker(markerOptions1);

        Attraction attraction2 = new Attraction("gdsfg",
                attractionName2, 1, attractionPos2, null, null);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.title(attractionName2);
        markerOptions2.position(attractionPos2);
        markerOptions2.icon(attraction2.getImage());
        mMap.addMarker(markerOptions2);

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(attractionPos1));
    }

    public void btnStartTrippin(View view) {
        String attractionName3 = ("Garfunkel's");
        String attractionName4 = ("MyMindWorks Ltd");
        String attractionName5 = ("Pizza Express");

        LatLng attractionPos3 = new LatLng(51.507196, -0.126829);
        LatLng attractionPos4 = new LatLng(51.508126, -0.127132);
        LatLng attractionPos5 = new LatLng(51.508448, -0.126147);

        Attraction attraction3 = new Attraction("gdsfg",
                attractionName3, 1, attractionPos3, null, null);

        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.title(attractionName3);
        markerOptions3.position(attractionPos3);
        markerOptions3.icon(attraction3.getImage());
        mMap.addMarker(markerOptions3);

        Attraction attraction4 = new Attraction("gdsfg",
                attractionName4, 1, attractionPos4, null, null);

        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.title(attractionName4);
        markerOptions4.position(attractionPos4);
        markerOptions4.icon(attraction4.getImage());
        mMap.addMarker(markerOptions4);


        Attraction attraction5 = new Attraction("gdsfg",
                attractionName5, 1, attractionPos5, null, null);

        MarkerOptions markerOptions5 = new MarkerOptions();
        markerOptions5.title(attractionName5);
        markerOptions5.position(attractionPos5);
        markerOptions5.icon(attraction5.getImage());
        mMap.addMarker(markerOptions5);

    }
}
