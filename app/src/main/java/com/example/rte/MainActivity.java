package com.example.rte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText editText; Button chat;
    Button button;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.editTextTextPersonName);
        button=(Button)findViewById(R.id.button);
        chat=(Button)findViewById(R.id.button3);
        preferences=getSharedPreferences("MainActivity",MODE_PRIVATE);
        editor=preferences.edit();
        final String string=preferences.getString("LOGGEDSTATUS",null);
        if(preferences.getString("cessation","not").equals("ok")){
            // this.finishAffinity();
            MainActivity.this.finish();
            System.exit(0);
        }
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(MainActivity.this,FirebaseService.class));
            }
        });t1.start();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(string!=null && string.equals("YES")){
                    //start the next activity
                    Intent intent=new Intent(MainActivity.this,ChatLists.class);
                    intent.putExtra("username",preferences.getString("USERNAME",null));
                    startActivity(intent);
                }
            }
        });
        if(string!=null && string.equals("YES")){
            //start the next activity
            Intent intent=new Intent(MainActivity.this,ChatLists.class);
            intent.putExtra("username",preferences.getString("USERNAME",null));
            startActivity(intent);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=editText.getText().toString();
                if(username.length()==0){
                    Toast.makeText(MainActivity.this,"INVALID USERNAME!!!",Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        // databaseReference= FirebaseDatabase.getInstance().getReference(username);
                        databaseReference= FirebaseDatabase.getInstance().getReference("All Users");
                        databaseReference.push().setValue(username);

                        editor.putString("LOGGEDSTATUS","YES");
                        editor.putString("USERNAME",username);
                        editor.commit();
                        Intent intent=new Intent(MainActivity.this,ChatLists.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this,"USERNAME MUST NOT CONTAIN '.', '#', '$', '[', or ']'",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}