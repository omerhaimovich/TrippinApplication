package trippin.trippinapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import trippin.trippinapp.R;
import trippin.trippinapp.adapter.AttractionAdapter;
import trippin.trippinapp.adapter.TripsAdapter;
import trippin.trippinapp.model.Trip;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Trip current = (Trip)getIntent().getSerializableExtra("trip");
        String dates = DateFormat.format("dd/MM/yy", current.getStartDate()).toString();
        if (current.getEndDate() != null) {
            dates += " - " + DateFormat.format("dd/MM/yy", current.getEndDate()).toString();
        }

        ((TextView)findViewById(R.id.txtTripTitle)).setText(current.getName());
        ((TextView)findViewById(R.id.txtTripDates)).setText(dates);

        AttractionAdapter adapter = new AttractionAdapter(current.getAttractions(), getApplicationContext());
        ((ListView)findViewById(R.id.lstAttraction)).setAdapter(adapter);

        ((Button)findViewById(R.id.btnTripEnd)).setVisibility(current.getEndDate() == null ? View.VISIBLE : View.GONE);
    }
}
