package trippin.trippinapp.model;

import java.util.ArrayList;

/**
 * Created by tacco on 5/6/17.
 */

public class User {
    public String getImageUrl() {
        return m_imageUrl;
    }

    private static final int ITEM_NOT_EXIST = -1;
    String m_imageUrl;
    String m_name;
    static User m_currentUser;


    public static User getCurrentUser() {

        return m_currentUser;
    }

    public static void SignIn(String email, String username, String image) {
        User.m_currentUser = new User(email, username, image);
    }

    public String getName() {
        return m_name;
    }

    String m_email;
    public ArrayList<Trip> m_trips;
    public Trip m_currentTrip;

    public User(String email) {
        this.m_email = email;
        m_trips = new ArrayList<Trip>();
    }

    public User(String email, String name, String image) {
        this.m_email = email;
        this.m_name = name;
        this.m_imageUrl = image;
        m_trips = new ArrayList<Trip>();
    }

    public ArrayList<Trip> getUserTrips() {
        return m_trips;
    }

    public void addNewTrip(Trip newTrip) {

        boolean isTripNew = true;

        for (Trip currTrip : getUserTrips()) {
            if (currTrip.getID().compareTo(newTrip.getID()) == 0) {
                isTripNew = false;
            }
        }

        if (isTripNew) {
            m_trips.add(newTrip);
        }
    }

    public void updateTrip(Trip newTrip) {

        for (Trip currTrip : getUserTrips()) {
            if (currTrip.getID().compareTo(newTrip.getID()) == 0) {

                currTrip.setStartDate(newTrip.getStartDate());
                currTrip.setEndDate(newTrip.getEndDate());
                currTrip.setName(newTrip.getName());
                currTrip.setCreatedAt(newTrip.getCreatedAt());

                break;
            }
        }
    }

    public void removeTrip(Trip tripToRemove) {

        int tripToDeleteIndex = ITEM_NOT_EXIST;

        for (int i = 0; i < getUserTrips().size(); i++) {
            Trip currTrip = getUserTrips().get(i);

            if (currTrip.getID().compareTo(tripToRemove.getID()) == 0) {
                tripToDeleteIndex = i;
                break;
            }
        }

        if (tripToDeleteIndex != ITEM_NOT_EXIST) {
            getUserTrips().remove(tripToDeleteIndex);
        }
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
}
