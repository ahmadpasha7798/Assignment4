package com.example.assignment4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ApplicationData> stu_data=new ArrayList<ApplicationData>();
    ArrayAdapter daysAdapter;
    DBHelper db=new DBHelper(this);
    int A_num=0;
    int index=0;
    String id;


    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private JSONArray restulJsonArray;
    private ArrayList<ApplicationData> array_list;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        if (PermissionManager.checkPermissionINTERNET(this))
            GetListData("getApplicationList_student",id,"Student");
        Get_App_num();
        lst=findViewById(R.id.lst);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent act=new Intent(MainActivity.this , UpdateForm.class);
                act.putExtra("num", array_list.get(position).num);
                index=array_list.get(position).num;
                act.putExtra("Roll_no", array_list.get(position).Roll_no);
                act.putExtra("Date", array_list.get(position).S_date);
                act.putExtra("C_sec", array_list.get(position).C_sec);
                act.putExtra("N_sec", array_list.get(position).N_sec);
                act.putExtra("Reason", array_list.get(position).Reason);
                act.putExtra("comment",array_list.get(position).comment);
                act.putExtra("status",array_list.get(position).status);

                startActivityForResult(act,1);
            }
        });

        FloatingActionButton btn=findViewById(R.id.log_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act=new Intent(MainActivity.this , Formactivity.class);
                act.putExtra("num", A_num+1);
                act.putExtra("Username",id);
                startActivityForResult(act,0);
             }
        });

    }

    private void Get_App_num() {
        String url=apiPath+"func=getAppNum";
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);
                    //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                    restulJsonArray = resultJsonObject.getJSONArray("output");

                    JSONObject jsonObject = restulJsonArray.getJSONObject(0);

                    A_num=jsonObject.getInt("max");
                    Toast.makeText(MainActivity.this,String.valueOf(A_num),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ApplicationData obj = new ApplicationData();
                obj.num=index;
                obj.Roll_no = data.getStringExtra("Roll_no");
                obj.S_date = data.getStringExtra("Date");
                obj.C_sec = data.getStringExtra("C_sec");
                obj.N_sec = data.getStringExtra("N_sec");
                obj.Reason = data.getStringExtra("Reason");
                obj.comment="No comment";
                obj.status="Pending";
                UpdateApplication(obj);
            }
        }
        else if(requestCode==0)
        {
            if (resultCode == RESULT_OK) {
                A_num++;

                ApplicationData obj = new ApplicationData();
                obj.num=A_num;
                assert data != null;
                obj.Roll_no = data.getStringExtra("Roll_no");
                obj.S_date = data.getStringExtra("Date");
                obj.C_sec = data.getStringExtra("C_sec");
                obj.N_sec = data.getStringExtra("N_sec");
                obj.Reason = data.getStringExtra("Reason");
                obj.comment="No comment";
                obj.status="Pending";
                InsertApplication(obj);
                //db.insert_Application(obj);


            }
        }
    }




    private void GetListData(String func, final String roll, String role) {
        String url=apiPath+"func="+func+"&roll="+roll+"&role="+role;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    //Toast.makeText(MainActivity.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    array_list=null;
                    array_list=new ArrayList<>();
                    for (int i = 0; i < restulJsonArray.length(); i++){
                        ApplicationData obj=new ApplicationData();
                        JSONObject jsonObject = restulJsonArray.getJSONObject(i);

                        obj.S_date=jsonObject.getString("S_date");
                        //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                        obj.num=jsonObject.getInt("id");
                        obj.Roll_no=jsonObject.getString("rollno");
                        obj.C_sec=jsonObject.getString("old_sec");
                        obj.N_sec=jsonObject.getString("N_sec");
                        obj.Reason=jsonObject.getString("description");
                        obj.comment=jsonObject.getString("staff_comment");
                        obj.status=jsonObject.getString("app_status");
                        array_list.add(obj);
                    }
                    daysAdapter= new ArrayAdapter<>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, array_list);
                    lst.setAdapter(daysAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                array_list=db.Get_list(roll);
               // Toast.makeText(MainActivity.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

                queue.add(jor);
    }


    private void InsertApplication(final ApplicationData obj) {
        String url1=apiPath+"func=insertApplication&S_date="+obj.S_date+"&id="+obj.num+"&roll="+obj.Roll_no+"&old_sec="+obj.C_sec+"&N_sec="+obj.N_sec+"&description="+obj.Reason+"&staff_comment="+obj.comment+"&app_status="+obj.status;
        final RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor1=new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //   Toast.makeText(MainActivity.this,"Application Inserted", Toast.LENGTH_SHORT).show();
                GetListData("getApplicationList_student",id,"Student");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                db.insert_Application(obj);

               //Toast.makeText(MainActivity.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor1);
    }


    private void UpdateApplication(final ApplicationData obj){
        final String url=apiPath+"func=deleteApplication&id="+obj.num;
        final RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InsertApplication(obj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"Update Error: "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }
}
