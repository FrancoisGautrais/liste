package fr.gautrais.liste.model.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListEntryWithItems;

@Dao
public interface ListEntryDao {
    @Insert
    void insert(ListEntry item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ListEntry item);

    @Query("SELECT * FROM ListEntry")
    List<ListEntry> getAll();

    @Query("DELETE FROM ListEntry")
    void deleteAll();

    @Query("DELETE FROM ListEntry where id = :id")
    void delete(String id);



    @Transaction
    @Query("SELECT * FROM ListEntry WHERE id = :id")
    ListEntryWithItems getListWithItems(String id);

    @Transaction
    @Query("SELECT * FROM ListEntry")
    List<ListEntryWithItems> getAllListWith();

    @Query("SELECT * FROM ListEntry WHERE id = :id")
    ListEntry get(String id);
}