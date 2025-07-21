package fr.gautrais.liste;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

import fr.gautrais.liste.model.entities.ListEntryWithItems;
import fr.gautrais.liste.model.entities.ListItemEntry;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ListEntryWithItems liste;


    private boolean is_adding = false;

    // Interface pour support drag & drop
    public interface ItemTouchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnFocusChangeListener,
            TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

        private final TextView textView;
        private final CheckBox checkBox;
        private final EditText entry;

        private boolean is_active = false;

        private final ItemAdapter parent;

        private final ImageButton remove_btn;

        private final ListEntryWithItems liste_parent;

        private ListItemEntry value;






        public ViewHolder(View view, ItemAdapter p) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.tv_text);
            textView.setOnClickListener(this);
            checkBox = (CheckBox) view.findViewById(R.id.cb_checked);
            checkBox.setOnCheckedChangeListener(this);
            entry = (EditText) view.findViewById(R.id.et_text);
            remove_btn = view.findViewById(R.id.btn_delete);
            remove_btn.setOnClickListener(this);
            entry.setOnFocusChangeListener(this);
            entry.setOnEditorActionListener(this);
            parent = p;
            liste_parent = parent.getData();
        }

        public int get_position() {
            return liste_parent.items.indexOf(value);
        }

        public void set_data(int i){
            ListItemEntry content = liste_parent.items.get(i);
            value = content;
            textView.setText(content.content);
            entry.setText(content.content);
            checkBox.setChecked(value.checked);
            this._update();

        }

        @Override
        public void onClick(View view) {
            if(view == textView){
                is_active = !is_active;
                this._update();
                this.entry.requestFocus();
                entry.setSelection(entry.getText().length());
            }else if(view == remove_btn){
                parent.on_remove(get_position());
            }
        }

        private void _update(){

            if(is_active){
                textView.setVisibility(View.GONE);
                entry.setVisibility(View.VISIBLE);
                remove_btn.setVisibility(View.VISIBLE);
            }else{
                textView.setVisibility(View.VISIBLE);
                entry.setVisibility(View.GONE);
                remove_btn.setVisibility(View.GONE);
            }
        }

        public void  set_active(){
            is_active = true;
            this._update();
            this.entry.requestFocus();
            entry.setSelection(entry.getText().length());
        }

        public void on_valid(){
            value.content = entry.getText().toString();
            value.update();
            textView.setText(value.content);
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if(b) return;;
            is_active = false;
            this._update();
            on_valid();
        }

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && event.getAction() == KeyEvent.ACTION_DOWN)) {
                parent.insert_after(get_position());


                return false;
            }
            return false;
        }

        @Override
        public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
            value.checked = b;
            value.update();
        }
    }

    public ListEntryWithItems getData() {
        return liste;
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ItemAdapter(ListEntryWithItems dataSet) {
        liste = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_item, viewGroup, false);

        return new ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.set_data(position);
        if(is_adding){
            viewHolder.set_active();
            is_adding=false;
        }

    }


    public void insert_after(int i) {
        liste.add_item("", i+1);
        is_adding = true;
        notifyItemInserted(i+1);
        //notifyDataSetChanged();
    }

    public void insert_after() {
        insert_after(liste.items.size()-1);
    }

    public void on_remove(int i){
        liste.remove_item(liste.items.get(i));
        notifyItemRemoved(i);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return liste.items.size();
    }

    // Swap temporaire utilisé pendant le déplacement
    public void swapItems(int fromPosition, int toPosition) {
        liste.items.get(fromPosition).order_num = toPosition;
        liste.items.get(fromPosition).update();
        liste.items.get(toPosition).order_num = fromPosition;
        liste.items.get(toPosition).update();
        Collections.swap(liste.items, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    // Appelée une seule fois à la fin du drag & drop
    public void onItemMoveFinal(int fromPosition, int toPosition) {
    }
}