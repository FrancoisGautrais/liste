package fr.gautrais.liste.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Category {

    @NonNull
    @PrimaryKey
    public String id;

    public String name;

    public boolean delete = false;

    public int order_num;



    public Category(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }
    public Category() {
        this.name = "";
        this.id = UUID.randomUUID().toString();
    }



}
