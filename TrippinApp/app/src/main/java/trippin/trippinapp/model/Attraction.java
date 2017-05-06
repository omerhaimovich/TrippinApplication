package trippin.trippinapp.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Attraction {
    String m_id;
    String m_googleID;
    String m_name;
    Date m_startTime;
    Date m_tripEndTime;
    int m_rate;
    Date m_createdAt;
    Date m_updaedAt;
    Trip m_Trip;

    public Attraction(String googleID, String name, Date from_time,
                      Date to_time, int rate, Trip trip) {
        this(UUID.randomUUID().toString(), googleID, name, from_time, to_time, rate, trip);
    }

    public Attraction(String _id, String m_googleID, String name,
                      Date from_time, Date to_time, int rate, Trip trip) {
        this.m_id = _id;
        this.m_googleID = m_googleID;
        this.m_name = name;
        this.m_startTime = from_time;
        this.m_tripEndTime = to_time;
        this.m_rate = rate;
        this.m_createdAt = new Date();
        this.m_updaedAt = new Date();
        this.m_Trip = trip;
    }
}