package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class AttractionRatedRequest  implements IUrlRequest{

    public String TripId;
    public String AttractionId;
    public Boolean IsGoodAttraction;

    @Override
    public String getURLSuffix() {
        return "attractions/AttractionRated";
    }

}
