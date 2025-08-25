package fr.gautrais.liste;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.common.ActivityResultManager;
import fr.gautrais.liste.common.ui.EntryDialog;
import fr.gautrais.liste.databinding.ActivityEditListBinding;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.dao.ListItemEntryDao;
import fr.gautrais.liste.model.entities.ListEntry;
import fr.gautrais.liste.model.entities.ListEntryWithItems;
import fr.gautrais.liste.model.entities.ListItemEntry;

public class EditListActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityEditListBinding mBinding;
    private ActivityResultLauncher<Intent> myActivityLauncher;
    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        ListEntryWithItems current = AppDatabase.getInstance(this).listeDao().getListWithItems(id);
        current.sort();

        mBinding = ActivityEditListBinding.inflate(getLayoutInflater());
        mItemAdapter = new ItemAdapter(this, current);
        setContentView(mBinding.getRoot());
        mBinding.toolbar.setTitle(current.liste.name);

        FloatingActionButton btn_add =  this.findViewById(R.id.btn_add_list);
        btn_add.setOnClickListener(this);


        setSupportActionBar(mBinding.toolbar);
        RecyclerView recyclerView = findViewById(R.id.rc_liste);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mItemAdapter);

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
                mItemAdapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
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
                    mItemAdapter.onItemMoveFinal(fromPos, toPos);
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


        myActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String value = data.getStringExtra("value");
                            try {
                                JSONArray js = new JSONArray(value);
                                for(int i=0; i<js.length(); i++){
                                    String text =js.get(i).toString();
                                    mItemAdapter.insert_after(text);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                });



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
        mItemAdapter.insert_after();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        String idlist = mItemAdapter.getDataset().liste.id;

        if (id == R.id.importer) {
            Intent i = new Intent(this, SelectListActivity.class);
            i.putExtra("dst", idlist);
            myActivityLauncher.launch(i);
        } else if (id == R.id.menu_clear){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Voulez vous vraiment vider la liste ?")
                    .setPositiveButton("Vider", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mItemAdapter.clear();
                        }
                    }) ;

            builder.create().show();

        }

        return super.onOptionsItemSelected(item);
    }


}