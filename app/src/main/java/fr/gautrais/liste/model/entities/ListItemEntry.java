package fr.gautrais.liste.model.entities;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import fr.gautrais.liste.model.AppDatabase;

@Entity(
        foreignKeys = @ForeignKey(
                entity = ListEntry.class,
                parentColumns = "id",
                childColumns = "list_id",
                onDelete = CASCADE // si ListeEntry supprimée, supprimer les ListeItem associés
        )
)
public class ListItemEntry {
    @NonNull
    @PrimaryKey
    public String id;

    public String content;

    public boolean delete = false;

    public int order_num;

    public boolean checked;

    public int last_edite;

    public String list_id;


    public ListItemEntry(String content) {

        this.content = content;
        this.id = UUID.randomUUID().toString();
    }
    public ListItemEntry() {
        this.content = "";
        this.id = UUID.randomUUID().toString();
    }

    public void update() {
        AppDatabase.getInstance().listeItemDao().update(this);
    }
}


