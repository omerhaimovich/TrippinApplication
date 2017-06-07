package trippin.trippinapp.serverAPI;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import trippin.trippinapp.common.Consts;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.serverAPI.Enums.AttractionType;
import trippin.trippinapp.serverAPI.UrlRequests.AttractionChosenRequest;
import trippin.trippinapp.serverAPI.UrlRequests.AttractionRatedRequest;
import trippin.trippinapp.serverAPI.UrlRequests.ConnectUserRequest;
import trippin.trippinapp.serverAPI.UrlRequests.CreateTripRequest;
import trippin.trippinapp.serverAPI.UrlRequests.EndAttractionRequest;
import trippin.trippinapp.serverAPI.UrlRequests.EndTripRequest;
import trippin.trippinapp.serverAPI.UrlRequests.GetAttractionRequest;
import trippin.trippinapp.serverAPI.UrlRequests.GetTripRequest;
import trippin.trippinapp.serverAPI.UrlRequests.IUrlRequest;
import trippin.trippinapp.serverAPI.UrlRequests.UpdateTripRequest;
import trippin.trippinapp.serverAPI.UrlRequests.UpdateUserRequest;

/**
 * Created by barbie on 20/05/2017.
 */

public class RequestHandler {

    private static RequestHandler ourInstance = null;
    private Location location;
    private Gson objGson = new Gson();

    private RequestHandler() {
    }

