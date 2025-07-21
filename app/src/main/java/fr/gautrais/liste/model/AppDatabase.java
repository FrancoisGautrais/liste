package fr.gautrais.liste.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.gautrais.liste.model.dao.CategoryDao;
import fr.gautrais.liste.model.dao.ListEntryDao;
import fr.gautrais.liste.model.dao.ListItemEntryDao;
import fr.gautrais.liste.model.entities.Category;
import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListItemEntry;

@Database(entities = {ListItemEntry.class, ListEntry.class, Category.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract ListItemEntryDao listeItemDao();
    public abstract ListEntryDao listeDao();
    public abstract CategoryDao categoryDao();


    public static AppDatabase getInstance() {
        return getInstance(null);
    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "liste_database"
                    ).allowMainThreadQueries() // ❗️OK pour démo, éviter en prod
                    .build();
        }
        return INSTANCE;
    }
}