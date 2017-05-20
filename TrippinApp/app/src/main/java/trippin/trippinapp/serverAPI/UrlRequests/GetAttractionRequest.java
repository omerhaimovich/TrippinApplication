package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class GetAttractionRequest implements IUrlRequest
{

    public String TripId;

    public Double Lat;

    public Double Lng;

    @Override
    public String getURLSuffix() {
        String url ="attractions/GetAttractions?tripId=";
        url += TripId;
        url += "&lat=";
        url += Lat;
        url += "&lng=";
        url += Lng;
        return url;
    }
}
