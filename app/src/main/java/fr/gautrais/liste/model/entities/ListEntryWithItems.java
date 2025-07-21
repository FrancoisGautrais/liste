package fr.gautrais.liste.model.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.gautrais.liste.model.AppDatabase;

public class ListEntryWithItems {
    @Embedded
    public ListEntry liste;

    @Relation(
            parentColumn = "id",
            entityColumn = "list_id"

    )
    public List<ListItemEntry> items;


    public void add_item(String item){
        add_item(item, -1);
    }
    public void add_item(String item, int order){
        ListItemEntry entry = new ListItemEntry(item);
        if(order>0){
            entry.order_num = order;
            for(int i=order; i<items.size(); i++){
                items.get(i).order_num++;
                items.get(i).update();
            }
        }else{
            entry.order_num = items.size();
        }
        add_item(entry);
    }

    public void add_item(ListItemEntry entry){
        entry.list_id = liste.id;
        AppDatabase.getInstance().listeItemDao().insert(entry);
        items.add(entry.order_num, entry);
    }


    public ListEntryWithItems sort(){
        Collections.sort(items, Comparator.comparingInt(item -> item.order_num));
        return this;
    }


    public void remove_item(ListItemEntry entry){
        remove_item(entry.id);
    }

    public void remove_item(String id){
        ListItemEntry curr = null;
        int i = 0;
        boolean found = false;
        for(; i<items.size(); i++){
            if(items.get(i).id.compareTo(id)==0){
                items.remove(i);
                found = true;
                break;
            }
        }

        if(!found) return;

        for(int j=i+1; j<items.size(); j++){
            items.get(j).order_num--;
            items.get(j).update();
        }


        AppDatabase.getInstance().listeItemDao().delete(id);


    }



    public void update() {
        AppDatabase.getInstance().listeDao().update(this.liste);
    }
}
