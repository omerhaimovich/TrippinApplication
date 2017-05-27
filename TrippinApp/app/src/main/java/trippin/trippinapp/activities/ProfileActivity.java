package trippin.trippinapp.activities;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;

import trippin.trippinapp.R;
import trippin.trippinapp.adapter.TripsAdapter;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.RequestHandler;

public class ProfileActivity extends ListActivity {

    public void EndTrip(View view) {
        ((Button)findViewById(R.id.btnEndTripProfile)).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.btnStartTripProfile)).setVisibility(View.VISIBLE);

        Trip currentTrip = User.getCurrentUser().getCurrentTrip();
        if(currentTrip != null){
            try {
                RequestHandler.endTrip(currentTrip.getID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void StartTrip(View view) {
        ((Button)findViewById(R.id.btnEndTripProfile)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.btnStartTripProfile)).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Location location = RequestHandler.getLocation();
            User.getCurrentUser().updateTrips(RequestHandler.connectUser(User.getCurrentUser().getEmail(), location.getLatitude(), location.getLongitude()).getAsJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = User.getCurrentUser();

        final ImageView image = ((ImageView)findViewById(R.id.profile_image));

        Glide.with(getApplicationContext()).load(User.getCurrentUser()
                .getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                image.setBackground(circularBitmapDrawable);
            }
        });
        TripsAdapter trips_adapter = new TripsAdapter(user.getTrips(), getApplicationContext());
        setListAdapter(trips_adapter);

        //((TextView)findViewById(R.id.txtCurrentTripLocation)).setText(user.getCurrentTrip().getName());
        ((TextView)findViewById(R.id.txtProfileName)).setText(user.getName());
        ((TextView)findViewById(R.id.txtEmail)).setText(user.getEmail());
        //((TextView)findViewById(R.id.txtCurrentTripDate)).setText(DateFormat.format("dd/MM/yy", user.getCurrentTrip().getStartDate()).toString());
        if (user.m_currentTrip == null)
        {
            ((Button)findViewById(R.id.btnEndTripProfile)).setVisibility(View.INVISIBLE);
        }
        else
        {
            ((Button)findViewById(R.id.btnStartTripProfile)).setVisibility(View.INVISIBLE);
        }
    }
}
