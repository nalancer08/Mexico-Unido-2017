package com.appbuilders.mexicounido2017;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appbuilders.mexicounido2017.Controller.Home;

import libraries.ReSTCallback;
import libraries.ReSTClient;
import libraries.ReSTRequest;
import libraries.ReSTResponse;

public class Splash extends AppCompatActivity {

    private CountDownTimer timer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Setting full screen
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        // Setting the intent
        this.intent = new Intent(Splash.this, Home.class);

        // Setting the timer
        this.timer = new CountDownTimer(5500, 1000) {

            public void onTick(long millisUntilFinished) {

                double time = millisUntilFinished / 1000;

                if ( time == 3 ) {

                    // Right now we gonna get the cameras throw my API, but if you cant connect, you gonna use the original file.
                    ReSTClient rest = new ReSTClient("http://appbuilders.com.mx/fotomultas_cdmx/getCameras");
                    ReSTRequest request = new ReSTRequest(ReSTRequest.REST_REQUEST_METHOD_GET, "");
                    rest.execute(request, new ReSTCallback() {
                        @Override
                        public void onSuccess(ReSTResponse response) {

                            Log.d("AB_DEV", "RESPUESTA = " + response.body);

                            //JSONObject jsonResponse = JsonFileManager.stringToJSON(response.body);
                            //camaras = jsonResponse.getJSONArray("data");
                            timer.cancel();

                            //intent.putExtra("camaras", camaras.toString());
                            //intent.putExtra("onMarkersOnline", onMarkersOnline);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onError(ReSTResponse response) {

                            String errorMessage = (response.statusCode == 404) ? "Failed!" : "Error " + Integer.toString(response.statusCode);
                            Log.e("AB_DEV", errorMessage);
                            //Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();

                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }

            public void onFinish() {

                startActivity(intent);
                finish();
            }
        }.start();
    }
}
