package trippin.trippinapp.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

import trippin.trippinapp.adapter.TripsAdapter;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.R;

public class ProfileActivity extends ListActivity {

    User user = new User("atoma@gmail.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Trip t1 = new Trip(null, "Bla", new Date(), new Date("20/07/2017"));
        Trip t2 = new Trip(null, "Trip 1", new Date(), new Date("13/06/2017"));
        Trip t3 = new Trip(null, "Trip 1", new Date(), new Date("13/06/2017"));
        user.addNewTrip(t1);
        user.addNewTrip(t2);
        user.addNewTrip(t3);
        user.setCurrentTrip(t1);

        TripsAdapter trips_adapter = new TripsAdapter(user.getTrips(), getApplicationContext());
        setListAdapter(trips_adapter);
    }
}
