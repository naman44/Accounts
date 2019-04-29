package com.naman.accounts.diffUtils;

import com.naman.accounts.Model.Attendance;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class AttendanceDiffUtil extends DiffUtil.Callback {

    private List<Attendance> oldList;
    private List<Attendance> newList;

    public AttendanceDiffUtil(List<Attendance> newList, List<Attendance> oldList){
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getEmpName().equalsIgnoreCase(oldList.get(oldItemPosition).getEmpName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Attendance a1 = oldList.get(oldItemPosition);
        Attendance a2 = newList.get(newItemPosition);

        return a1.getEmpName().equalsIgnoreCase(a2.getEmpName()) &&
                a1.getDate().equalsIgnoreCase(a2.getDate()) &&
                a1.getTimeIn().equalsIgnoreCase(a2.getTimeIn()) &&
                a1.getTimeOut().equalsIgnoreCase(a2.getTimeOut()) &&
                a1.getPresent() == a2.getPresent() &&
                a1.getAdvance() == a2.getAdvance();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
