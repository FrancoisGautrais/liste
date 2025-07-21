package fr.gautrais.liste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.adapter.holder.BaseHolder;


public abstract class BaseAdapter<T, E extends BaseHolder > extends RecyclerView.Adapter<E> {


    protected Context mContext;

    public BaseAdapter(Context ctx){
        mContext = ctx;
    }



    @NonNull
    @Override
    public E onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(_get_layout_id(), parent, false);

        return _instanciate(view);
    }

    abstract protected E _instanciate(@NonNull View view);


    @Override
    public void onBindViewHolder(@NonNull E holder, int position) {
        holder.set_data(position);
    }



    abstract protected int _get_layout_id();




}




