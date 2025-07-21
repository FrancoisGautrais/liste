package fr.gautrais.liste.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Collections;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.holder.BaseHolder;
import fr.gautrais.liste.adapter.holder.ItemViewHolder;
import fr.gautrais.liste.model.entities.ListEntryWithItems;


public class ItemAdapter extends BaseAdapter<ListEntryWithItems, ItemViewHolder>{
    private final ListEntryWithItems mDataset;


    private boolean is_adding = false;

    // Interface pour support drag & drop
    public interface ItemTouchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);
    }

    public ListEntryWithItems getDataset() {
        return mDataset;
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataset String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ItemAdapter(Context ctx, ListEntryWithItems dataset) {
        super(ctx);
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)



    @Override
    protected ItemViewHolder _instanciate(@NonNull View view) {
        return new ItemViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)

    public void onBindViewHolder(ItemViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.set_data(position);
        if(is_adding){
            viewHolder.set_active();
            is_adding=false;
        }

    }


    public void insert_after(int i) {
        mDataset.add_item("", i+1);
        is_adding = true;
        notifyItemInserted(i+1);
        //notifyDataSetChanged();
    }

    public void insert_after() {
        insert_after(mDataset.items.size()-1);
    }

    public void on_remove(int i){
        mDataset.remove_item(mDataset.items.get(i));
        notifyItemRemoved(i);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.items.size();
    }


    @Override
    protected int _get_layout_id() {
        return R.layout.fragment_item;
    }

    // Swap temporaire utilisé pendant le déplacement
    public void swapItems(int fromPosition, int toPosition) {
        mDataset.items.get(fromPosition).order_num = toPosition;
        mDataset.items.get(fromPosition).update();
        mDataset.items.get(toPosition).order_num = fromPosition;
        mDataset.items.get(toPosition).update();
        Collections.swap(mDataset.items, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    // Appelée une seule fois à la fin du drag & drop
    public void onItemMoveFinal(int fromPosition, int toPosition) {
    }

}