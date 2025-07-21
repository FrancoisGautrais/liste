package fr.gautrais.liste;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.databinding.ActivityEditListBinding;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.entities.ListEntryWithItems;

public class EditListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppDatabase db;

    private AppBarConfiguration appBarConfiguration;
    private ActivityEditListBinding binding;

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        ListEntryWithItems current = AppDatabase.getInstance().listeDao().getListWithItems(id);
        current.sort();

        binding = ActivityEditListBinding.inflate(getLayoutInflater());
        itemAdapter = new ItemAdapter(this, current);
        setContentView(binding.getRoot());
        binding.toolbar.setTitle(current.liste.name);

        FloatingActionButton btn_add =  this.findViewById(R.id.btn_add_list);
        btn_add.setOnClickListener(this);


        setSupportActionBar(binding.toolbar);
        RecyclerView recyclerView = findViewById(R.id.rc_liste);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

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
                itemAdapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
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
                    itemAdapter.onItemMoveFinal(fromPos, toPos);
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
    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_edit_list);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
        return true;
    }

    @Override
    public void onClick(View view) {
        itemAdapter.insert_after();
    }
}