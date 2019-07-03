package com.naman.accounts.adapter;

import android.media.SubtitleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SubJournalListAdapter extends ListAdapter<SubTransaction, SubJournalListAdapter.SubJournalListHolder> {

    class SubJournalListHolder extends RecyclerView.ViewHolder{

        private TextView attName, timeIn, timeOut;

        SubJournalListHolder(View view){
            super(view);
            attName = itemView.findViewById(R.id.att_name);
            timeIn = itemView.findViewById(R.id.time_in);
            timeOut = itemView.findViewById(R.id.time_out);
        }

        void setObj(SubTransaction t){
            attName.setText(t.getName());
            timeIn.setText(String.valueOf(t.getAmount()));
        }
    }

    public SubJournalListAdapter(){
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull SubJournalListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    @NonNull
    @Override
    public SubJournalListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendance_list, parent, false);
        return new SubJournalListHolder(v);
    }

    private static final DiffUtil.ItemCallback<SubTransaction> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SubTransaction>() {

                @Override
                public boolean areItemsTheSame(@NonNull SubTransaction oldItem, @NonNull SubTransaction newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull SubTransaction oldItem, @NonNull SubTransaction newItem) {
                    return false;
                }
            };

}
