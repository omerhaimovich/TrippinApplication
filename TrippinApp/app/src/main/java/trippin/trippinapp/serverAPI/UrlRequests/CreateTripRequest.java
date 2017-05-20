package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class CreateTripRequest implements IUrlRequest {

    public String UserEmail;
    public Double Lat;
    public Double Lng;

    @Override
    public String getURLSuffix() {
        return "trips/CreateTrip";
    }
}
