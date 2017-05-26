package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by barbie on 24/05/2017.
 */

public class EndAttractionRequest implements IUrlRequest{

    public String TripId;
    public String AttractionId;

    @Override
    public String getURLSuffix() {
        return "attractions/AttractionEnd";
    }
}
