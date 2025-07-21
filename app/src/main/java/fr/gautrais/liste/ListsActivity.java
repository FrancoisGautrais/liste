package fr.gautrais.liste;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.common.ui.EntryDialog;
import fr.gautrais.liste.databinding.ActivityListsBinding;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.dao.ListEntryDao;
import fr.gautrais.liste.model.entities.ListEntryWithItems;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class ListsActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityListsBinding binding;

    private ListAdapter adapter;

    private FloatingActionButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityListsBinding binding = ActivityListsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btn_add =  this.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);


        AppDatabase app = AppDatabase.getInstance(this);

        List<ListEntryWithItems> dataset = app.listeDao().getAllListWith();
        adapter = new ListAdapter(this);


        setSupportActionBar(binding.toolbar);
        RecyclerView recyclerView = findViewById(R.id.rc_listes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
        if(view == btn_add){
            EntryDialog.run(this, "Nom de la liste", new EntryDialog.ValidListener() {
                @Override
                public void onValid(EntryDialog dialog, String value) {
                    adapter.add(value);
                }
            });
        }
    }
}