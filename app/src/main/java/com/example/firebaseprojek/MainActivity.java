package com.example.firebaseprojek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editName, editAddress, updateName, updateAddress;
//    Button insert, read, update;

    DatabaseReference databaseReference;
    Student student;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        editName = findViewById(R.id.txt_name);
        editAddress = findViewById(R.id.txt_address);
        updateName = findViewById(R.id.update_name);
        updateAddress = findViewById(R.id.update_address);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        findViewById(R.id.btn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeData();
            }
        });
    }

    private void insertData(){
        Student newStudent = new Student();
        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        if(name != "" && address != ""){
            newStudent.setName(name);
            newStudent.setAddress(address);

            databaseReference.push().setValue(newStudent);
            Toast.makeText(this, "Successfully insert data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData(){

        student = new Student();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot currentData:snapshot.getChildren()){
                        key = currentData.getKey();
                        student.setName(currentData.child("name").getValue().toString());
                        student.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                updateName.setText(student.getName());
                updateAddress.setText(student.getAddress());
                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData(){
        Student updatedData = new Student();
        updatedData.setName(updateName.getText().toString());
        updatedData.setAddress(updateAddress.getText().toString());

        //untuk update
        databaseReference.child(key).setValue(updatedData);

        //untuk hapus
//        databaseReference.child(key).removeValue();
    }

    private void removeData() {
        databaseReference.child(key).removeValue();
        Toast.makeText(this, "Remove Data Sucessfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.student){
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.matkul) {
            startActivity(new Intent(this, MainActivity2.class));
        }

        return true;
    }
}