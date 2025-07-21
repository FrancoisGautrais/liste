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

import fr.gautrais.liste.model.entities.Category;


@Dao
public interface CategoryDao {
    @Insert
    void insert(Category item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category item);

    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Query("DELETE FROM Category")
    void deleteAll();

    @Query("DELETE FROM Category where id = :id")
    void delete(String id);



    @Transaction
    @Query("SELECT * FROM Category WHERE id = :id")
    Category getListWithItems(String id);

    @Transaction
    @Query("SELECT * FROM Category")
    List<Category> getAllListWith();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category get(String id);
}