    public static RequestHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new RequestHandler();
        }

        return ourInstance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public JsonElement doPostRequest(IUrlRequest urlRequest) throws IOException {
        String strUrlPrefix = "http://db.cs.colman.ac.il/trippin/api/";
        strUrlPrefix += urlRequest.getURLSuffix();

        URL objURL = new URL(strUrlPrefix);
        HttpURLConnection objConnection = (HttpURLConnection) objURL.openConnection();

        objConnection.setRequestProperty(Consts.CONTENT_TYPE, "application/json;charset=utf-8");
        objConnection.setRequestProperty("Accept-Charset", Consts.UTF_8);
        objConnection.setRequestMethod("POST");
        objConnection.setDoInput(true);
        objConnection.setDoOutput(true);

        OutputStream os = objConnection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, Consts.UTF_8));
        writer.write(objGson.toJson(urlRequest));
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(objConnection.getInputStream(), Consts.UTF_8));

        String s = reader.readLine();
        String strResult = "";
        while (s != null) {
            strResult += s;
            s = reader.readLine();
        }

        return objGson.fromJson(strResult, JsonElement.class);
    }

    public JsonElement doGetRequest(IUrlRequest urlRequest) throws IOException {
        ArrayList<JsonObject> retVal = new ArrayList<>();

        String strUrlPrefix = "http://db.cs.colman.ac.il/trippin/api/";
        strUrlPrefix += urlRequest.getURLSuffix();

        URL objURL = new URL(strUrlPrefix);
        HttpURLConnection objConnection = (HttpURLConnection) objURL.openConnection();

        objConnection.setRequestProperty(Consts.CONTENT_TYPE, "application/json;charset=utf-8");
        objConnection.setRequestProperty("Accept-Charset", Consts.UTF_8);

        BufferedReader reader = new BufferedReader(new InputStreamReader(objConnection.getInputStream(), Consts.UTF_8));

        String s = reader.readLine();
        String strResult = "";
        while (s != null) {
            strResult += s;
            s = reader.readLine();
        }

        return objGson.fromJson(strResult, JsonElement.class);
    }

    public JsonElement connectUser(String p_strEmail) throws IOException {

        ConnectUserRequest objConnectUserRequest = new ConnectUserRequest();
        objConnectUserRequest.Email = p_strEmail;

        Location location = RequestHandler.getInstance().getLocation();
        double latitude = 32.1812643;
        double longitude = 34.9781035;


        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        objConnectUserRequest.Lat = latitude;
        objConnectUserRequest.Lng = longitude;

        return doPostRequest(objConnectUserRequest);
    }

    public JsonElement createTrip(String p_strEmail, ArrayList<AttractionType> attractionTypes) throws IOException {

        CreateTripRequest objConnectUserRequest = new CreateTripRequest();
        objConnectUserRequest.UserEmail = p_strEmail;


        Location location = RequestHandler.getInstance().getLocation();
        double latitude = 32.1812643;
        double longitude = 34.9781035;

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        objConnectUserRequest.Lat = latitude;
        objConnectUserRequest.Lng = longitude;

        objConnectUserRequest.AttractionTypes = new ArrayList<>();

        if (attractionTypes == null) {
            attractionTypes = new ArrayList<>();
        }

        for (AttractionType attractionType : attractionTypes) {
            objConnectUserRequest.AttractionTypes.add(attractionType);
        }


        return doPostRequest(objConnectUserRequest);


    }

    public Trip getTrip(String p_strTripId, String p_strEmail, boolean isLoadAttractions) throws IOException {
        Trip retVal = null;

        GetTripRequest tripRequest = new GetTripRequest();

        Location location = RequestHandler.getInstance().getLocation();
        double latitude = 0.0;
        double longitude = 0.0;

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        tripRequest.Lat = latitude;
        tripRequest.Lng = longitude;
        tripRequest.UserEmail = p_strEmail;
        tripRequest.TripId = p_strTripId;

        JsonElement tripJson = doGetRequest(tripRequest);

        if (tripJson != null) {
            retVal = Trip.fromJSON(tripJson.getAsJsonObject(), isLoadAttractions);
        }

        return retVal;
    }

    public void updateUser(String Email, Boolean notificationsOn, int radius) throws IOException {
        UpdateUserRequest obj = new UpdateUserRequest();
        obj.Email = Email;
        obj.NotificationsOn = notificationsOn;
        obj.Radius = radius;

        doPostRequest(obj);
    }

    public void updateTrip(String TripId, AttractionType... attractionTypes) throws IOException {
        UpdateTripRequest obj = new UpdateTripRequest();
        obj.TripId = TripId;
        obj.AttractionTypes = new ArrayList<>();

        for (AttractionType attractionType : attractionTypes) {
            obj.AttractionTypes.add(attractionType);
        }

        doPostRequest(obj);
    }

    public ArrayList<Attraction> getAttractions(String TripId) throws IOException {
        ArrayList<Attraction> attractions = new ArrayList<>();

        GetAttractionRequest obj = new GetAttractionRequest();
        obj.TripId = TripId;

        Location location = RequestHandler.getInstance().getLocation();
        double latitude = 0.0;
        double longitude = 0.0;

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        obj.Lat = latitude;
        obj.Lng = longitude;

        JsonElement attractionsFromServer = doGetRequest(obj);

        if (attractionsFromServer != null) {
            JsonArray attractionsJsons = attractionsFromServer.getAsJsonArray();

            for (int i = 0; i < attractionsJsons.size(); i++) {
                JsonElement currAttraction = attractionsJsons.get(i);

                if (currAttraction != null) {
                    attractions.add(Attraction.fromJSON(currAttraction.getAsJsonObject()));
                }
            }
        }
        return attractions;
    }

    public void attractionChosen(String TripId, String AttractionId) throws IOException {
        AttractionChosenRequest obj = new AttractionChosenRequest();
        obj.AttractionId = AttractionId;
        obj.TripId = TripId;

        doPostRequest(obj);
    }

    public void attractionRated(String TripId, String AttractionId, Boolean IsGoodAttraction) throws IOException {
        AttractionRatedRequest obj = new AttractionRatedRequest();
        obj.AttractionId = AttractionId;
        obj.TripId = TripId;
        obj.IsGoodAttraction = true;
        doPostRequest(obj);
    }

    public JsonElement endAttraction(String p_strTripID, String p_strAttractionId) throws IOException {

        EndAttractionRequest objEndAttractionRequest = new EndAttractionRequest();
        objEndAttractionRequest.TripId = p_strTripID;
        objEndAttractionRequest.AttractionId = p_strAttractionId;

        return doPostRequest(objEndAttractionRequest);
    }

    public JsonElement endTrip(String p_strTripID) throws IOException {

        EndTripRequest objEndAttractionRequest = new EndTripRequest();
        objEndAttractionRequest.TripId = p_strTripID;
        return doPostRequest(objEndAttractionRequest);


    }

}
