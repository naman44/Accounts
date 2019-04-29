package com.naman.accounts.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman.accounts.Model.Attendance;
import com.naman.accounts.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceListAdapter extends ListAdapter<Attendance, AttendanceListAdapter.AttendanceListHolder> {

    class AttendanceListHolder extends RecyclerView.ViewHolder {

        private TextView attName, attAdvance, timeIn, timeOut;

        AttendanceListHolder(View itemView) {
            super(itemView);
            attName = itemView.findViewById(R.id.att_name);
            attAdvance = itemView.findViewById(R.id.att_advance);
            timeIn = itemView.findViewById(R.id.time_in);
            timeOut = itemView.findViewById(R.id.time_out);
        }

        void setObj(Attendance a){
            attName.setText(a.getEmpName());
            attAdvance.setText(a.getAdvance() + "");
            timeIn.setText(a.getTimeIn());
            timeOut.setText(a.getTimeOut());
            if(a.getPresent() == 0){
                attName.setTextColor(Color.RED);
            }
            else
                attName.setTextColor(Color.BLACK);
        }
    }

    public AttendanceListAdapter(){
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Attendance> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Attendance>() {
                @Override
                public boolean areItemsTheSame(@NonNull Attendance oldItem, @NonNull Attendance newItem) {
                    return oldItem.getEmpName().equalsIgnoreCase(newItem.getEmpName()) &&
                            oldItem.getDate().equalsIgnoreCase(newItem.getDate());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Attendance oldItem, @NonNull Attendance newItem) {
                    return oldItem.getAdvance() == newItem.getAdvance() &&
                            oldItem.getPresent() == newItem.getPresent() &&
                            oldItem.getTimeIn().equalsIgnoreCase(newItem.getTimeIn()) &&
                            oldItem.getTimeOut().equalsIgnoreCase(newItem.getTimeOut()) &&
                            oldItem.getDate().equalsIgnoreCase(newItem.getDate()) &&
                            oldItem.getEmpName().equalsIgnoreCase(newItem.getEmpName());
                }
            };

    @NonNull
    @Override
    public AttendanceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendance_list, parent, false);
        return new AttendanceListHolder(v);
    }
}
