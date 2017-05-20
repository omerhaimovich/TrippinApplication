package trippin.trippinapp.serverAPI;

import android.location.Location;

import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import trippin.trippinapp.serverAPI.Enums.AttractionType;
import trippin.trippinapp.serverAPI.UrlRequests.AttractionChosenRequest;
import trippin.trippinapp.serverAPI.UrlRequests.AttractionRatedRequest;
import trippin.trippinapp.serverAPI.UrlRequests.ConnectUserRequest;
import trippin.trippinapp.serverAPI.UrlRequests.CreateTripRequest;
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

        objConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        objConnection.setRequestProperty("Accept-Charset", "utf-8");
        objConnection.setRequestMethod("POST");
        objConnection.setDoInput(true);
        objConnection.setDoOutput(true);

        OutputStream os = objConnection.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
        writer.write(objGson.toJson(urlRequest));
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(objConnection.getInputStream(), "utf-8"));

        String s = reader.readLine();
        String strResult = "";
        while (s != null) {
            strResult += s;
            s = reader.readLine();
        }

        return objGson.fromJson(strResult, JsonElement.class);
    }

    public JsonElement doGetRequest(IUrlRequest urlRequest) throws IOException {
        String strUrlPrefix = "http://db.cs.colman.ac.il/trippin/api/";
        strUrlPrefix += urlRequest.getURLSuffix();

        URL objURL = new URL(strUrlPrefix);
        HttpURLConnection objConnection = (HttpURLConnection) objURL.openConnection();

        objConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        objConnection.setRequestProperty("Accept-Charset", "utf-8");

        BufferedReader reader = new BufferedReader(new InputStreamReader(objConnection.getInputStream(), "utf-8"));

        String s = reader.readLine();
        String strResult = "";
        while (s != null) {
            strResult += s;
            s = reader.readLine();
        }

        return objGson.fromJson(strResult, JsonElement.class);
    }

    public JsonElement connectUser(String p_strEmail, Double p_dLat, Double p_dLng) throws IOException {

        ConnectUserRequest objConnectUserRequest = new ConnectUserRequest();
        objConnectUserRequest.Email = p_strEmail;
        objConnectUserRequest.Lat = p_dLat;
        objConnectUserRequest.Lng = p_dLng;

        return doPostRequest(objConnectUserRequest);


    }

    public JsonElement createTrip(String p_strEmail, Double p_dLat, Double p_dLng) throws IOException {

        CreateTripRequest objConnectUserRequest = new CreateTripRequest();
        objConnectUserRequest.UserEmail = p_strEmail;
        objConnectUserRequest.Lat = p_dLat;
        objConnectUserRequest.Lng = p_dLng;

        return doPostRequest(objConnectUserRequest);


    }

    public JsonElement getTrip(String p_strTripId, String p_strEmail, Double p_dLat, Double p_dLng) throws IOException {
        GetTripRequest tripRequest = new GetTripRequest();
        tripRequest.Lat = p_dLat;
        tripRequest.Lng = p_dLng;
        tripRequest.UserEmail = p_strEmail;
        tripRequest.TripId = p_strTripId;

        return doGetRequest(tripRequest);
    }

    public void updateUser(String Email, Boolean notificationsOn) throws IOException {
        UpdateUserRequest obj = new UpdateUserRequest();
        obj.Email = Email;
        obj.NotificationsOn = notificationsOn;

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

    public JsonElement getAttractions(String TripId, Double p_dLat, Double p_dLng) throws IOException {
        GetAttractionRequest obj = new GetAttractionRequest();
        obj.TripId = TripId;
        obj.Lat = p_dLat;
        obj.Lng = p_dLng;

        return doGetRequest(obj);
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

}
