package com.naman.accounts.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.AddItemActivity;

import java.util.List;

public class ItemListViewModel extends AndroidViewModel {

    private LiveData<List<Item>> itemsList;
    private DatabaseAdapter appDatabase;
    private Item item;

    public ItemListViewModel(Application application){
        super(application);
        appDatabase = DatabaseAdapter.getInstance(application);
        itemsList = appDatabase.itemDao().fetchItemsLive();
    }

    public LiveData<List<Item>> getItemsList(){
        return itemsList;
    }

    public void upSertItem(Item item){
        this.item = item;
        new AsyncTaskItem().execute("Upsert");
    }

    public void deleteItem(Item item){
        this.item = item;
        new AsyncTaskItem().execute("delete");
    }

    class AsyncTaskItem extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            if(strings[0].equalsIgnoreCase("Upsert")){
                Item i = appDatabase.itemDao().fetchItemByName(item.getItemName());
                if(i != null){
                    appDatabase.itemDao().updateItem(item);
                }
                else{
                    appDatabase.itemDao().insertItem(item);
                }
            }
            else if(strings[0].equalsIgnoreCase("delete")){
                appDatabase.itemDao().deleteItem(item);
            }
            return null;
        }
    }
}
