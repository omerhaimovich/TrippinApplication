package trippin.trippinapp.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tacco on 5/6/17.
 */

public class Trip {
    String id;
    String google_id;
    String name;
    Date from_date;
    Date to_date;
    Date created_at;
    Date updated_at;

    public Trip(String google_id, String name, Date from_date, Date to_date) {
        this(UUID.randomUUID().toString(), google_id, name, from_date, to_date);
    }

    public Trip(String id, String google_id, String name, Date from_date, Date to_date) {
        this.id = id;
        this.google_id = google_id;
        this.name = name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.created_at = new Date();
        this.updated_at = new Date();
    }
}
