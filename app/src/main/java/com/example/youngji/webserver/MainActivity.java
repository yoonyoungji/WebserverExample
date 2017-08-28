package com.example.youngji.webserver;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private Webserver server;
    private static final String TAG = "MYSERVER";
    private static final int PORT = 8080;
    com.devbrackets.android.exomedia.ui.widget.VideoView Videoview;
    TextView text;
    String ipAddress;

    String sendurl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.ipaddr);
        Videoview = (com.devbrackets.android.exomedia.ui.widget.VideoView) findViewById(R.id.video_view);
        ipAddress = getLocalIpAddress();

        if (ipAddress != null) {
            text.setText("Please Access:" + "http://" + ipAddress + ":" + PORT);
            sendurl = "http://" + ipAddress + ":" + PORT;
        } else {
            text.setText("Wi-Fi Network Not Available");
        }

        if (shouldAskPermissions()) {
            askPermissions();
        }

        server = new Webserver(MainActivity.this);
        try {
            server.start();
        } catch(IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        sendGetRequest(sendurl);
    }

    private void sendGetRequest(String serverurl){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,serverurl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                if(response.contains("Error")){
                    Log.d("Error", response.toString());
                }
                else{
                    System.out.println("----Local play url-----: "+ response);
                    Videoview.setVideoURI(Uri.parse(response));
                    Videoview.start();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Error", error.toString());
            }
        }){
        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddr = inetAddress.getHostAddress();
                        return ipAddr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d(TAG, ex.toString());
        }
        return null;
    }

}
