package com.example.assignment4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

public class StaffList extends AppCompatActivity {
    List<ApplicationData> stu_data=new ArrayList<ApplicationData>();
    ArrayAdapter daysAdapter;
    DBHelper db=new DBHelper(this);
    int index=0;
    String role;

    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private JSONArray restulJsonArray;
    private ArrayList<ApplicationData> array_list;
    String id;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        //getActionBar().setTitle(id);
        role=intent.getStringExtra("role");
        GetListData("getApplicationList_student",id,role);
        //stu_data=db.Get_list_staff(role);


       // daysAdapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stu_data);
        lst=findViewById(R.id.lst);
        //lst.setAdapter(daysAdapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent act=new Intent(StaffList.this , StaffApplicationViewer.class);
                act.putExtra("num", array_list.get(position).num);
                index=array_list.get(position).num;
                act.putExtra("Roll_no", array_list.get(position).Roll_no);
                act.putExtra("Date", array_list.get(position).S_date);
                act.putExtra("C_sec", array_list.get(position).C_sec);
                act.putExtra("N_sec", array_list.get(position).N_sec);
                act.putExtra("Reason", array_list.get(position).Reason);
                act.putExtra("comment",array_list.get(position).comment);

                startActivityForResult(act,1);
            }
        });



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
                obj.comment=data.getStringExtra("comment");
                String status=data.getStringExtra("status");
                if(status.equals("Approved")&&role.equals("Coordinator"))
                {
                    obj.status="Under Processing";
                }
                else if(status.equals("Disapproved"))
                {
                    obj.status="Disapproved";
                }
                else
                {
                    obj.status="Approved";
                    UpdateSection(obj.Roll_no,obj.N_sec);
                    //db.Update_section(obj.Roll_no,obj.N_sec);
                }
                //db.update_application(obj);
                UpdateApplication(obj);
                GetListData("getApplicationList_student",id,role);
                //stu_data=db.Get_list_staff(role);
                daysAdapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stu_data);
                ListView lst=findViewById(R.id.lst);
                lst.setAdapter(daysAdapter);
            }
        }

    }


    private void UpdateApplication(final ApplicationData obj){
        final String url=apiPath+"func=deleteApplication&id="+obj.num;
        final RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String url1=apiPath+"func=insertApplication&S_date="+obj.S_date+"&id="+obj.num+"&roll="+obj.Roll_no+"&old_sec="+obj.C_sec+"&N_sec="+obj.N_sec+"&description="+obj.Reason+"&staff_comment="+obj.comment+"&app_status="+obj.status;
                StringRequest jor1=new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(StaffList.this,"Application Status Update", Toast.LENGTH_SHORT).show();
                            }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(StaffList.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });

                queue.add(jor1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StaffList.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }


    private void UpdateSection(String roll_no, String n_sec) {
        String url=apiPath+"func=updateSection&roll="+roll_no+"&sec="+n_sec;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(StaffList.this,"Updated",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StaffList.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }

    private void GetListData(String func, String roll, final String role) {
        String url=apiPath+"func="+func+"&roll="+roll+"&role="+role;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    Toast.makeText(StaffList.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    array_list=null;
                    array_list=new ArrayList<>();
                    for (int i = 0; i < restulJsonArray.length(); i++){
                        ApplicationData obj=new ApplicationData();
                        JSONObject jsonObject = restulJsonArray.getJSONObject(i);

                        obj.S_date=jsonObject.getString("S_date");
                        Toast.makeText(StaffList.this,obj.S_date,Toast.LENGTH_SHORT).show();
                        obj.num=jsonObject.getInt("id");
                        obj.Roll_no=jsonObject.getString("rollno");
                        obj.C_sec=jsonObject.getString("old_sec");
                        obj.N_sec=jsonObject.getString("N_sec");
                        obj.Reason=jsonObject.getString("description");
                        obj.comment=jsonObject.getString("staff_comment");
                        obj.status=jsonObject.getString("app_status");
                        array_list.add(obj);
                    }
                    daysAdapter= new ArrayAdapter<>(StaffList.this, R.layout.support_simple_spinner_dropdown_item, array_list);
                    lst.setAdapter(daysAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                array_list=db.Get_list_staff(role);
                Toast.makeText(StaffList.this,"No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }
}
