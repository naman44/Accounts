package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.R;
import java.util.List;

public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.TradeListHolder> {

    private List<ItemJournal> list;
    private Context mContext;

    class TradeListHolder extends RecyclerView.ViewHolder{

        private TextView name, salary, advance, rest, id, paidTxt;

        TradeListHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.salary_list_name);
            salary = itemView.findViewById(R.id.salary_list_amount);
            advance = itemView.findViewById(R.id.salary_list_advance);
            rest = itemView.findViewById(R.id.salary_list_rest);
            id = itemView.findViewById(R.id.text_first);
        }

        void setObj(ItemJournal obj){
            name.setText(obj.getVendorName());
            salary.setText(mContext.getString(R.string.salary_placeholder, obj.getAmountWithGst()));
            advance.setText(obj.getDate());
            rest.setText(obj.getEntryType() + "");
            id.setText(String.valueOf(obj.getId()));
        }
    }

    public TradeListAdapter(Context context, List<ItemJournal> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TradeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_salary_list, parent, false);
        return new TradeListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeListHolder holder, int position) {
        holder.setObj(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        else
            return list.size();
    }

    public void updateList(List<ItemJournal> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
