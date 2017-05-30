package trippin.trippinapp.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;

import trippin.trippinapp.R;
import trippin.trippinapp.activities.LoginActivity;
import trippin.trippinapp.model.Attraction;

/**
 * Created by tacco on 5/30/17.
 */

public class NotificationHelper {


    public static void create(Context context, ArrayList<Attraction> attractions)
    {
        String content = "";

        for (Attraction attr : attractions) {
            content += attr.getName() + ", ";
        }

        content.replace(",", "-");
        content = content.substring(0,content.length()-1);


        content += "is Near By, Click here to see more details";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_public_black_24dp)
                        .setContentTitle("New Attractions")
                        .setContentText(content);

        Intent resultIntent = new Intent(context, LoginActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager)context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
