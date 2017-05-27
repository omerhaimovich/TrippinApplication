package trippin.trippinapp.serverAPI.UrlRequests;

/**
 * Created by Omer Haimovich on 5/5/2017.
 */
public class UpdateUserRequest implements IUrlRequest {

    public String Email;

    public Boolean NotificationsOn;
    public int Radius;

    @Override
    public String getURLSuffix() {
        return "users/UpdateUser";
    }
}
