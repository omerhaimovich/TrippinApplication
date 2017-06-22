package trippin.trippinapp.services;

import android.content.Context;
import android.location.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import trippin.trippinapp.helpers.MySQLHelper;
import trippin.trippinapp.helpers.NotificationHelper;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.RequestHandler;

/***************************************
 * Created by Hila Partuk on 03-Jun-17.
 ***************************************/

public class GetNewAttractionsTask extends TimerTask {

    private static Context mContext;
    static GetNewAttractionsTask mAttractionsTask;

    public GetNewAttractionsTask(Context ctx) {
        mContext = ctx;
    }

    public static GetNewAttractionsTask getInstance(){

        // maybe this make all the problems
        if (mAttractionsTask == null) {
            mAttractionsTask = new GetNewAttractionsTask(mContext);
        }

        return mAttractionsTask;
    }

    @Override
    public void run() {
        Location location = RequestHandler.getInstance().getLocation();

        if (location != null) {
            User currentUser = User.getCurrentUser();

            if (currentUser != null) {

                if (currentUser.getCurrentTrip() != null) {

                    try {
                        ArrayList<Attraction> attractionsFromServer =
                                RequestHandler.getInstance().getAttractions(currentUser.getCurrentTrip().getGoogleID());

                        if (attractionsFromServer != null) {
                            MySQLHelper mySQLHelper = new MySQLHelper(mContext);

                            ArrayList<Attraction> attractionsFromDB = mySQLHelper.getAttractions();

                            if (attractionsFromDB != null) {

                                ArrayList<Attraction> notificationAttractions = new ArrayList<>();

                                for (Attraction currAttFromServer : attractionsFromServer) {

                                    boolean isAttractionInDB = false;

                                    for (Attraction currAttFromDB : attractionsFromDB) {
                                        if (currAttFromDB.getID().equals(currAttFromServer.getID())) {
                                            isAttractionInDB = true;

                                            break;
                                        }
                                    }

                                    if (isAttractionInDB == false) {
                                        notificationAttractions.add(currAttFromServer);
                                    }
                                }

                                if (notificationAttractions.isEmpty() == false) {
                                    NotificationHelper.create(mContext, notificationAttractions);
                                }

                                mySQLHelper.deleteDataFromTable();
                                for (Attraction currAttFromServer : attractionsFromServer) {

                                    mySQLHelper.addAttraction(currAttFromServer);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        }
    }

