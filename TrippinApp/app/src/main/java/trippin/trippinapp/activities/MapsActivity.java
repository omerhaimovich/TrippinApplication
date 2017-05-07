package trippin.trippinapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import trippin.trippinapp.R;
import trippin.trippinapp.model.Attraction;

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
    }

    public void ShowProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
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

        ArrayList<Attraction> attractions = new ArrayList<>();

        // TODO: 06-May-17 Hila - check this with real attractions from server API 
        for (int i = 0; i < 10; i++) {
            String attractionName = ("Attraction" + i);
            LatLng attractionPos = new LatLng((-34 - i * 2), (151 + i * 5));

            Attraction attraction = new Attraction("gdsfg",
                    attractionName,
                    1,
                    attractionPos, null, null);
            attractions.add(attraction);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(attractionName);
            markerOptions.position(attractionPos);
            markerOptions.icon(attraction.getImage());
            mMap.addMarker(markerOptions);
        }

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(attractions.get(0).getAttractionLocation()));
    }
}
