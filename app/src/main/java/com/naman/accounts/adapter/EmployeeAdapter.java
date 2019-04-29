package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naman.accounts.Model.Employee;
import com.naman.accounts.R;
import com.naman.accounts.diffUtils.EmployeeDiffUtil;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private Context mContext;
    private List<Employee> mList;
    private ListViewHolder.ItemClickListener listener;

    public EmployeeAdapter(Context context, List<Employee> list, ListViewHolder.ItemClickListener listener){
        mContext = context;
        mList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_list, parent, false);
        return new ListViewHolder(v, mContext, listener);
    }

    public void updateList(List<Employee> newList){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new EmployeeDiffUtil(newList, mList));
        mList.clear();
        mList.addAll(newList);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.setObj(mList.get(position));
    }
}
