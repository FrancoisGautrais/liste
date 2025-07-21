package fr.gautrais.liste.adapter.holder;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import fr.gautrais.liste.EditListActivity;
import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.adapter.ListAdapter;
import fr.gautrais.liste.model.entities.ListEntry;

public class ListViewHolder extends BaseHolder<ListEntry> implements
        View.OnClickListener{

    private final TextView textView;

    private final ImageButton bt_supress;

    protected List<ListEntry> mDataset;

    protected ListAdapter mParent;

    public ListViewHolder(@NonNull View itemView, ListAdapter parent) {
        super(itemView, parent);
        mParent = parent;
        mDataset = parent.getDataset();
        textView = itemView.findViewById(R.id.tv_title);
        textView.setOnClickListener(this);
        bt_supress = itemView.findViewById(R.id.btn_rm_list);
        bt_supress.setOnClickListener(this);
    }

    public int get_position() {
        return mDataset.indexOf(mValue);
    }


    @Override
    public void onClick(View view) {
        if(view == bt_supress){
            suppress_dialog();
        }else if(view == textView){
            Intent i = new Intent(view.getContext(), EditListActivity.class);
            i.putExtra("id", mValue.id);
            view.getContext().startActivity(i);
        }
    }


    protected void suppress_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(bt_supress.getContext());
        builder.setMessage("Voulez vous vraiment supprimer la liste '"+mValue.name+"'")
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mParent.on_remove(get_position());
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });
        // Create the AlertDialog object and return it.
        builder.create().show();
    }


    @Override
    public void set_data(int position) {
        mValue = mDataset.get(position);
        textView.setText(mValue.name);
    }
}
