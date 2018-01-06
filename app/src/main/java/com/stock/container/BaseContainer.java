package com.stock.container;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.stock.R;


public class BaseContainer extends Fragment{
    private FragmentTransaction ft;

    public void replaceFragment(Fragment fragment) {
        try {
            ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
            ft.commit();

        } catch (Exception e) {
        }
    }


    public void addFragment(Fragment fragment) {
        try {
            ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.add(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
            ft.commit();

        } catch (Exception e) {
        }
    }

    public boolean popFragment(){
        try{
        int init = getChildFragmentManager().getBackStackEntryCount();
            if (init > 1){
                getChildFragmentManager().popBackStack();
                return true;
            }
        }catch (Exception e){}
        return false;
    }

//    public void replaceFragment(Fragment fragment) {
//    }
//
//    public void addFragment(Fragment fragment) {
//    }

    public void setTop() {
    }
}
