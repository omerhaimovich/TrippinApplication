package trippin.trippinapp.model;

import java.util.ArrayList;

/**
 * Created by tacco on 5/6/17.
 */

public class User {
    String m_email;
    public ArrayList<Trip> m_trips;
    public Trip m_currentTrip;

    public User(String email) {
        this.m_email = email;
        m_trips = new ArrayList<Trip>();
    }

    public ArrayList<Trip> getTrips() {
        return m_trips;
    }

    public Trip getCurrentTrip() {
        return m_currentTrip;
    }

    public void setCurrentTrip(Trip m_currentTrip) {
        this.m_currentTrip = m_currentTrip;
    }

    public void AddTrip(Trip trip) {
        m_trips.add(trip);
    }
}
