package trippin.trippinapp.serverAPI.UrlRequests;

import java.util.ArrayList;

import trippin.trippinapp.serverAPI.Enums.AttractionType;


public class UpdateTripRequest implements IUrlRequest{

    public String TripId;

    public ArrayList<AttractionType> AttractionTypes;



    @Override
    public String getURLSuffix() {
        return "trips/UpdateTrip";
    }
}
