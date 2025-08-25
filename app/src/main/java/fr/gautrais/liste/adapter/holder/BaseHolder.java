package fr.gautrais.liste.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.adapter.BaseAdapter;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{

    protected int mMode = 0;
    protected Object mParentObj;

    protected T mValue;

    public BaseHolder(@NonNull View itemView, BaseAdapter parent) {
        super(itemView);
        mParentObj = parent;
        mMode = parent.getMode();
    }
    public BaseHolder(@NonNull View itemView, Object parent, int select) {
        super(itemView);
        mParentObj = parent;
        mMode = select;
    }


    abstract public void set_data(int i);



}
