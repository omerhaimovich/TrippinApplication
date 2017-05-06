package trippin.trippinapp.model;

import java.util.ArrayList;

/**
 * Created by tacco on 5/6/17.
 */

public class User {
    String email;
    ArrayList<Trip> trips;
    Trip current_trip;

    public User(String email) {
        this.email = email;
        trips = new ArrayList<Trip>();
    }
}
