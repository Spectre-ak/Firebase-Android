# Firebase-Android
## Using Firebase Real Time Database to create a chat application

### First you have to add your application to a Firebase Project and create a database.
### During adding your application to the firebase project download the google-services.json file and copy it to the app folder inside your Android project.

#### Dependicies to be added to the build.gradel(app) are below

    implementation 'com.google.firebase:firebase-analytics:17.2.1'
    implementation 'com.google.firebase:firebase-database:19.2.0'
    implementation 'com.google.firebase:firebase-messaging:18.0.0'
    implementation 'com.squareup.okhttp3:okhttp:3.4.1'

#### Add the following under the dependencies in build.gradel(project) file
    classpath "com.android.tools.build:gradle:4.0.1"
    
    
    
#### Sample code for sending a message to the firebase database:
    
    //make a Database Reference object first
    DatabaseReference databaseReference;
    //following will create a sub parent in your database of name Hello
    //Note:- This parent will be only visible if it has a child or value
    databaseReference= FirebaseDatabase.getInstance().getReference("Hello");
    //this will create a child named child1 inside Hello with the message as msg
    databaseReference.child("child1").setValue("msg");
    //by using push() multiple messages can be added
    databaseReference.push().child("child1").setValue("msg");
    
    //following can be used for retrieving values from the database and this will also work whenever the
    //values are changed on the database
    ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //data is returned in the form of hashmap 
                //iterating the hasmap will give the desired data
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                System.out.println(hashMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error...
            }
        };
        databaseReference.addValueEventListener(postListener);
        
        
#### So now.... How app works---->
      At first for a new user the user will be asked for a username so as to create a reference on the database.
      Like Below---image
        
