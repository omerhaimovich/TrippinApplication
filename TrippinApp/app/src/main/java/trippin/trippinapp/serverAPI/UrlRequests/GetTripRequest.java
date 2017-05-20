package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class GetTripRequest implements IUrlRequest{

    public String TripId;
    public String UserEmail;
    public Double Lat;
    public Double Lng;

    @Override
    public String getURLSuffix() {
        String url= "trips/GetTrip?tripId=";
        url += TripId;
        url += "&UserEmail=";
        url += UserEmail;
        url += "&lat=";
        url += Lat.toString();
        url += "&Lng=";
        url += Lng.toString();
        return url;
    }
}
