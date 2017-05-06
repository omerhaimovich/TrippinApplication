package trippin.trippinapp.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Attraction {
    String id;
    String google_id;
    String name;
    Date from_time;
    Date to_time;
    int rate;
    Date created_at;
    Date updaed_at;

    public Attraction(String google_id, String name, Date from_time, Date to_time, int rate) {
        this(UUID.randomUUID().toString(), google_id, name, from_time, to_time, rate);
    }

    public Attraction(String _id, String google_id, String name, Date from_time, Date to_time, int rate) {
        this.id = _id;
        this.google_id = google_id;
        this.name = name;
        this.from_time = from_time;
        this.to_time = to_time;
        this.rate = rate;
        this.created_at = new Date();
        this.updaed_at = new Date();
    }
}