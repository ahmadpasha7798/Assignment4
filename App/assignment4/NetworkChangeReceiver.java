package com.example.assignment4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class NetworkChangeReceiver extends BroadcastReceiver {
    Context c;
    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    @Override
    public void onReceive(Context context, Intent intent) {
        c=context;
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if(ni!=null&&ni.isConnected()){
            DBHelper db=new DBHelper(context);
            ArrayList array_list=db.GetAll();
            for(int i=0;i<array_list.size();i++){
                InsertApplication((ApplicationData) array_list.get(i));
            }


        }
    }

    private void InsertApplication(final ApplicationData obj) {
        String url1=apiPath+"func=insertApplication&S_date="+obj.S_date+"&id="+obj.num+"&roll="+obj.Roll_no+"&old_sec="+obj.C_sec+"&N_sec="+obj.N_sec+"&description="+obj.Reason+"&staff_comment="+obj.comment+"&app_status="+obj.status;
        final RequestQueue queue= Volley.newRequestQueue(c);
        StringRequest jor1=new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(MainActivity.this,"Application Inserted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(MainActivity.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor1);
    }
}
