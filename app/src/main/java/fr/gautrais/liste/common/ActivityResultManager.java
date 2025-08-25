package fr.gautrais.liste.common;

import java.util.ArrayList;

public class ActivityResultManager {

    public static class ActivityResult {
        public Object   result;

        public boolean has_value = false;

    }

    private static ArrayList<ActivityResult> mData = new ArrayList<ActivityResult>();

    public static Object pop(){
        ActivityResult ar = mData.get(mData.size()-1);
        if(ar.has_value){
            mData.remove(mData.size()-1);
            return ar.result;
        }
        return null;
    }

    public static boolean hasValue(){
        ActivityResult ar = mData.get(mData.size()-1);
        return ar.has_value;
    }

    public static ActivityResult push(){
        ActivityResult ar = new ActivityResult();
        ar.result = null;
        mData.add(ar);
        return ar;
    }

    public static void setValue(Object v){
        ActivityResult ar = mData.get(mData.size()-1);
        ar.result = v;
    }




}
