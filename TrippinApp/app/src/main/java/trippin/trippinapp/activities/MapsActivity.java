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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.messages.internal.Update;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import trippin.trippinapp.R;
import trippin.trippinapp.fragment.CurrentAttraction;
import trippin.trippinapp.fragment.Like;
import trippin.trippinapp.helpers.MySQLHelper;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.Enums.AttractionType;
import trippin.trippinapp.serverAPI.RequestHandler;
import trippin.trippinapp.services.UpdateLocationService;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        CurrentAttraction.OnCurrentAttractionListener, Like.OnLikelistener {

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

            MyInfoWindowAdapter(){
                myContentsView = getLayoutInflater().inflate(R.layout.attraction, null);
            }

            @Override
            public View getInfoContents(Marker marker) {

                final String[] Content = marker.getSnippet().split(";");

                TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.attrNameMarker));
                tvTitle.setText(marker.getTitle());
                TextView rate = ((TextView)myContentsView.findViewById(R.id.txtAttrRateMarker));
                rate.setText(Content[0]);

                TextView type = ((TextView)myContentsView.findViewById(R.id.txtAttrTypeMarker));
                type.setText(Content[1]);
                final ImageView image = ((ImageView) myContentsView.findViewById(R.id.attrImageMarker));

//                myContentsView.findViewById(R.id.startAttractionBtn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            RequestHandler.getInstance().attractionChosen(Content[3], Content[4]);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

                Glide.with(getApplicationContext()).load(Content[2]).asBitmap().centerCrop().into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        image.setBackground(circularBitmapDrawable);
                    }
                });

                return myContentsView;
            }

            @Override
            public View getInfoWindow(Marker marker) {
                // TODO Auto-generated method stub
                return null;
            }
    }

    private GoogleMap mMap;

    CurrentAttraction ca;
    Attraction attraction;
    DialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Trip trip = User.getCurrentUser().getCurrentTrip();

        if (trip == null) {
            findViewById(R.id.map_endTrippinBtn).setVisibility(View.GONE);
        } else {
            findViewById(R.id.map_startTrippinBtn).setVisibility(View.GONE);

            ca = null;
            attraction = null;
            try {

                Trip attractionsTrip = RequestHandler.getInstance().getTrip(trip.getGoogleID(),
                        User.getCurrentUser().getEmail(), true);

                if (attractionsTrip != null &&
                        attractionsTrip.getCurrentAttraction() != null) {

                    attraction = attractionsTrip.getCurrentAttraction();
                }

                if (attraction != null) {
                    ca = CurrentAttraction.newInstance(attraction);
                    getSupportFragmentManager().beginTransaction().replace(R.id.CurrentAttractionContainer, ca).commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {getAttractions(); }
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void startTrippin(View view) {

        (findViewById(R.id.map_startTrippinBtn)).setVisibility(View.INVISIBLE);

        User currUser = User.getCurrentUser();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPref != null) {
            Set<String> selections = sharedPref.getStringSet("attraction_types", null);

            if (selections != null) {

                String[] selected = selections.toArray(new String[]{});

                ArrayList<AttractionType> selectedAttractionTypes = new ArrayList<AttractionType>();
                for (int i = 0; i < selected.length; i++) {
                    selectedAttractionTypes.add(AttractionType.values()[Integer.parseInt(selected[i])]);
                }

                try {
                    //RequestHandler.createTrip(bla,32.075842,34.889338,selectedAttractionTypes);
                    RequestHandler.getInstance().createTrip(currUser.getEmail(), selectedAttractionTypes);
                    User.getCurrentUser().updateTrips(RequestHandler.getInstance().connectUser(User.getCurrentUser().getEmail()).getAsJsonObject());

                    getAttractions();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void editSettings(View view) {
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
        getAttractions();
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String Content[] = marker.getSnippet().split(";");
                try {
                    RequestHandler.getInstance().attractionChosen(Content[4], Content[3]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getAttractions() {

        MySQLHelper mySQLHelper = new MySQLHelper(this);
        ArrayList<Attraction> attractions = mySQLHelper.getAttractions();

        boolean isMapInFocus = false;

        if (attractions != null &&
                attractions.isEmpty() == false) {
            mMap.clear();

            for (Attraction currAttraction : attractions) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currAttraction.getAttractionLocation());
                markerOptions.title(currAttraction.getName());
                markerOptions.snippet(currAttraction.getRate() + ";" + "general" + ";" + currAttraction.getImage() + ";" + currAttraction.getM_googleID() + ";" + User.getCurrentUser().getCurrentTrip().getGoogleID());

                if (currAttraction.getImage() != null)
                {
                    markerOptions.icon(BitmapDescriptorFactory.fromPath(currAttraction.getImage()));
                }

                mMap.addMarker(markerOptions);

                if (isMapInFocus == false) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currAttraction.getAttractionLocation()));

                    isMapInFocus = true;
                }
            }
        }

    }

    @Override
    public void closeAttraction() {
        Like like = Like.newInstance(attraction.getM_googleID());
        like.show(getSupportFragmentManager(), "dialog");
        dialog = like;
        //
    }

    @Override
    public void close() {
        getSupportFragmentManager().beginTransaction().remove(ca).commit();
        dialog.dismiss();
    }

    public void find_me(View view) {
        //connect user to the server
        Location location = RequestHandler.getInstance().getLocation();
        double latitude = 32.1812643;
        double longitude = 34.9781035;

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
    }

    public void btnEndTrippin(View view) {
        ((Button) findViewById(R.id.map_endTrippinBtn)).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.map_startTrippinBtn)).setVisibility(View.VISIBLE);

        Trip currentTrip = User.getCurrentUser().getCurrentTrip();
        if (currentTrip != null) {
            try {
                RequestHandler.getInstance().endTrip(currentTrip.getID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
