package fr.gautrais.liste;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.dao.ListEntryDao;
import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListEntryWithItems;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {


    ListEntryDao dao;

    List<ListEntry> liste;


    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            TextView.OnEditorActionListener {

        private ListEntry value;

        private ListAdapter parent;

        private List<ListEntry> dataset;

        private final TextView textView;

        private final ImageButton bt_supress;

        public ViewHolder(@NonNull View itemView, ListAdapter parent) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_title);
            textView.setOnClickListener(this);
            bt_supress = itemView.findViewById(R.id.btn_rm_list);
            bt_supress.setOnClickListener(this);
            this.parent = parent;
            dataset = parent.liste;

        }

        @Override
        public void onClick(View view) {
            if(view == bt_supress){

            }else if(view == textView){
                Intent i = new Intent(view.getContext(), EditListActivity.class);
                i.putExtra("id", value.id);
                view.getContext().startActivity(i);
            }
        }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            return false;
        }

        public void set_data(int position) {
            value = dataset.get(position);
            textView.setText(value.name);
        }
    }

    public ListAdapter(Context ctx){
        dao = AppDatabase.getInstance(ctx).listeDao();
        liste = dao.getAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);

        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set_data(position);
    }

    public void add() {
        add("");
    }

    public void add(String name) {
        ListEntry temp = new ListEntry(name);
        dao.insert(temp);
        Log.e("DB_TAG", "INsert text = "+temp.id);

        liste.add(dao.get(temp.id));
        notifyDataSetChanged();




    }

    @Override
    public int getItemCount() {
        return liste.size();
    }



}
