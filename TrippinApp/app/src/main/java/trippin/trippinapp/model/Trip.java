package trippin.trippinapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Trip {
    String m_ID;
    String m_googleID;
    String m_name;
    Date m_startDate;
    Date m_endDate;
    Date m_createdAt;
    Date m_updatedAt;
    ArrayList<Attraction> attractions;

    public Trip(String google_id, String name, Date from_date, Date to_date) {
        this(UUID.randomUUID().toString(), google_id, name, from_date, to_date);
    }

    public Trip(String id, String google_id, String name, Date from_date, Date to_date) {
        this.m_ID = id;
        this.m_googleID = google_id;
        this.m_name = name;
        this.m_startDate = from_date;
        this.m_endDate = to_date;
        this.m_createdAt = new Date();
        this.m_updatedAt = new Date();
        this.attractions = new ArrayList<>();
    }
}
