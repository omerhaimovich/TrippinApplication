package trippin.trippinapp.model;

import android.icu.text.SimpleDateFormat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Attr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Attraction implements Serializable {
    String m_id;
    String m_googleID;
    String m_name;
    double m_rate;
    Date m_startDate;
    Date m_endDate;
    transient LatLng m_attractionLocation;
    String m_image;

    public Attraction() {

    }

    public Date getStartDate() {
        return m_startDate;
    }

    public void setStartDate(Date m_startDate) {
        this.m_startDate = m_startDate;
    }

    public Date getEndDate() {
        return m_endDate;
    }

    public void setEndDate(Date m_endDate) {
        this.m_endDate = m_endDate;
    }

    public static Attraction fromJSON(JsonObject object) {
        Attraction attraction = null;

        try {
            String id = object.get("Id").getAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String start = object.get("StartDate").getAsString();


            Date startDate = start.equals("0001-01-01T00:00:00") || start.equals("0001-01-01T00:00:00Z") ? null : format.parse(start);

            String end = object.get("EndDate").getAsString();
            double rate = object.get("Rating").getAsFloat();
            double y = object.get("Longitude").getAsDouble();
            double x = object.get("Latitude").getAsDouble();
            String url = object.get("PhotoUrl").getAsString();
            String name = object.get("Name").getAsString();
            Date endDate = null;

            if (end != null) {
                endDate = end.equals("0001-01-01T00:00:00") || end.equals("0001-01-01T00:00:00Z") ? null : format.parse(end);
            }

            attraction = new Attraction(id, name, rate, new LatLng(x, y), startDate, endDate, url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attraction;
    }

    public Attraction(String googleID, String name, double rate, LatLng location, Date startDate,
                      Date endDate, String url) {
        this(UUID.randomUUID().toString(), googleID, name, rate, location, startDate, endDate, url);
    }

    public Attraction(String _id, String m_googleID, String name, double rate, LatLng location,
                      Date startDate, Date endDate, String url) {
        this.m_id = _id;
        this.m_googleID = m_googleID;
        this.m_name = name;
        this.m_rate = rate;
        this.m_attractionLocation = location;
        this.m_startDate = startDate;
        this.m_endDate = endDate;
        this.m_image = url;
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

    public double getRate() {
        return m_rate;
    }

    public String getImage() {
        return m_image;
    }

    public void setImage(String m_image) {
        this.m_image = m_image;
    }

    public void setRate(double m_rate) {
        this.m_rate = m_rate;
    }

    public LatLng getAttractionLocation() {
        return m_attractionLocation;
    }

    public void setAttractionLocation(LatLng m_attractionLocation) {
        this.m_attractionLocation = m_attractionLocation;
    }

    public String getM_googleID() {
        return this.m_googleID;
    }

    public void setM_googleID(String googleID) {
        this.m_googleID = googleID;
    }
}