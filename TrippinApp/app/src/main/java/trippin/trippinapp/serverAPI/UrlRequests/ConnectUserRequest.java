package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/4/2017.
 */
public class ConnectUserRequest implements IUrlRequest {

    public String Email;
    public Double Lat;
    public Double Lng;

    @Override
    public String getURLSuffix() {
        return "users/ConnectUser";
    }
}
