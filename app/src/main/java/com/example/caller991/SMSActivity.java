package com.example.caller991;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.caller991.databinding.ActivityContactsBinding;
import com.example.caller991.databinding.ActivitySmsactivityBinding;

public class SMSActivity extends AppCompatActivity {
    public static final String PHONE_KEY = "phone_key";
    private ActivitySmsactivityBinding binding;
    private ActivityResultLauncher<String> smsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmsactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneTextSMS.setText(
                getIntent().getStringExtra(PHONE_KEY)
        );

        smsPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if(result)
                        sendSMS(binding.phoneTextSMS.getText().toString(), binding.textEditSMS.getText().toString());
                }
        );

        binding.sendSMSButton.setOnClickListener(view -> {
            smsPermission.launch(Manifest.permission.SEND_SMS);
        });
    }
    
    void sendSMS(String phone, String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:" + phone));
        intent.putExtra("sms_body", text);
        startActivity(intent);
    }
}