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
    int m_rate;

    public Attraction(String googleID, String name, int rate) {
        this(UUID.randomUUID().toString(), googleID, name, rate);
    }

    public Attraction(String _id, String m_googleID, String name, int rate) {
        this.m_id = _id;
        this.m_googleID = m_googleID;
        this.m_name = name;
        this.m_rate = rate;
    }
}