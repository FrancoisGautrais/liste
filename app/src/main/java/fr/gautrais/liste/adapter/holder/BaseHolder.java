package fr.gautrais.liste.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.BaseAdapter;
import fr.gautrais.liste.adapter.ListAdapter;
import fr.gautrais.liste.model.entities.ListItemEntry;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{


    protected Object mParentObj;

    protected T mValue;

    public BaseHolder(@NonNull View itemView, Object parent) {
        super(itemView);
        mParentObj = parent;
    }


    abstract public void set_data(int i);



}
