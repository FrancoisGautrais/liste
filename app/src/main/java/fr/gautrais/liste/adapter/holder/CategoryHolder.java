package fr.gautrais.liste.adapter.holder;
import fr.gautrais.liste.adapter.Const;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.CategoryAdapter;
import fr.gautrais.liste.adapter.ListAdapter;
import fr.gautrais.liste.common.ui.EntryDialog;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.entities.Category;
import fr.gautrais.liste.model.entities.ListEntry;

public class CategoryHolder   extends BaseHolder<Category> implements View.OnClickListener {

    private final ImageButton mArrow;
    private final TextView mTextView;
    private final ImageButton mBtnAdd;
    private final ImageButton mBtnSettings;

    private final RecyclerView mRecycler;
    protected List<Category> mDataset;
    protected List<ListEntry> mLists = null;

    private CategoryAdapter mParent;

    protected ListAdapter mAdapter;

    protected boolean mVisible = false;

    public CategoryHolder(@NonNull View view, CategoryAdapter parent){
        super(view, parent);
        mParent = parent;
        mDataset = parent.getDataset();
        mArrow = view.findViewById(R.id.ib_arrow);
        mBtnAdd = view.findViewById(R.id.ib_add);
        mBtnSettings = view.findViewById(R.id.ib_settings);
        mTextView = view.findViewById(R.id.tv_category);
        mRecycler = view.findViewById(R.id.rc_listes);

        if(mMode != Const.MODE_SELECT_NONE){
            mBtnAdd.setVisibility(View.GONE);
            mBtnSettings.setVisibility(View.GONE);

        }else{
            mBtnAdd.setOnClickListener(this);
            mBtnSettings.setOnClickListener(this);
        }

        mTextView.setOnClickListener(this);
        mArrow.setOnClickListener(this);


    }

    void expand(){

        mVisible = true;
        mArrow.setBackgroundResource(R.drawable.arrow_bottom);
        mRecycler.setVisibility(View.VISIBLE);
    }

    void collapse(){

        mVisible = false;
        mArrow.setBackgroundResource(R.drawable.arrow_right);
        mRecycler.setVisibility(View.GONE);
    }

    void toggleExpand(){
        if(mVisible){
            collapse();
        }else{
            expand();
        }
    }

    @Override
    public void set_data(int i) {
        mValue = mDataset.get(i);
        mTextView.setText(mValue.name);

        if(mLists==null){
            mLists = AppDatabase.getInstance().listeDao().get_from_category(mValue.id);
        }
        if(mAdapter == null) {
            mAdapter = new ListAdapter(mParent.getActivity(), mLists, mMode);
            mAdapter.setParent(mParent);

            mRecycler.setLayoutManager(new LinearLayoutManager(mRecycler.getContext()));
            mRecycler.setAdapter(mAdapter);
            if (!mVisible) {
                collapse();
            }


            ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    0  // pas de swipe
            ) {
                private int fromPos = -1;
                private int toPos = -1;

                @Override
                public boolean onMove(RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    if (fromPos == -1) {
                        fromPos = viewHolder.getBindingAdapterPosition();
                    }
                    toPos = target.getBindingAdapterPosition();

                    // Affichage visuel seulement (swap temporaire)
                    mAdapter.swapItems(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // Pas de swipe
                }

                @Override
                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);
                    if (fromPos != -1 && toPos != -1 && fromPos != toPos) {
                        mAdapter.onItemMoveFinal(fromPos, toPos);
                    }
                    fromPos = toPos = -1;
                }

                @Override
                public boolean isLongPressDragEnabled() {
                    return true;
                }
            };


            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecycler);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == mTextView){
            toggleExpand();

        }else if (view == mBtnAdd){
            expand();
            EntryDialog.run(mArrow.getContext(), "Nom de la liste", new EntryDialog.ValidListener() {
                @Override
                public void onValid(EntryDialog dialog, String value) {
                    mAdapter.add(value, mValue);
                }
            });
        }else if(view == mBtnSettings){
            PopupMenu popup = new PopupMenu(mBtnAdd.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.category_menu, popup.getMenu());
            final int mv = R.id.item_rename;
            final int rm = R.id.item_rm;
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == mv){
                    rename_dialog();
                    return true;
                }else if (id==rm){
                    remove_dialog();
                    return true;
                }
                return false;
            });

            popup.show();
        }else if(view == mArrow){
            toggleExpand();
        }

    }

    private void rename_dialog(){
        EntryDialog.run(mArrow.getContext(), "Renommer", new EntryDialog.ValidListener() {
            @Override
            public void onValid(EntryDialog dialog, String value) {
                mValue.name = value;
                mValue.update();
                mTextView.setText(value);
            }
        });
    }
    public int get_position() {
        return mDataset.indexOf(mValue);
    }
    private void remove_dialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(mArrow.getContext());
        builder.setMessage("Voulez vous vraiment supprimer la catégorie '"+mValue.name+"'")
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

}
