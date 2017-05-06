package trippin.trippinapp.model;

import java.util.ArrayList;

/**
 * Created by tacco on 5/6/17.
 */

public class User {
    String m_email;
    ArrayList<Trip> m_trips;
    String m_currentTripID;

    public User(String email) {
        this.m_email = email;
        m_trips = new ArrayList<Trip>();
    }
}
