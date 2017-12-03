package com.seminario.pardo.presthing;

/**
 * Created by Maxi on 12/10/2017.
 */
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyTestService extends IntentService {

    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyTestService", "Servicio ejecutandose. Recordatorios");
    }
}
