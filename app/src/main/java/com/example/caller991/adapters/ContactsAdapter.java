package com.example.caller991.adapters;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caller991.R;

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

    private Cursor getContactData(String contactId){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.DATA1
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                new String[]{contactId},
                null
        );
        return cursor;
    }
    
    private Cursor getPhones(String contactId){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
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
                Log.e("FF", String.valueOf(cursor.getString(
                        cursor.getColumnIndex("data" + i)
                )));
            }
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            holder.nameField.setText(String.valueOf(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
            );
            Cursor phones = getPhones(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            );
            cursorOut(phones);
//            if(phones.moveToFirst()){
//                holder.phoneField.setText(String.valueOf(
//                        phones.getString(phones.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Phone.DATA
//                        ))
//                ));
////                phones.moveToFirst();
//                cursorOut(phones);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        TextView nameField;
        TextView phoneField;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.nameField);
            phoneField = itemView.findViewById(R.id.phoneField);
        }
    }
}
