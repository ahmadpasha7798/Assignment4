package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login_Activity extends AppCompatActivity {

    //DBHelper db=new DBHelper(this);

    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private JSONArray restulJsonArray;
    private User user=null;
    String u,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        final EditText username=findViewById(R.id.id_txt);
        final EditText pwd=findViewById(R.id.password_txt);
        Button btn= findViewById(R.id.log_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u=username.getText().toString();
                p=pwd.getText().toString();
                GetUserData(username.getText().toString());
            }

        });

        TextView not_reg=findViewById(R.id.not_registered);
        not_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,RegisterUser.class);
                startActivity(intent);
            }
        });
    }

    private void GetUserData(String id) {
        String url=apiPath+"func=getUser"+"&id="+id;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    //Toast.makeText(MainActivity.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = restulJsonArray.getJSONObject(0);

                    String id=jsonObject.getString("id");
                    if(!id.equals("empty")){
                        user=new User();
                        user.id=id;
                        user.pwd=jsonObject.getString("pwd");
                        user.role=jsonObject.getString("role");
                        if(u.equals(user.id)&&p.equals(user.pwd))
                        {
                            Intent intent;
                            if(user.role.equals("Student"))
                                intent=new Intent(Login_Activity.this,MainActivity.class);
                            else
                                intent=new Intent(Login_Activity.this,StaffList.class);
                            intent.putExtra("id",user.id);
                            intent.putExtra("pwd",user.pwd);
                            intent.putExtra("role",user.role);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            Toast.makeText(Login_Activity.this,"Password is wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                        //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login_Activity.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }
}