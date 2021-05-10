package com.glitch.annapurna;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NeedFood extends AppCompatActivity {

    private static final String TAG = NeedFood.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputAddress, inputPhone_number,inputneedy;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;
    private String Address;
    private static final int PERMISSIONS_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_food);
        TextView textView=findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        textView.setText(currentDateandTime);


        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        inputAddress = (EditText) findViewById(R.id.Address);
        inputPhone_number = (EditText) findViewById(R.id.phone_number);
        inputneedy = (EditText) findViewById(R.id.needy);
        btnSave = (Button) findViewById(R.id.save);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("needy");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Annapurna");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Address = inputAddress.getText().toString();
                String Phonenumber = inputPhone_number.getText().toString();
                String needy = inputneedy.getText().toString();


                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(Address,Phonenumber,needy);
                } else {
                    updateUser(Address,Phonenumber,needy);
                }
                Intent intent = new Intent(view.getContext(), HomeScreen.class);
                view.getContext().startActivity(intent);
                Toast.makeText(getApplicationContext(), "Data submited", Toast.LENGTH_SHORT).show();
            }
        });

        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Save");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String Address, String Phone_number,String needy) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        com.glitch.annapurna.needy user = new needy(Address,Phone_number,needy);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                needy needy = dataSnapshot.getValue(needy.class);

                // Check for null
                if (needy == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + needy.Address + ", " + needy.Phone_number+","+ needy.needy);

                // Display newly updated name and email
             //   txtDetails.setText(user.Address + ", " + user.Phone_number+","+user.needy);

                // clear edit text
                inputAddress.setText("");
                inputPhone_number.setText("");
                inputneedy.setText("");


                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String Address, String Phone_number,String needy) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(Address))
            mFirebaseDatabase.child(userId).child("name").setValue(Address);

        if (!TextUtils.isEmpty(Phone_number))
            mFirebaseDatabase.child(userId).child("email").setValue(Phone_number);

        if (!TextUtils.isEmpty(needy))
            mFirebaseDatabase.child(userId).child("email").setValue(needy);
    }
}