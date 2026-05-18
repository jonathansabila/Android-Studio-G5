package com.example.dietreport;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private final ArrayList<Record> records;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecordAdapter(Context context, ArrayList<Record> records, OnItemClickListener listener) {
        this.context = context;
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = records.get(position);

        // Build display info string
        String info = "Name: " + record.getName() + "\n" +
                "Age: " + record.getAge() + "\n" +
                "Room: " + record.getRoom() + "\n" +
                "Date: " + record.getDateTime() + "\n" +
                "Religion: " + record.getReligion() + "\n" +
                "Diet: " + record.getDiet();

        holder.txtInfo.setText(info);

        // Set signature image
        if (record.getSignature() != null && record.getSignature().length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(record.getSignature(), 0, record.getSignature().length);
            holder.imgSignature.setImageBitmap(bmp);
        } else {
            holder.imgSignature.setImageResource(R.drawable.placeholder_signature);
        }

        // Click item to view details
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));

        // Delete button logic
        holder.btnDelete.setOnClickListener(v -> {
            EditText input = new EditText(context);
            new AlertDialog.Builder(context)
                    .setTitle("Admin Authentication")
                    .setMessage("Enter admin password to delete")
                    .setView(input)
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        String pwd = input.getText().toString();
                        if ("1234".equals(pwd)) { // admin password
                            if (context instanceof ViewRecordsActivity) {
                                ((ViewRecordsActivity) context).deleteRecord(record.getId());
                            }
                        } else {
                            Toast.makeText(context, "Wrong admin password", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;
        ImageView imgSignature;
        Button btnDelete;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            imgSignature = itemView.findViewById(R.id.imgSignature);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}