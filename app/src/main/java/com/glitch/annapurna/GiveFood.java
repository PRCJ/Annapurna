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

public class GiveFood extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputAddress, inputPhone_number,inputsabji,inputroti,inputdal,inputchawal,inputother;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_food);

        TextView textView=findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        textView.setText(currentDateandTime);
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

      //  txtDetails = (TextView) findViewById(R.id.txt_user);
        inputAddress = (EditText) findViewById(R.id.Address);
        inputPhone_number = (EditText) findViewById(R.id.phone_number);
        inputroti = (EditText) findViewById(R.id.roti);
        inputsabji = (EditText) findViewById(R.id.sabji);
        inputdal = (EditText) findViewById(R.id.dal);
        inputchawal = (EditText) findViewById(R.id.chawal);
        inputother = (EditText) findViewById(R.id.other);

        btnSave = (Button) findViewById(R.id.submit);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("donar");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

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
                String roti = inputroti.getText().toString();
                String sabji = inputsabji.getText().toString();
                String dal = inputdal.getText().toString();
                String chawal = inputchawal.getText().toString();
                String other = inputother.getText().toString();
                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(Address, Phonenumber, roti, dal, chawal, sabji, other);
                } else {
                    updateUser(Address, Phonenumber, roti, dal, chawal, sabji, other);
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
    private void createUser(String Address, String Phone_number,String roti,String dal,String chawal,String sabji,String other) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        Donar donar = new Donar(Address,Phone_number,roti,dal,chawal,sabji,other);

        mFirebaseDatabase.child(userId).setValue(donar);

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
                Donar donar = dataSnapshot.getValue(Donar.class);

                // Check for null
                if (donar == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + donar.Address + ", " + donar.phonenumber+ ","+donar.roti+","+donar.dal+","+donar.chawal+","+donar.sabji+","+donar.other);

                // Display newly updated name and email
             //   txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                inputAddress.setText("");
                inputPhone_number.setText("");
                inputroti.setText("");
                inputdal.setText("");
                inputchawal.setText("");
                inputsabji.setText("");
                inputother.setText("");
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String Address, String Phone_number,String roti,String dal,String chawal,String sabji,String other) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(Address))
            mFirebaseDatabase.child(userId).child("Address").setValue(Address);

        if (!TextUtils.isEmpty(Phone_number))
            mFirebaseDatabase.child(userId).child("Phone number").setValue(Phone_number);

        if (!TextUtils.isEmpty(roti))
            mFirebaseDatabase.child(userId).child("roti").setValue(roti);

        if (!TextUtils.isEmpty(dal))
            mFirebaseDatabase.child(userId).child("dal").setValue(dal);

        if (!TextUtils.isEmpty(chawal))
            mFirebaseDatabase.child(userId).child("chawal").setValue(chawal);

        if (!TextUtils.isEmpty(sabji))
            mFirebaseDatabase.child(userId).child("sabji").setValue(sabji);

        if (!TextUtils.isEmpty(other))
            mFirebaseDatabase.child(userId).child("other").setValue(other);
    }
}