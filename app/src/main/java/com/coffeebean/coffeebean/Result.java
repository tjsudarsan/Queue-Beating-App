package com.coffeebean.coffeebean;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().hide();

        String tableno = getIntent().getStringExtra("tableno");
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("Orders");

        Database.child(tableno + "/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue().toString();
                TextView statustxt = (TextView) findViewById(R.id.status);
                TextView orderextra = (TextView) findViewById(R.id.orderextra);
                statustxt.setText(status);
                if(status.equals("Order Ready...")){
                    v.vibrate(1000);
                    orderextra.setText("Order Anything Else? Please Click Here");
                    orderextra.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Result.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
