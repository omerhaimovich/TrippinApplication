package trippin.trippinapp.activities;

import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import trippin.trippinapp.R;
import trippin.trippinapp.adapter.AttractionAdapter;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.RequestHandler;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Trip current = null;
        try {
            Location location = RequestHandler.getLocation();
            current = Trip.fromJSON(RequestHandler.getTrip(getIntent().getStringExtra("trip_id"),
                    User.getCurrentUser().getEmail(),
                    location.getLatitude(),
                    location.getLongitude()).getAsJsonObject(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dates = DateFormat.format("dd/MM/yy", current.getStartDate()).toString();
        if (current.getEndDate() != null) {
            dates += " - " + DateFormat.format("dd/MM/yy", current.getEndDate()).toString();
        }

        ((TextView) findViewById(R.id.txtTripTitle)).setText(current.getName());
        ((TextView) findViewById(R.id.txtTripDates)).setText(dates);

        AttractionAdapter adapter = new AttractionAdapter(current.getAttractions(), getApplicationContext());
        ((ListView) findViewById(R.id.lstAttraction)).setAdapter(adapter);

        ((Button) findViewById(R.id.btnTripEnd)).setVisibility(current.getEndDate() == null ? View.VISIBLE : View.GONE);
    }
}
