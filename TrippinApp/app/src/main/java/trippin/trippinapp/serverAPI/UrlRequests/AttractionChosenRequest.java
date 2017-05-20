package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class AttractionChosenRequest implements IUrlRequest {

    public String TripId;
    public String AttractionId;

    @Override
    public String getURLSuffix() {
        return "attractions/AttractionChosen";
    }
}
