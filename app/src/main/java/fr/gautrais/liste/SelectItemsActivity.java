package fr.gautrais.liste;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.gautrais.liste.adapter.Const;
import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.model.AppDatabase;
import fr.gautrais.liste.model.entities.ListEntryWithItems;
import fr.gautrais.liste.model.entities.ListItemEntry;

public class SelectItemsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnImport;

    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_items);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        ListEntryWithItems current = AppDatabase.getInstance().listeDao().getListWithItems(id);
        current.sort();

        mItemAdapter = new ItemAdapter(this, current, Const.MODE_SELECT_ITEM);
        MaterialToolbar tb = findViewById(R.id.toolbar);
        mBtnImport = findViewById(R.id.btn_import);
        setSupportActionBar(tb);


        mRecyclerView = findViewById(R.id.rc_liste);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mItemAdapter);

        mBtnImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ListEntryWithItems data = mItemAdapter.getDataset();
        JSONArray jsonArray = new JSONArray();
        for(ListItemEntry entry : data.items){
            if(entry.checked){
                jsonArray.put(entry.content);
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("value", jsonArray.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();



    }

    @Override
    protected void onStart() {
        super.onStart();
        mItemAdapter.onActivityStarted();
    }

}