package trippin.trippinapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

import trippin.trippinapp.serverAPI.RequestHandler;

public class GetAttractionsService extends Service {
    public GetAttractionsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {


        return super.onStartCommand(intent, flags, startId);
    }
}
