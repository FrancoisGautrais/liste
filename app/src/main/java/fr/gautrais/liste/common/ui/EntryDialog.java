package fr.gautrais.liste.common.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class EntryDialog extends AlertDialog.Builder implements TextView.OnEditorActionListener {


    public interface ValidationListener {

        public void onValid(EntryDialog dialog, String value);
        public void onCancel(EntryDialog dialog);

    }

    public interface ValidListener {
        public void onValid(EntryDialog dialog, String value);
    }

    public interface CancelListener {
        public void onCancel(EntryDialog dialog);
    }

    private ValidationListener both_listener;
    private ValidListener ok_listener;
    private CancelListener cancel_listener;

    private EditText entry;
    private String ok_text = "Valider";
    private String cancel_text = "Annuler";

    private AlertDialog mCurrent = null;

    public EntryDialog(Context ctx){
        super(ctx);
        init("");
    }

    public EntryDialog(Context ctx, String text){
        super(ctx);
        init(text);
    }



    public EntryDialog setListener(ValidationListener l){
        both_listener = l;
        return this;
    }
    public EntryDialog setListener(ValidListener l){
        ok_listener = l;
        return this;
    }
    public EntryDialog setListener(CancelListener l){
        cancel_listener = l;
        return this;
    }


    public AlertDialog show_dialog(){
        mCurrent = create();
        mCurrent.show();

        return mCurrent;
    }
    public void valid(boolean destroy){
        if(both_listener!=null){
            both_listener.onValid(this, entry.getText().toString());
        }
        if(ok_listener!=null){
            ok_listener.onValid(this, entry.getText().toString());
        }
        if(destroy){
            mCurrent.cancel();
        }
    }

    public void cancel(boolean destroy){
        if(both_listener!=null){
            both_listener.onCancel(this);
        }
        if(cancel_listener!=null){
            cancel_listener.onCancel(this);
        }
        if(destroy){
            mCurrent.cancel();
        }
    }

    private void init(String text){
        setTitle(text);
        entry = new EditText(this.getContext());
        entry.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        entry.setOnEditorActionListener(this);
        setView(entry);
        setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                valid(false);
            }
        });
        setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cancel(false);
            }
        });

        entry.post(() -> {
            entry.requestFocus();


            


            InputMethodManager imm = (InputMethodManager) entry.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(entry, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    public static void run(Context ctx, String text, ValidationListener l){
        EntryDialog self = new EntryDialog(ctx, text);
        self.setListener(l);
        self.show_dialog();
    }
    public static void run(Context ctx, String text, ValidListener l){
        EntryDialog self = new EntryDialog(ctx, text);
        self.setListener(l);
        self.show_dialog();
    }
    public static void run(Context ctx, String text, ValidListener l, CancelListener l2){
        EntryDialog self = new EntryDialog(ctx, text);
        self.setListener(l);
        self.setListener(l2);
        self.show_dialog();
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
            valid(true);


            return false;
        }
        return false;
    }



}
