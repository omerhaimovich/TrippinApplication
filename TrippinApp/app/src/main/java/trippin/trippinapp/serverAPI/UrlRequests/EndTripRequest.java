package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by barbie on 24/05/2017.
 */

public class EndTripRequest implements IUrlRequest
{
    public String TripId;

    @Override
    public String getURLSuffix() {
        return "trips/endTrip";
    }
}