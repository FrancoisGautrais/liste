package fr.gautrais.liste.model.dao;


import android.graphics.pdf.models.ListItem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListItemEntry;

@Dao
public interface ListItemEntryDao {
    @Insert
    void insert(ListItemEntry item);

    @Query("DELETE FROM ListItemEntry where id = :id")
    void delete(String id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ListItemEntry item);

    @Query("SELECT * FROM ListItemEntry")
    List<ListItemEntry> getAll();

    @Query("DELETE FROM ListItemEntry")
    void deleteAll();

    @Query("SELECT * FROM ListItemEntry WHERE list_id = :listeId ORDER BY order_num ASC")
    List<ListItemEntry> getItemsForListOrdered(String listeId);
}