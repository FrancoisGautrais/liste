package fr.gautrais.liste;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import fr.gautrais.liste.adapter.CategoryAdapter;
import fr.gautrais.liste.adapter.Const;
import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.databinding.ActivityListsBinding;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.entities.Category;

public class SelectListActivity extends AppCompatActivity {


    private ItemAdapter mItemAdapter;

    private CategoryAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_list);
        MaterialToolbar tb = findViewById(R.id.toolbar);

        AppDatabase app = AppDatabase.getInstance(this);
        List<Category> dataset = app.categoryDao().getAll();
        mAdapter = new CategoryAdapter(this, Const.MODE_SELECT_ITEM);


        setSupportActionBar(tb);
        tb.setTitle("Importer depuis une liste");

        RecyclerView recyclerView = findViewById(R.id.rc_listes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter.onActivityStarted();
    }


}