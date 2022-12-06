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

public class MainActivity2 extends AppCompatActivity {

    EditText editNama, editHari, editSKS, updateNama, updateHari, updateSKS;

    DatabaseReference databaseReference;
    Matkul matkul;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        databaseReference = FirebaseDatabase.getInstance().getReference(Matkul.class.getSimpleName());

        editNama = findViewById(R.id.txt_nama);
        editHari = findViewById(R.id.txt_hari);
        editSKS = findViewById(R.id.txt_sks);

        updateNama = findViewById(R.id.update_nama);
        updateHari = findViewById(R.id.update_hari);
        updateSKS = findViewById(R.id.update_sks);

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

    private void insertData() {
        Matkul newMatkul = new Matkul();

        String nama = editNama.getText().toString();
        String hari = editHari.getText().toString();
        int Sks = Integer.parseInt(editSKS.getText().toString());

        if (nama != "" && hari != "" && Sks != 0) {
            newMatkul.setNama(nama);
            newMatkul.setHari(hari);
            newMatkul.setSks(Sks);

            databaseReference.push().setValue(newMatkul);
            Toast.makeText(this, "Data Matkul Terkirim", Toast.LENGTH_LONG).show();
        }
    }

    private void readData() {

        matkul = new Matkul();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot currentData:snapshot.getChildren()){
                        key = currentData.getKey();
                        matkul.setNama(currentData.child("nama").getValue().toString());
                        matkul.setHari(currentData.child("hari").getValue().toString());
                        matkul.setSks(Integer.parseInt(currentData.child("sks").getValue().toString()));
//                        Integer.parseInt(matkul.setSks(currentData.child("Sks").getValue().toString()));
                    }
                }

                updateNama.setText(matkul.getNama());
                updateHari.setText(matkul.getHari());
                updateSKS.setText(String.valueOf(matkul.getSks()));

                Toast.makeText(MainActivity2.this, "Data Matkul Ditampilkan!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        Matkul updatedData = new Matkul();
        updatedData.setNama(updateNama.getText().toString());
        updatedData.setHari(updateHari.getText().toString());
        updatedData.setSks(Integer.parseInt(updateSKS.getText().toString()));

        databaseReference.child(key).setValue(updatedData);

        Toast.makeText(MainActivity2.this, "Data Matkul Diperbarui!", Toast.LENGTH_SHORT).show();
    }

    private void removeData() {
        databaseReference.child(key).removeValue();
        Toast.makeText(this, "Berhasil Menghapus Data Matkul!", Toast.LENGTH_SHORT).show();
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