package trippin.trippinapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import trippin.trippinapp.services.GetNewAttractionsTask;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        CurrentAttraction.OnCurrentAttractionListener, Like.OnLikelistener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.attraction, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            final String[] Content = marker.getSnippet().split(";");

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.attrNameMarker));
            tvTitle.setText(marker.getTitle());
            TextView rate = ((TextView) myContentsView.findViewById(R.id.txtAttrRateMarker));
            rate.setText(Content[0]);

            TextView type = ((TextView) myContentsView.findViewById(R.id.txtAttrTypeMarker));
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {
            getAttractions();
        }
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
            String[] selected = selections.toArray(new String[]{});

            ArrayList<AttractionType> selectedAttractionTypes = new ArrayList<AttractionType>();
            for (int i = 0; i < selected.length; i++) {
                selectedAttractionTypes.add(AttractionType.values()[Integer.parseInt(selected[i])]);
            }

            try {
                GetNewAttractionsTask.getInstance().run();
                RequestHandler.getInstance().createTrip(currUser.getEmail(), selectedAttractionTypes);
                User.getCurrentUser().updateTrips(RequestHandler.getInstance().connectUser(User.getCurrentUser().getEmail()).getAsJsonObject());
                findViewById(R.id.map_endTrippinBtn).setVisibility(View.VISIBLE);
                getAttractions();

            } catch (IOException e) {
                e.printStackTrace();
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
                if (attraction == null)
                {
                    try {

                        RequestHandler.getInstance().attractionChosen(Content[4], Content[3]);
                        attraction = RequestHandler.getInstance().getTrip(User.getCurrentUser().getCurrentTrip().getGoogleID(), User.getCurrentUser().getEmail(), true).getCurrentAttraction();
                        ca = CurrentAttraction.newInstance(attraction);
                        getSupportFragmentManager().beginTransaction().replace(R.id.CurrentAttractionContainer, ca).commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Must end current attraction to chose new one", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void getAttractions() {

        MySQLHelper mySQLHelper = new MySQLHelper(this);
        ArrayList<Attraction> attractions = mySQLHelper.getAttractions();

        boolean isMapInFocus = false;

        Trip trip = User.getCurrentUser().getCurrentTrip();
        String trip_id = null;
        if (trip != null) {trip_id = trip.getGoogleID();}

        if (attractions != null &&
                attractions.isEmpty() == false) {
            mMap.clear();

            for (Attraction currAttraction : attractions) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currAttraction.getAttractionLocation());
                markerOptions.title(currAttraction.getName());
                markerOptions.snippet(currAttraction.getRate() + ";" + "general" + ";" + currAttraction.getImage() + ";" + currAttraction.getM_googleID() + ";" + trip_id);

                if (currAttraction.getImage() != null) {
                    try {
                        markerOptions.icon(null);//BitmapDescriptorFactory.fromBitmap(getBitmapFromURL(currAttraction.getImage())));
                    } catch (Exception e) {

                    }

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
        attraction = null;
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
        if (attraction == null) {
            ((Button) findViewById(R.id.map_endTrippinBtn)).setVisibility(View.INVISIBLE);
            ((Button) findViewById(R.id.map_startTrippinBtn)).setVisibility(View.VISIBLE);

            Trip currentTrip = User.getCurrentUser().getCurrentTrip();
            if (currentTrip != null) {

                try {
                    RequestHandler.getInstance().endTrip(currentTrip.getGoogleID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Must end current attraction to end trip", Toast.LENGTH_SHORT).show();
        }
    }
}
