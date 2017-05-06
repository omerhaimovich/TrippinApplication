package trippin.trippinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Date;

import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;

public class ProfileActivity extends AppCompatActivity {

    User user = new User("atoma@gmail.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Trip t1 = new Trip(null, "Bla", new Date(), new Date("30/10.2017"))
    }
}
