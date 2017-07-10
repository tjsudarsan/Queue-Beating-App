package com.coffeebean.coffeebean;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    int q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        final Spinner spinner = (Spinner) findViewById(R.id.tablenumber);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tableno,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinner1 = (Spinner) findViewById(R.id.coffee_items);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.coffeeitems,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        final TextView quantity = (TextView) findViewById(R.id.quantity);
        Button add = (Button) findViewById(R.id.add);
        Button minus = (Button) findViewById(R.id.minus);
        Button order = (Button) findViewById(R.id.orderbtn);

        final EditText name = (EditText) findViewById(R.id.name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q<10) {
                    q = q + 1;
                    String quan = "" + q + "";
                    quantity.setText(quan);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q>0){
                    q = q - 1;
                    String quan = ""+q+"";
                    quantity.setText(quan);
                }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mname = name.getText().toString();
                if(mname.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Your Name", Toast.LENGTH_SHORT).show();
                } else if(q==0){
                    Toast.makeText(getApplicationContext(),"Quantity cannout be 0",Toast.LENGTH_SHORT).show();
                } else {
                    final String mtableno = spinner.getSelectedItem().toString();
                    String mcoffee = spinner1.getSelectedItem().toString();
                    String qn = "" + q + "";

                    final String message = "Name: " + mname +
                            "\nTable No: " + mtableno +
                            "\nCoffee Item: " + mcoffee +
                            "\nQuantity: " + qn + "";

                    final HashMap<String, String> data = new HashMap<String, String>();

                    data.put("name",mname);
                    data.put("coffeeitem",mcoffee);
                    data.put("quantity",qn);
                    data.put("status","Preparing...");



                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(message + "\n\nAre You Sure Want to Conform the Order?")
                            .setTitle("Order Conformation")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
                                    progressdialog.setMessage("Loading...");
                                    progressdialog.show();
                                    progressdialog.setCancelable(false);;

                                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("Orders");

                                    Database.child(mtableno).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                progressdialog.cancel();
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("Status")
                                                        .setMessage("Order Placed! Please Wait..")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(MainActivity.this, Result.class).putExtra("tableno",mtableno);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
    }
}
