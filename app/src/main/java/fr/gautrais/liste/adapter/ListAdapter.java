package fr.gautrais.liste.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.holder.BaseHolder;
import fr.gautrais.liste.adapter.holder.ListViewHolder;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.dao.ListEntryDao;
import fr.gautrais.liste.model.entities.Category;
import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListItemEntry;


public class ListAdapter extends BaseAdapter<ListEntry, ListViewHolder> {

    protected List<ListEntry> mDataset;
    ListEntryDao mDao;

    BaseAdapter mParent;

    public ListAdapter(Activity ctx) {
        super(ctx);
        mDao = AppDatabase.getInstance().listeDao();
        mDataset = mDao.getAll();
        Collections.sort(mDataset, Comparator.comparingInt(item -> item.order_num));
    }

    public ListAdapter(Activity ctx, List<ListEntry> lists) {
        super(ctx);
        mDao = AppDatabase.getInstance().listeDao();
        mDataset = lists;
        Collections.sort(mDataset, Comparator.comparingInt(item -> item.order_num));
    }
    public ListAdapter(Activity ctx, List<ListEntry> lists, int select) {
        super(ctx, select);
        mDao = AppDatabase.getInstance().listeDao();
        mDataset = lists;
        Collections.sort(mDataset, Comparator.comparingInt(item -> item.order_num));
    }

    public void setParent(BaseAdapter parent){
        mParent = parent;
    }


    public BaseAdapter getParent(){
        return mParent;
    }



    @Override
    protected ListViewHolder _instanciate(@NonNull View view) {
        return new ListViewHolder(view, this);
    }

    @Override
    protected int _get_layout_id() {
        return R.layout.fragment_list;
    }

    public List<ListEntry> getDataset(){
        return mDataset;
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public ListEntry add() {
        return add("", null);
    }

    public ListEntry add(String name) {
        return add(name, null);
    }

    public ListEntry add(String name, Category cat) {
        ListEntry temp = new ListEntry(name);
        temp.category_id = (cat!=null?cat.id:null);
        mDao.insert(temp);
        mDataset.add(mDao.get(temp.id));
        notifyDataSetChanged();
        return temp;
    }


    public void on_remove(int position) {

        ListEntry curr = mDataset.get(position);
        mDataset.remove(position);

        for(int j=position+1; j<mDataset.size(); j++){
            mDataset.get(j).order_num--;
            mDataset.get(j).update();
        }
        mDao.delete(curr.id);
        notifyItemRemoved(position);
    }

    public void swapItems(int fromPosition, int toPosition) {
        mDataset.get(fromPosition).order_num = toPosition;
        mDataset.get(fromPosition).update();
        mDataset.get(toPosition).order_num = fromPosition;
        mDataset.get(toPosition).update();
        Collections.swap(mDataset, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }
    public void onItemMoveFinal(int fromPos, int toPos) {
    }
}
