package fr.gautrais.liste;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.adapter.CategoryAdapter;
import fr.gautrais.liste.common.ui.EntryDialog;
import fr.gautrais.liste.databinding.ActivityListsBinding;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.entities.Category;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class ListsActivity extends AppCompatActivity implements View.OnClickListener {

    private CategoryAdapter mAadapter;

    private FloatingActionButton mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityListsBinding binding = ActivityListsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mBtnAdd =  this.findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);


        AppDatabase app = AppDatabase.getInstance(this);

        List<Category> dataset = app.categoryDao().getAll();
        mAadapter = new CategoryAdapter(this);


        setSupportActionBar(binding.toolbar);
        RecyclerView recyclerView = findViewById(R.id.rc_listes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAadapter);

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
                mAadapter.swapItems(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
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
                    mAadapter.onItemMoveFinal(fromPos, toPos);
                }
                fromPos = toPos = -1;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };


        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == mBtnAdd){
            EntryDialog.run(this, "Nom de la catégorie", new EntryDialog.ValidListener() {
                @Override
                public void onValid(EntryDialog dialog, String value) {
                    mAadapter.add(value);
                }
            });
        }
    }
}