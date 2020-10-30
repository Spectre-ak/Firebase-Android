package com.example.rte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReferenceyour;
    DatabaseReference databaseReferenceother;
    static int valtoiden=0;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    String you;
    String otheruser;
    LinearLayout linearLayout;
    ScrollView scrollView;
    Button send;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        scrollView=(ScrollView)findViewById(R.id.scroll);


        editText=(EditText)findViewById(R.id.editTextTextMultiLine);
        send=(Button)findViewById(R.id.button2);
        Bundle bundle=getIntent().getExtras();
        you=bundle.getString("You");
        otheruser=bundle.getString("other");
        preferences=getSharedPreferences(you+"&"+otheruser,MODE_PRIVATE);
        editor=preferences.edit();
        valtoiden=preferences.getInt("IDEN",-1);
        System.out.println(valtoiden);
        if(valtoiden==-1)valtoiden=0;
        else loadusingsharedpredf();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        System.out.println(you+"  "+otheruser);
        databaseReferenceyour= FirebaseDatabase.getInstance().getReference(you+"to"+otheruser);
        databaseReferenceother=FirebaseDatabase.getInstance().getReference(otheruser+"to"+you);
        System.out.println(you+"to"+otheruser);
        System.out.println(otheruser+"to"+you);

        //databaseReferenceyour.child("msg").setValue("hello try");
        //databaseReferenceother.child("msg").setValue("hello form otrghe user");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  databaseReferenceother.child("msg").orderByPriority();
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                System.out.println(hashMap);
                List<String> userslist=new ArrayList();
                try{
                    List<String> arrfordate=new ArrayList<>();
                    for(Object users:hashMap.values()){
                        HashMap hashMap1=(HashMap)users;
                        for(Object object:hashMap1.values()){
                            //  System.out.println(object.toString());
                            userslist.add(object.toString());
                            String ar[]=object.toString().split("QQ!!@@QQ!!");
                            arrfordate.add(ar[1]);
                        }
                    }
                    Collections.sort(arrfordate);
                    List<String> msgs=new ArrayList<>();
                    //  System.out.println(arrfordate);//----contine here
                    for(int i=0;i<arrfordate.size();i++){
                        String aa=arrfordate.get(i);
                        for(String ss:userslist){
                            if(ss.contains(aa)){
                                msgs.add(ss);
                            }
                        }
                    }
                    // System.out.println(msgs);
                    for(String a:msgs){
                        if(a.split("QQ!!@@QQ!!")[0].contains("cessation")){
                          //  SharedPreferences sharedPreferences1=getSharedPreferences("MainActivity",MODE_PRIVATE);
                          //  SharedPreferences.Editor editor1=sharedPreferences1.edit();
                          //  editor1.putString("cessation","ok");editor1.commit();
                          //  System.out.println("ffodf");
                        }
                        String otherusermsg=a+" otheruser"+otheruser;
                        valtoiden++;
                        editor.putInt("IDEN",valtoiden);
                        editor.putString("TXT"+valtoiden,otherusermsg);
                        editor.commit();
                        TextView textView=new TextView(Chat.this);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextIsSelectable(true);
                        textView.setText(a.split("QQ!!@@QQ!!")[0]);
                        TextView textView1=new TextView(Chat.this);
                        textView1.setTextColor(Color.WHITE);
                        textView1.setTextIsSelectable(true);
                        textView1.setTextSize(9f);
                        textView1.setText(a.split("QQ!!@@QQ!!")[1]);
                        linearLayout.addView(textView);
                        linearLayout.addView(textView1);
                    }
                    databaseReferenceother.removeValue();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        databaseReferenceother.addValueEventListener(postListener);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=editText.getText().toString();
                if(msg.length()==0){
                    Toast.makeText(Chat.this,"EMPTY MSG!!!",Toast.LENGTH_LONG).show();
                }
                else if(iswhiteonly(msg)==true){
                    editText.setText("");
                    String artime= Calendar.getInstance().getTime().toString().split("GMT")[0];
                    msg=msg+" QQ!!@@QQ!!"+artime;
                    System.out.println(msg);
                    final String finalMsg = msg+" you"+you;final String finalMsg1 = msg;

                    //////sending notification test

                    ///////

                    databaseReferenceyour.push().child("msg").setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // databaseReferenceyour.orderByPriority();

                            TextView textView=new TextView(Chat.this);
                            textView.setTextColor(Color.WHITE);
                            textView.setGravity(5);
                            //textView.setTextSize(9f);
                            textView.setTextIsSelectable(true);
                            textView.setText(finalMsg1.split("QQ!!@@QQ!!")[0]);
                            TextView textView1=new TextView(Chat.this);
                            textView1.setTextColor(Color.WHITE);
                            textView1.setGravity(5);
                            textView1.setTextSize(9f);
                            textView1.setTextIsSelectable(true);
                            textView1.setText(finalMsg1.split("QQ!!@@QQ!!")[1]);
                            linearLayout.addView(textView);
                            linearLayout.addView(textView1);
                            valtoiden++;
                            editor.putInt("IDEN",valtoiden);
                            editor.putString("TXT"+valtoiden, finalMsg);
                            editor.commit();
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                            editText.setFocusableInTouchMode(true);
                            editText.post(new Runnable() {
                                @Override
                                public void run() {
                                    editText.requestFocus();
                                }
                            });
                        }
                    });

                }
                else{
                    Toast.makeText(Chat.this,"EMPTY MSG!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyBCiec200q3DXiXtE54t4PuNFThQnbXCl0")
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void loadusingsharedpredf(){
        //load using sharedprefrence and update the linearlayout
        System.out.println("DEBUG FUNC SHARED ");
        int ex=preferences.getInt("IDEN",-1);
        if(ex==-1){
            System.out.println("NO HISTORY");return;
        }
        else{String msg="";
            for(int i=1;i<=ex;i++){
                msg=preferences.getString("TXT"+i,null);
                if(msg==null)continue;
                if( msg.equals("you"+you) ||  msg.equals("otheruser"+otheruser))continue;
                if(msg.contains("otheruser")){//otheruser data
                    String ar[]=msg.split("otheruser"+otheruser);
                    TextView tv=new TextView(Chat.this);
                    //System.out.println("DEBUG FUNC SHARED "+ar[0]+"otheruser");
                    tv.setTextColor(Color.WHITE);
                    tv.setTextIsSelectable(true);
                    tv.setText(ar[0].split("QQ!!@@QQ!!")[0]);
                    //linearLayout.addView((TextView)tv);
                    TextView tv1=new TextView(Chat.this);
                    tv1.setTextIsSelectable(true);
                    tv1.setTextColor(Color.WHITE);
                    tv1.setTextSize(9f);
                    tv1.setText(ar[0].split("QQ!!@@QQ!!")[1]+"\n");
                    linearLayout.addView((TextView)tv);
                    linearLayout.addView((TextView)tv1);
                }
                else{//this user data
                    String ar[]=msg.split("you"+you);
                    TextView tv=new TextView(Chat.this);
                    tv.setGravity(5);
                    tv.setTextIsSelectable(true);
                    tv.setTextColor(Color.WHITE);
                    tv.setText(ar[0].split("QQ!!@@QQ!!")[0]);
                    System.out.println("DEBUG FUNC SHARED "+ar[0]+"you");

                    TextView tv1=new TextView(Chat.this);
                    tv1.setTextIsSelectable(true);
                    tv1.setTextSize(9f);
                    tv1.setGravity(5);
                    tv1.setTextColor(Color.WHITE);
                    tv1.setText(ar[0].split("QQ!!@@QQ!!")[1]+"\n");
                    linearLayout.addView((TextView)tv);
                    linearLayout.addView((TextView)tv1);
                }
            }
        }

    }
    public static boolean iswhiteonly(String s){ int conbl=0,connl=0;

        for(Character c:s.toCharArray()){
            if(c!=' '){
                conbl++;
                //return true;//alphanumerix]\
            }
            if(c!='\n'){
                connl++;
            }
        }
        //System.out.println(conbl+"    "+connl);
        if(conbl==0)
        return false;
        else if(conbl+connl==s.length())return false;
        else return true;
    }
}