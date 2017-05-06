package trippin.trippinapp.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Attraction {
    String m_id;
    String m_googleID;
    String m_name;
    int m_rate;
    LatLng m_attractionLocation;
BitmapDescriptor m_image;

    public Attraction(String googleID, String name, int rate, LatLng location) {
        this(UUID.randomUUID().toString(), googleID, name, rate, location);
    }

    public Attraction(String _id, String m_googleID, String name, int rate, LatLng location) {
        this.m_id = _id;
        this.m_googleID = m_googleID;
        this.m_name = name;
        this.m_rate = rate;
        m_attractionLocation = location;
    }

    public String getID() {
        return m_id;
    }

    public void setID(String m_id) {
        this.m_id = m_id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public int getRate() {
        return m_rate;
    }

    public BitmapDescriptor getImage() {
        return m_image;
    }

    public void setImage(BitmapDescriptor m_image) {
        this.m_image = m_image;
    }

    public void setRate(int m_rate) {
        this.m_rate = m_rate;
    }

    public LatLng getAttractionLocation() {
        return m_attractionLocation;
    }

    public void setAttractionLocation(LatLng m_attractionLocation) {
        this.m_attractionLocation = m_attractionLocation;
    }
}