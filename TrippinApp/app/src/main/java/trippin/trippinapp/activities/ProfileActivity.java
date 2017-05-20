package trippin.trippinapp.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;
import java.util.Date;

import trippin.trippinapp.adapter.TripsAdapter;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.R;
import trippin.trippinapp.serverAPI.RequestHandler;

public class ProfileActivity extends ListActivity {

    public void EndTrip(View view) {
        ((Button)findViewById(R.id.btnEndTripProfile)).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.btnStartTripProfile)).setVisibility(View.VISIBLE);
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
            User.getCurrentUser().UpdateTrips(RequestHandler.connectUser(User.getCurrentUser().getEmail(), (double)0, (double)0).getAsJsonObject());

            int t = 12 + 2;
        } catch (IOException e) {
            e.printStackTrace();
        }


        User user = User.getCurrentUser();
//        Trip t1 = new Trip(null, "Berlin", new Date(), null);
//        t1.getAttractions().add(new Attraction(null, "Star Bucks", 5, null, new Date(), null, null));
//        t1.getAttractions().add(new Attraction(null, "Zara", 4, null, new Date(),new Date(), null));
//        t1.getAttractions().add(new Attraction(null, "Mcdonalds", 4, null, new Date(),new Date(), null));
//        Trip t2 = new Trip(null, "London", new Date("20/12/2015"), new Date("29/12/2015"));
//        Trip t3 = new Trip(null, "Paris", new Date("05/08/2016"), new Date("13/08/2016"));
//        User user = User.getCurrentUser();
//        user.addNewTrip(t1);
//        user.addNewTrip(t2);
//        user.addNewTrip(t3);
//        user.setCurrentTrip(t1);

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
