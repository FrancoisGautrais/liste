package fr.gautrais.liste.adapter.holder;


import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import fr.gautrais.liste.R;
import fr.gautrais.liste.adapter.BaseAdapter;
import fr.gautrais.liste.adapter.ItemAdapter;
import fr.gautrais.liste.model.entities.ListEntryWithItems;
import fr.gautrais.liste.model.entities.ListItemEntry;

public class ItemViewHolder extends BaseHolder<ListItemEntry> implements
        View.OnClickListener,
        View.OnFocusChangeListener,
        TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

    private final TextView mTextView;
    private final CheckBox mCheckBox;
    private final EditText mEntry;

    private boolean mIsActive = false;

    private final ImageButton mRemoveBtn;

    private ListEntryWithItems mDataset;



    protected ItemAdapter mParent;








    public ItemViewHolder(@NonNull View view, ItemAdapter parent) {
        super(view, parent);
        mParent = parent;
        mDataset = parent.getDataset();
        // Define click listener for the ViewHolder's View

        mTextView = (TextView) view.findViewById(R.id.tv_text);
        mTextView.setOnClickListener(this);
        mCheckBox = (CheckBox) view.findViewById(R.id.cb_checked);
        mCheckBox.setOnCheckedChangeListener(this);
        mEntry = (EditText) view.findViewById(R.id.et_text);
        mRemoveBtn = view.findViewById(R.id.btn_delete);
        mRemoveBtn.setOnClickListener(this);
        mEntry.setOnFocusChangeListener(this);
        mEntry.setOnEditorActionListener(this);
    }


    public int get_position() {
        return mDataset.items.indexOf(mValue);
    }

    public void set_data(int i){
        mValue = mDataset.items.get(i);
        mTextView.setText(mValue.content);
        mEntry.setText(mValue.content);
        mCheckBox.setChecked(mValue.checked);
        this._update();

    }

    @Override
    public void onClick(View view) {
        if(view == mTextView){
            mIsActive = !mIsActive;
            this._update();
            this.mEntry.requestFocus();
            mEntry.setSelection(mEntry.getText().length());
        }else if(view == mRemoveBtn){
            ((ItemAdapter)mParent).on_remove(get_position());
        }
    }

    private void _update(){

        if(mIsActive){
            mTextView.setVisibility(View.GONE);
            mEntry.setVisibility(View.VISIBLE);
            mRemoveBtn.setVisibility(View.VISIBLE);
        }else{
            mTextView.setVisibility(View.VISIBLE);
            mEntry.setVisibility(View.GONE);
            mRemoveBtn.setVisibility(View.GONE);
        }
    }

    public void  set_active(){
        mIsActive = true;
        this._update();
        this.mEntry.requestFocus();
        mEntry.setSelection(mEntry.getText().length());
    }

    public void on_valid(){
        mValue.content = mEntry.getText().toString();
        mValue.update();
        mTextView.setText(mValue.content);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b) return;;
        mIsActive = false;
        this._update();
        on_valid();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
            mParent.insert_after(get_position());


            return false;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
        mValue.checked = b;
        mValue.update();
    }


}
