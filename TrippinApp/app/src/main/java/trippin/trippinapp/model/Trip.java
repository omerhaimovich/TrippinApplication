package trippin.trippinapp.model;


import com.google.gson.JsonObject;

import android.icu.text.SimpleDateFormat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Attr;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Trip implements Serializable {
    String m_ID;
    String m_googleID;
    String m_name;
    Date m_startDate;
    Date m_endDate;
    Date m_createdAt;
    Date m_updatedAt;
    ArrayList<Attraction> m_attractions;

    public static Trip fromJSON(JsonObject object, boolean load_atrraction) {
        Trip trip = null;


        ArrayList<Attraction> attractions = new ArrayList<Attraction>();

        String id = object.get("Id").getAsString();
        String name = object.get("Country").getAsString();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date startDate = null;

        try {
            startDate = format.parse(object.get("CreationDate").getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDate != null) {

            String end = object.get("EndDate").getAsString();
            Date endDate = null;

            if (end != null) {
                try {
                    endDate = format.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            trip = new Trip(id, name, startDate, endDate);

            if (load_atrraction) {
                JsonArray attrs = object.get("FullAllAttractions").getAsJsonArray();

                for (JsonElement attr : attrs) {
                    attractions.add(Attraction.fromJSON(attr.getAsJsonObject()));
                }

                trip.m_attractions = attractions;
            }
        }

        return trip;
    }

    public Trip(String google_id, String name, Date from_date, Date to_date) {
        this(UUID.randomUUID().toString(), google_id, name, from_date, to_date);
    }

    public String getGoogleID() {
        return m_googleID;
    }

    public String getID() {
        return m_ID;
    }

    public void setID(String m_ID) {
        this.m_ID = m_ID;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public Date getStartDate() {
        return m_startDate;
    }

    public void setStartDate(Date m_startDate) {
        this.m_startDate = m_startDate;
    }

    public void setEndDate(Date m_endDate) {
        this.m_endDate = m_endDate;
    }

    public Date getCreatedAt() {
        return m_createdAt;
    }

    public void setCreatedAt(Date m_createdAt) {
        this.m_createdAt = m_createdAt;
    }

    public Date getUpdatedAt() {
        return m_updatedAt;
    }

    public void setUpdatedAt(Date m_updatedAt) {
        this.m_updatedAt = m_updatedAt;
    }

    public ArrayList<Attraction> getAttractions() {
        return m_attractions;
    }


    public Attraction getCurrentAttraction() {

        if (getAttractions() != null) {
            for (Attraction attr : getAttractions()) {
                if (attr.getEndDate() == null) {
                    return attr;
                }
            }
        }

        return null;
    }

    public Trip(String id, String google_id, String name, Date from_date, Date to_date) {
        this.m_ID = id;
        this.m_googleID = google_id;
        this.m_name = name;
        this.m_startDate = from_date;
        this.m_endDate = to_date;
        this.m_createdAt = new Date();
        this.m_updatedAt = new Date();
        this.m_attractions = new ArrayList<>();
    }

    public Date getEndDate() {
        return m_endDate;
    }
}
