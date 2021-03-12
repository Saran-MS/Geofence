package com.example.geofence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    TextView login;
    EditText name,ph,pass,cpass;
    ImageButton submit;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        login=findViewById(R.id.login);
        name=findViewById(R.id.name);
        ph=findViewById(R.id.phoneNo);
        pass=findViewById(R.id.password);
        cpass=findViewById(R.id.confirmpassword);
        submit=findViewById(R.id.submit);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignUp.this,LoginActivity.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()) {
                    if (pass.getText().toString().equals(cpass.getText().toString())) {
                        users.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int found=0;
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    if (Objects.equals(child.getKey(), ph.getText().toString())) {
                                        found = 1;
                                        Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if(found==0) {
                                    DatabaseReference user = users.child(ph.getText().toString());
                                    user.child("Name").setValue(name.getText().toString());
                                    user.child("Password").setValue(pass.getText().toString());
                                    user.child("Location").child("Latitude").setValue("Null");
                                    user.child("Location").child("Longitude").setValue("Null");
                                    Intent i = new Intent(SignUp.this,LoginActivity.class);
                                    startActivity(i);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Fill all Details", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean check() {
                if(ph.getText().length()!=10){
                    Toast.makeText(getApplicationContext(), "Please Enter correct mobile number", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if(!name.getText().toString().equals("") && !pass.getText().toString().equals("") && !cpass.getText().toString().equals("")){
                    return true;
                }
                return false;
            }
        });

    }
}