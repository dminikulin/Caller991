package com.example.caller991.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller991.ContactsActivity;
import com.example.caller991.R;
import com.example.caller991.SMSActivity;

import org.w3c.dom.Text;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Cursor cursor;

    public ContactsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = getContacts();
        cursorOut(cursor);
    }

    private Cursor getContacts() {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                }, null, null);
        return cursor;
    }

    private Cursor getContactData(String contactId, String... keys) {
        Cursor cursor = context.getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                ContactsContract.Data.CONTENT_URI,
                keys,
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }

    private Cursor getPhones(String contactId) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }

    @SuppressLint("Range")
    private void cursorOut(Cursor cursor) {
        while (cursor.moveToNext()) {
            Log.e("FF", "-------------------------");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.e("FF", String.valueOf(cursor.getString(i)));
            }
        }
    }

    @SuppressLint("Range")
    private void cursorOut(Cursor cursor, String... keys) {
        while (cursor.moveToNext()) {
            Log.e("FF", "-------------------------");
            for (String key : keys) {
                Log.e("FF", key + "\t\t" +
                        cursor.getString(cursor.getColumnIndex(key))
                );
            }
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }

    private void dial(View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == null || textView.getText().length() == 0)
            return;
        try {

            ((ContactsActivity) context).callPermission.launch(Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + textView.getText()));
            context.startActivity(intent);
        } catch (Exception ignored) {

        }
    }

    private void call(View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == null || textView.getText().length() == 0)
            return;
        try {

            ((ContactsActivity) context).callPermission.launch(Manifest.permission.CALL_PHONE);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + textView.getText()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("FF", e.toString());
        }
    }

    private void SMS(View view){
        TextView textView = (TextView) view;
        if(textView.getText() == null || textView.getText().length() == 0) return;
        Intent intent = new Intent(context, SMSActivity.class);
        intent.putExtra(SMSActivity.PHONE_KEY, textView.getText().toString());
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
//            holder.nameField.setText(String.valueOf(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
//            );
//            Cursor phones = getPhones(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
//            );
//            cursorOut(phones);
//            if(phones.moveToFirst()){
//                holder.phoneField.setText(String.valueOf(
//                        phones.getString(phones.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.DATA
//                        ))
//                ));
////                phones.moveToFirst();
//                cursorOut(phones);
//            }
            String[] keys = {
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.DATA2,
                    ContactsContract.Data.DATA3,
            };
            Cursor cursorData = getContactData(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)), keys
            );
//            cursorOut(cursorData, keys);
            holder.nameField.setText(String.valueOf(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            ));
            int phoneCount = 0;
            String val;
            while (cursorData.moveToNext()) {
                switch (cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE))) {
                    case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                        val = String.valueOf(
                                cursorData.getString(cursorData.getColumnIndex(
                                        ContactsContract.Data.DATA1
                                ))
                        );
                        switch (phoneCount++) {
                            case 0:
                                holder.phoneField.setText(val);
                                break;
                            case 1:
                                holder.phoneField2.setText(val);
                                break;
                        }
                        break;
                    case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                        val = String.valueOf(
                                cursorData.getString(cursorData.getColumnIndex(
                                        ContactsContract.Data.DATA1
                                ))
                        );
                        holder.emailField.setText(val);
                        Log.e("FF", val);
                        break;
                }
            }
        }
        holder.phoneField.setOnLongClickListener(view -> {
            SMS(view);
            return true;
        });
        holder.phoneField2.setOnLongClickListener(view -> {
            SMS(view);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        TextView nameField;
        TextView phoneField;
        TextView phoneField2;
        TextView emailField;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.nameField);
            phoneField = itemView.findViewById(R.id.phoneField);
            phoneField2 = itemView.findViewById(R.id.phoneField2);
            emailField = itemView.findViewById(R.id.emailField);
        }
    }


}
