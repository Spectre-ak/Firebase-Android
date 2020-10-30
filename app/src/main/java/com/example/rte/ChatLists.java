package com.example.rte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatLists extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lists);
        listView=(ListView)findViewById(R.id.listview);
        databaseReference= FirebaseDatabase.getInstance().getReference("All Users");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("error", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                System.out.println("tttttttoooooooooooookkkkkkkkeeeeeeeeee============="+token);
                // Log and toast
                //String msg = getString(R.string., token);
               // Log.d("token", msg);
               // Toast.makeText(ChatLists.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        //databaseReference.child("All Users").push().setValue("dddddd");
        // databaseReference.push().;
        Bundle bundle=getIntent().getExtras();
        final String username=bundle.getString("username");
        System.out.println(username);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                System.out.println(hashMap);
                List<String> userslist=new ArrayList();
                for(Object users:hashMap.values()){
                    if(!users.toString().equals(username))
                        userslist.add(users.toString());
                }
                final String arr[]=new String[userslist.size()];
                for(int i=0;i<userslist.size();i++){
                    arr[i]=userslist.get(i);
                }
                arrayAdapter=new ArrayAdapter<String>(ChatLists.this,R.layout.chatlistviewtextview,arr);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("executed on clickl d=listen");
                        Log.println(Log.INFO,"debu",arr[i]);
                        Intent intent=new Intent(ChatLists.this,Chat.class);
                        intent.putExtra("You",username);
                        intent.putExtra("other",arr[i]);

                        startActivity(intent);
                        // Toast.makeText(ChatLists.this,"will open later",Toast.LENGTH_LONG);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        databaseReference.addValueEventListener(postListener);

    }
}