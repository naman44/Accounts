package com.naman.accounts.diffUtils;

import com.naman.accounts.Model.Employee;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class EmployeeDiffUtil  extends DiffUtil.Callback{

    private List<Employee> oldList;
    private List<Employee> newList;

    public EmployeeDiffUtil(List<Employee> newList, List<Employee> oldList){
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
        Employee e1 = oldList.get(oldItemPosition);
        Employee e2 = newList.get(newItemPosition);
        return e1.getEmpName().equalsIgnoreCase(e2.getEmpName()) &&
                e1.getDateOfJoin().equalsIgnoreCase(e2.getDateOfJoin()) &&
                e1.getLastDate().equalsIgnoreCase(e2.getLastDate()) &&
                e1.getEmpSalary() == e2.getEmpSalary();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
