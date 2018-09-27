package com.example.khire.androidquiz;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TextBased extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button save, next;
    TextView qsNo, qsDesc;
    EditText answerText;
    String answer, rightanswer;
    boolean limitReached = false;
    static int index = 0;
    static final ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_based);


        save = findViewById(R.id.save);
        next = findViewById(R.id.next);
        qsDesc = findViewById(R.id.quesText1);
        qsNo = findViewById(R.id.quesNo1);
        answerText = findViewById(R.id.answerText);
        database = FirebaseDatabase.getInstance("https://androidquiz-22fda.firebaseio.com/");
        myRef = database.getReference().child("root").child("quiz").child("TEXT");


        retrieveCompleteData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == 5) {
                    Toast.makeText(getApplicationContext(), "Finished in next", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(TextUtils.isEmpty(answerText.getText().toString())){
                     Toast.makeText(getApplicationContext(),"Enter answer",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        checkAnswer();
                        updateQues();
                    }
                }
            }
        });


    }


    private void retrieveCompleteData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("Q1", dataSnapshot.child("q" + 1).child("description").getValue().toString());
                arrayList.add(dataSnapshot.child("q" + 1).child("description").getValue().toString());
                Log.e("Q2", dataSnapshot.child("q" + 2).child("description").getValue().toString());
                arrayList.add(dataSnapshot.child("q" + 2).child("description").getValue().toString());
                Log.e("Q3", dataSnapshot.child("q" + 3).child("description").getValue().toString());
                arrayList.add(dataSnapshot.child("q" + 3).child("description").getValue().toString());
                Log.e("Q4", dataSnapshot.child("q" + 4).child("description").getValue().toString());
                arrayList.add(dataSnapshot.child("q" + 4).child("description").getValue().toString());
                Log.e("Q5", dataSnapshot.child("q" + 5).child("description").getValue().toString());
                arrayList.add(dataSnapshot.child("q" + 5).child("description").getValue().toString());
                displayTextqs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void displayTextqs() {

        qsNo.setText(String.valueOf(index + 1));

        if (arrayList.size() != 0) {
            if (index != 5) {
                Log.e("q in disMcq", arrayList.get(index).toString());
                qsDesc.setText(arrayList.get(index).toString());
            } else {
                Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Arraylist is empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateQues() {

        if (index != 4) {
            index = index + 1;
            displayTextqs();
            answerText.setText("");
        } else {
            limitReached = true;
        }
    }

    private void rightAnswer() {

        Location location;
        LocationManager locationManager;
        double latitude=0,longitude=0;
        String cityname="",countryname="";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
             location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             latitude = location.getLatitude();
             longitude = location.getLongitude();
        }


        Calendar calendar = Calendar.getInstance();
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        List<Address> addresses;
        try{

            addresses = geocoder.getFromLocation(latitude,longitude,10);
            if(addresses.size()>0){
                for(Address addr:addresses){
                    if(addr.getLocality() != null && addr.getLocality().length() >0){
                        cityname = addr.getLocality();
                        countryname = addr.getCountryName();
                        break;
                    }
                }
            }

        }catch (IOException i){
            i.printStackTrace();
        }
        switch (index){
            case 0:
                int year = calendar.get(Calendar.YEAR);
                String syear = String.valueOf(year);
                Log.e("Current Year",syear);
                rightanswer = syear;
                break;
            case 1:
                int month = calendar.get(Calendar.MONTH)+1;
                String smonth = String.valueOf(month);
                Log.e("Current month",smonth);
                rightanswer = smonth;
                break;
            case 2:
                int date = calendar.get(Calendar.DATE);
                String sdate = String.valueOf(date);
                Log.e("Current month",sdate);
                rightanswer = sdate;
                break;
            case 3:
                    rightanswer = countryname;
                    Log.e("Country",countryname);
                break;
            case 4:
                    rightanswer = cityname;
                    Log.e("City",cityname);
                break;
        }
    }

    private void checkAnswer(){
        rightAnswer();
        if(answerText.getText().toString().equalsIgnoreCase(rightanswer)){
            Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Wrong",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
    }


}
