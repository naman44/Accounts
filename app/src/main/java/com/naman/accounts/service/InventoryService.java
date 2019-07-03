package com.naman.accounts.service;

import com.naman.accounts.Model.Item;
import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.ItemList;
import com.naman.accounts.Model.Manufacturing;
import com.naman.accounts.Model.Mapping;
import com.naman.accounts.Model.Vendor;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    private DatabaseAdapter db;

    public InventoryService(DatabaseAdapter db){
        this.db = db;
    }

    private void updateInventory(String itemName, double change){
        Item item = db.itemDao().fetchItemByName(itemName);
        double quantity = item.getQuantity() + change;
        item.setQuantity(quantity);
        db.itemDao().updateItem(item);
    }

    public void updateInventoryList(List<ItemList> itemList, int type){
        for(ItemList l : itemList){
            if(type == AppConstants.JOURNAL_TYPE_PURCHASE){
                updateInventory(l.getItemName(), l.getQuantity());
            }
            else
                updateInventory(l.getItemName(), l.getQuantity()*-1);
        }
    }

    public long insertJournalEntry(ItemJournal journal){
        int x = db.vendorDao().checkVendorName(journal.getVendorName());
        AccountService service = new AccountService(db);
        if(x <= 0){
            Vendor vendor = new Vendor();
            vendor.setVendorName(journal.getVendorName());
            service.createVendor(vendor);
        }
        return service.newJournalEntry(journal);
    }

    public void updateInventoryForManufacturing(Manufacturing manufacturing){
        updateInventory(manufacturing.getItemName(), manufacturing.getQuantity());
    }
}
