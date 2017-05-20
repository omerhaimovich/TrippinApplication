package trippin.trippinapp.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Attraction implements Serializable {
    String m_id;
    String m_name;
    int m_rate;
    Date m_startDate;
    Date m_endDate;
    LatLng m_attractionLocation;
    BitmapDescriptor m_image;

    public Attraction(){

    }

    public static Attraction FromJSON(JsonObject object)
    {
        Attraction att = null;

        try
        {
            String id = object.get("ID").getAsString();
            int rate = object.get("Rating").getAsInt();
            String name = object.get("Name").getAsString();
            long x = object.get("Longitude").getAsLong();
            long y = object.get("Latitude").getAsLong();
            String image = object.get("PhotoUrl").getAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date startDate = format.parse(object.get("CreationDate").getAsString());
            Date endDate = format.parse(object.get("EndDate").getAsString());

            att = new Attraction(id, name, rate, new LatLng(x,y),  startDate, endDate, image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return att;
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



    public Attraction(String name, int rate, LatLng location, Date startDate, Date endDate, String url) {
        this(UUID.randomUUID().toString(), name, rate, location, startDate, endDate, url);
    }

    public Attraction(String _id, String name, int rate, LatLng location, Date startDate, Date endDate, String Url) {
        this.m_id = _id;
        this.m_name = name;
        this.m_rate = rate;
        this.m_attractionLocation = location;
        this.m_startDate = startDate;
        this.m_endDate = endDate;
        //this.m_image = new BitmapDescriptor(Url);
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