package fr.gautrais.liste.model.entities;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

import fr.gautrais.liste.model.AppDatabase;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = CASCADE // si ListeEntry supprimée, supprimer les ListeItem associés
        )
)
public class ListEntry {
    @NonNull
    @PrimaryKey
    public String id;


    public String category_id;

    public String name;

    public boolean delete = false;

    public int order_num;





    public ListEntry(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }
    public ListEntry() {
        this.name = "";
        this.id = UUID.randomUUID().toString();
    }

    public void set_category(String uuid){
        this.category_id = uuid;
        update();
    }

    public void set_category(Category cat){
        this.category_id = cat.id;
        update();
    }


    public void update() {
        AppDatabase.getInstance().listeDao().update(this);
    }

}