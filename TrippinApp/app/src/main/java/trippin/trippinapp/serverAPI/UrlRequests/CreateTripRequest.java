package trippin.trippinapp.serverAPI.UrlRequests;

import java.util.ArrayList;

import trippin.trippinapp.serverAPI.Enums.AttractionType;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class CreateTripRequest implements IUrlRequest {

    public String UserEmail;
    public Double Lat;
    public Double Lng;
    public ArrayList<AttractionType> AttractionTypes;

    @Override
    public String getURLSuffix() {
        return "trips/CreateTrip";
    }
}
