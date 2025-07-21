package fr.gautrais.liste.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.holder.BaseHolder;
import fr.gautrais.liste.adapter.holder.CategoryHolder;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.dao.CategoryDao;
import fr.gautrais.liste.model.dao.ListEntryDao;
import fr.gautrais.liste.model.entities.Category;
import fr.gautrais.liste.model.entities.ListEntry;

public class CategoryAdapter extends BaseAdapter<Category, CategoryHolder> {

    protected List<Category> mDataset;
    CategoryDao mDao;

    public CategoryAdapter(Context ctx) {
        super(ctx);
        mDao = AppDatabase.getInstance().categoryDao();
        mDataset = mDao.getAll();
        Collections.sort(mDataset, Comparator.comparingInt(item -> item.order_num));
    }

    @Override
    protected CategoryHolder _instanciate(@NonNull View view) {
        return new CategoryHolder(view, this);
    }

    @Override
    protected int _get_layout_id() {
        return R.layout.fragment_category;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public List<Category> getDataset(){
        return mDataset;
    }

    public void add(String name) {
        Category temp = new Category(name);
        mDao.insert(temp);
        mDataset.add(mDao.get(temp.id));
        notifyDataSetChanged();
    }


    public void on_remove(int position) {
        Category curr = mDataset.get(position);
        mDataset.remove(position);

        for(int j=position+1; j<mDataset.size(); j++){
            mDataset.get(j).order_num--;
            mDataset.get(j).update();
        }
        mDao.delete(curr.id);
        notifyItemRemoved(position);
    }

    public void onItemMoveFinal(int fromPos, int toPos) {
    }

    public void swapItems(int fromPosition, int toPosition) {
        mDataset.get(fromPosition).order_num = toPosition;
        mDataset.get(fromPosition).update();
        mDataset.get(toPosition).order_num = fromPosition;
        mDataset.get(toPosition).update();
        Collections.swap(mDataset, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }
}
