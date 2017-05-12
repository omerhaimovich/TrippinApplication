package trippin.trippinapp.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import trippin.trippinapp.adapter.TripsAdapter;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.R;

public class ProfileActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Trip t1 = new Trip(null, "Berlin", new Date(), null);
        t1.getAttractions().add(new Attraction(null, "Star Bucks", 5, null, new Date(), new Date()));
        t1.getAttractions().add(new Attraction(null, "Zara", 4, null, new Date(),new Date()));
        t1.getAttractions().add(new Attraction(null, "Mcdonalds", 4, null, new Date(),new Date()));
        Trip t2 = new Trip(null, "London", new Date("20/12/2015"), new Date("29/12/2015"));
        Trip t3 = new Trip(null, "Paris", new Date("05/08/2016"), new Date("13/08/2016"));
        User user = User.getCurrentUser();
        user.addNewTrip(t1);
        user.addNewTrip(t2);
        user.addNewTrip(t3);
        user.setCurrentTrip(t1);

        TripsAdapter trips_adapter = new TripsAdapter(user.getTrips(), getApplicationContext());
        setListAdapter(trips_adapter);

        ((TextView)findViewById(R.id.txtCurrentTripLocation)).setText(user.getCurrentTrip().getName());
        ((TextView)findViewById(R.id.txtProfileName)).setText(user.getName());
        ((TextView)findViewById(R.id.txtCurrentTripDate)).setText(DateFormat.format("dd/MM/yy", user.getCurrentTrip().getStartDate()).toString());
        ((Button)findViewById(R.id.btnProfileAction)).setText(user.m_currentTrip == null ? "Start Trippin" : "End Trippin");

    }
}
