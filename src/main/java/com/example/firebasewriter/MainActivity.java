package com.example.firebasewriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
        EditText name,purl,yt,c,imageName,type;
        ImageView image;
        Button save,iSave;
        public Uri imageUri;
        FirebaseDatabase database;
        FirebaseStorage storage;
        StorageReference storageReference;
        DatabaseReference reference;
        SharedPreferences share;
        int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        purl = findViewById(R.id.purl);
        yt = findViewById(R.id.yt);
        image = findViewById(R.id.image);
        save = findViewById(R.id.save);
        iSave = findViewById(R.id.imageSave);
        c = findViewById(R.id.count);
        imageName = findViewById(R.id.nameImage);
        type = findViewById(R.id.type);

        share = getSharedPreferences("",0);
        database = FirebaseDatabase.getInstance();
        count = share.getInt("count",0);
        c.setText(Integer.toString(count));
        reference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to update data!", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(view -> {
            String n = name.getText().toString();
            String p = purl.getText().toString();
            String y = yt.getText().toString();
            count = Integer.parseInt(c.getText().toString());
            if(!n.equals("") && !p.equals("") && !y.equals("") && count!=0){
                Animal animal = new Animal();
                animal.name = n;
                animal.purl = p;
                animal.yt = y;
                reference.child("animals").child("e" + count).setValue(animal);

                name.setText("");
                yt.setText("");
                count =  Integer.parseInt(c.getText().toString());
                c.setText(Integer.toString(count+1));
            }
            else
                Toast.makeText(this, "Fill out all the fields", Toast.LENGTH_SHORT).show();
        });

        iSave.setOnClickListener(view -> {
            if(imageName.getText().toString().equals("") || type.getText().toString().equals(""))
                Toast.makeText(this, "Enter all fields for the image", Toast.LENGTH_SHORT).show();
            else
                choosePicture();
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null &&data.getData()!=null){
            imageUri = data.getData();
            image.setImageURI(imageUri);
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        String key = type.getText().toString();
        String id = imageName.getText().toString();
        StorageReference mountainsRef = storageReference.child(key + "/").child("images/" + id);

        mountainsRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Snackbar.make(findViewById(android.R.id.content),"Photo Uploaded",Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = share.edit();
        edit.putInt("count",count + 1);
        edit.apply();
    }
}