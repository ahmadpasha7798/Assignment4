package com.example.assignment4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private ProgressDialog processDialog;
    private JSONArray restulJsonArray;
    private int success = 0;
    private ArrayList<ApplicationData> array_list;
    private User user;
    public static final String DATABASE_NAME="Student.db";
    public static final String LOGIN_TABLE="Login_data";
    public static final String STUDENT_TABLE="Student_data";
    public static final String STAFF_TABLE="Staff_data";
    public static final String APPLICATION_TABLE="Application_data";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+LOGIN_TABLE+" (id text primary key, pwd text, role text);");
        db.execSQL("CREATE TABLE "+STUDENT_TABLE+" (rollno text primary key, name text,section text);");
        db.execSQL("CREATE TABLE "+STAFF_TABLE+" (id text primary key, name text,Designation text);");
        db.execSQL("CREATE TABLE "+APPLICATION_TABLE+" (S_date text, id integer primary key, rollno text,N_sec text, description text,staff_comment text,app_status text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ LOGIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ STAFF_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ APPLICATION_TABLE);
        onCreate(db);
    }


    public void insert_Application(ApplicationData obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",obj.num);
        contentValues.put("S_date",obj.S_date);
        contentValues.put("rollno",obj.Roll_no);
        contentValues.put("N_sec",obj.N_sec);
        contentValues.put("description",obj.Reason);
        contentValues.put("staff_comment",obj.comment);
        contentValues.put("app_status",obj.status);
        db.insert(APPLICATION_TABLE,null, contentValues);
    }

    public ArrayList<ApplicationData> Get_list(String roll) {
        ArrayList<ApplicationData> array_list = new ArrayList<ApplicationData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + APPLICATION_TABLE+" WHERE rollno = '" + roll+"';", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ApplicationData obj=new ApplicationData();
            obj.S_date=res.getString(0);
            obj.num=res.getInt(1);
            obj.Roll_no=res.getString(2);
            Cursor res1=this.get_Studata(obj.Roll_no);
            res1.moveToFirst();
            obj.C_sec=res1.getString(2);
            obj.N_sec=res.getString(3);
            obj.Reason=res.getString(4);
            obj.comment=res.getString(5);
            obj.status=res.getString(6);

            array_list.add(obj);
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<ApplicationData> Get_list_staff(String role) {
        ArrayList<ApplicationData> array_list = new ArrayList<ApplicationData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(role.equals("Coordinator"))
            res= db.rawQuery("SELECT * FROM " + APPLICATION_TABLE+" WHERE app_status='Pending';", null);
        else
            res=db.rawQuery("SELECT * FROM " + APPLICATION_TABLE+" WHERE app_status='Under Processing';", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ApplicationData obj=new ApplicationData();
            obj.S_date=res.getString(0);
            obj.num=res.getInt(1);
            obj.Roll_no=res.getString(2);
            Cursor res1=this.get_Studata(obj.Roll_no);
            res1.moveToFirst();
            obj.C_sec=res1.getString(2);
            obj.N_sec=res.getString(3);
            obj.Reason=res.getString(4);
            obj.comment=res.getString(5);
            obj.status=res.getString(6);

            array_list.add(obj);
            res.moveToNext();
        }

        return array_list;
    }
    public ArrayList<ApplicationData> GetAll() {
        ArrayList<ApplicationData> array_list = new ArrayList<ApplicationData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res= db.rawQuery("SELECT * FROM " + APPLICATION_TABLE+";", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ApplicationData obj=new ApplicationData();
            obj.S_date=res.getString(0);
            obj.num=res.getInt(1);
            obj.Roll_no=res.getString(2);
            Cursor res1=this.get_Studata(obj.Roll_no);
            res1.moveToFirst();
            obj.C_sec=res1.getString(2);
            obj.N_sec=res.getString(3);
            obj.Reason=res.getString(4);
            obj.comment=res.getString(5);
            obj.status=res.getString(6);

            array_list.add(obj);
            res.moveToNext();
        }

        return array_list;
    }

    public int Get_App_num(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT MAX(id) FROM "+APPLICATION_TABLE,null);
        if(res.getCount()>0) {
            res.moveToFirst();
            return res.getInt(0);
        }
        return 0;
    }

    public boolean chk_user(String id) {
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * From "+STUDENT_TABLE+" WhERE rollno='"+id+"'  ;",null);
        if(res.getCount()>0)
            return true;
        else
        {
            Cursor res1=db.rawQuery("Select * From "+STAFF_TABLE+" WHERE id='"+id+"';",null);
            int a=res1.getCount();
            if(res1.getCount()>0)
                return true;
            else
                return false;
        }
    }



    public Cursor get_Studata(String id) {
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor res=db.query(STUDENT_TABLE,new String[]{"rollno","name","section"},"  rollno=?",new String[]{id},null,null,null);
        return res;

    }

    public void Update_section(String roll_no, String n_sec) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=get_Studata(roll_no);
        res.moveToFirst();
        db.delete(STUDENT_TABLE,"rollno=?",new String[]{roll_no});
        ContentValues contentValues=new ContentValues();
        contentValues.put("rollno",roll_no);
        contentValues.put("name",res.getString(1));
        contentValues.put("section",n_sec);
        db.insert(STUDENT_TABLE,null, contentValues);
    }

    private class getApplicationList extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        public String roll="";
        public String role="";
        public String func="";
        String response = "";
        HashMap<String, String> postDataParams;
        public getApplicationList(){}
        public getApplicationList(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            /*super.onPreExecute();
            processDialog = new ProgressDialog(mContext);
            processDialog.setMessage("Please  Wait ...");
            processDialog.setCancelable(false);
            processDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String p=apiPath+"func="+func+"&roll="+roll+"&role="+role;
            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(p, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                restulJsonArray = resultJsonObject.getJSONArray("output");
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            array_list=null;
            array_list=new ArrayList<>();
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }

            if (success == 1) {
                if (null != restulJsonArray) {

                    for (int i = 0; i < restulJsonArray.length(); i++) {
                        try {
                            ApplicationData obj=new ApplicationData();
                            JSONObject jsonObject = restulJsonArray.getJSONObject(i);
                            obj.S_date=jsonObject.getString("S_date");
                            obj.num=jsonObject.getInt("id");
                            obj.Roll_no=jsonObject.getString("roll");
                            obj.C_sec=jsonObject.getString("old_sec");
                            obj.N_sec=jsonObject.getString("N_sec");
                            obj.Reason=jsonObject.getString("description");
                            obj.comment=jsonObject.getString("staff_comment");
                            obj.status=jsonObject.getString("app_status");
                            array_list.add(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }//end of async task

    private class getUser extends AsyncTask<Void, Void, Void> {
        public String id="";
        public String func="";
        String response = "";
        HashMap<String, String> postDataParams;
        public getUser(){}


        @Override
        protected void onPreExecute() {
            /*super.onPreExecute();
            processDialog = new ProgressDialog(mContext);
            processDialog.setMessage("Please  Wait ...");
            processDialog.setCancelable(false);
            processDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //String p=apiPath+"func="+func+"&roll="+roll+"&role="+role;
            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            //response = service.sendRequest(p, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                restulJsonArray = resultJsonObject.getJSONArray("output");
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            array_list=null;
            array_list=new ArrayList<>();
            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }

            if (success == 1) {
                if (null != restulJsonArray) {

                    for (int i = 0; i < restulJsonArray.length(); i++) {
                        try {
                            ApplicationData obj=new ApplicationData();
                            JSONObject jsonObject = restulJsonArray.getJSONObject(i);
                            obj.S_date=jsonObject.getString("S_date");
                            obj.num=jsonObject.getInt("id");
                            obj.Roll_no=jsonObject.getString("roll");
                            obj.C_sec=jsonObject.getString("old_sec");
                            obj.N_sec=jsonObject.getString("N_sec");
                            obj.Reason=jsonObject.getString("description");
                            obj.comment=jsonObject.getString("staff_comment");
                            obj.status=jsonObject.getString("app_status");
                            array_list.add(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }//end of async task
}
