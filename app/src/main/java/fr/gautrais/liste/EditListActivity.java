package fr.gautrais.liste;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import fr.gautrais.liste.databinding.ActivityEditListBinding;

public class EditListActivity extends AppCompatActivity {



    private AppBarConfiguration appBarConfiguration;
    private ActivityEditListBinding binding;

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate: Slaut");
        ArrayList<MyElem> dataset = new ArrayList<MyElem>();
        dataset.add(new MyElem("Pomme"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Banane"));
        dataset.add(new MyElem("Kiwi"));
        itemAdapter = new ItemAdapter(dataset);
        binding = ActivityEditListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        RecyclerView recyclerView = findViewById(R.id.rc_liste);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_edit_list);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}