package com.example.crimeandmissingpersonreporting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ComplaintsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    EditText nameEditText, ageEditText, genderEditText, descriptionEditText,residenceEditText,contactEditText;
    Button reportBtn;
     Uri mImageUri = null;
     ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        imageButton = findViewById(R.id.imageButton);
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        residenceEditText = findViewById(R.id.residenceEditText);
        contactEditText = findViewById(R.id.contactEditText);
        reportBtn = findViewById(R.id.reportBtn);

        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference().child("complaint");
        mStorage=FirebaseStorage.getInstance();
        mProgressDialog=new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            imageButton.setImageURI(mImageUri);
        }
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String age = ageEditText.getText().toString().trim();
                String gender = genderEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String residence = residenceEditText.getText().toString().trim();
                String contact = contactEditText.getText().toString().trim();

                if (!name.isEmpty() && !age.isEmpty() && !gender.isEmpty() && !description.isEmpty() && !residence.isEmpty() && !contact.isEmpty() && mImageUri!=null)
                {
                    mProgressDialog.setTitle("Uploading.............");
                    mProgressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    DatabaseReference newPost = mReference.push();

                                    newPost.child("Names").setValue(name);
                                    newPost.child("Age").setValue(age);
                                    newPost.child("Gender").setValue(gender);
                                    newPost.child("Description").setValue(description);
                                    newPost.child("Residence").setValue(residence);
                                    newPost.child("Contact").setValue(contact);
                                    newPost.child("Image").setValue(task.getResult().toString());
                                    mProgressDialog.dismiss();

                                    //Intent intent=new Intent(ComplaintsActivity.this, HistoryActivity.class);
                                   // startActivity(intent);
                                }
                            });

                        }
                    });
                }
            }
        });
    }
}