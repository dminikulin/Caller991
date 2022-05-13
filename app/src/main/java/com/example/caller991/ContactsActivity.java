package com.example.caller991;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.caller991.adapters.ContactsAdapter;
import com.example.caller991.databinding.ActivityContactsBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ActivityResultLauncher<String> readContactsPermission;
    public ActivityResultLauncher<String> callPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        readContactsPermission=registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if(result){
                        ContactsAdapter adapter = new ContactsAdapter(this);
                        binding.contactsRecycler.setAdapter(adapter);
                        binding.contactsRecycler.setLayoutManager(
                                new LinearLayoutManager(ContactsActivity.this)
                        );
                    }else{
                        Snackbar.make(binding.contactsRecycler,
                                "No permission to read contacts",
                                BaseTransientBottomBar.LENGTH_LONG
                                ).show();
                    }
                }
        );

        readContactsPermission.launch(Manifest.permission.READ_CONTACTS);

        callPermission = registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        result -> {
                            if(result){

//                                Log.e("FF", "permission trigger");
                            }
                            else{
                                callPermission.launch(Manifest.permission.CALL_PHONE);
//                                throw new RuntimeException("Something happened");
                            }
                        }
                );
//        callPermission.launch(Manifest.permission.CALL_PHONE);
    }
}