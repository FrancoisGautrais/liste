package fr.gautrais.liste.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import fr.gautrais.liste.adapter.holder.BaseHolder;


public abstract class BaseAdapter<T, E extends BaseHolder > extends RecyclerView.Adapter<E>{

    protected boolean mActivityStarted = false;
    private int mMode = 0;
    protected Activity mActivity;

    ActivityResultLauncher<Intent> mActivityLauncher;

    public BaseAdapter(Activity ctx){
        mActivity = ctx;
    }
    public BaseAdapter(Activity ctx, int select){
        mActivity = ctx;
        mMode = select;
    }

    public int getMode(){
        return mMode;
    }


    public Activity getActivity(){
        return mActivity;
    }



    @NonNull
    @Override
    public E onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(_get_layout_id(), parent, false);

        return _instanciate(view);
    }

    abstract protected E _instanciate(@NonNull View view);


    @Override
    public void onBindViewHolder(@NonNull E holder, int position) {

        holder.set_data(position);
    }



    abstract protected int _get_layout_id();

    public void onActivityStarted() {
        mActivityStarted = true;
        AppCompatActivity activity = (AppCompatActivity) mActivity;

        mActivityLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                   this.onActivityResult(result);
                });
    }


    public void onActivityResult(ActivityResult result){
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                mActivity.setResult(Activity.RESULT_OK, data);
                mActivity.finish();
            }
        }
    }
    public void launchctivity(Intent i){
        mActivityLauncher.launch(i);
    }


}




