package com.offpen.sp_pen;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class PagerAdapter extends FragmentPagerAdapter {

    private int tabNumber;
    private Map<Integer,String> mfragmentTags;
    private FragmentManager fragmentManager;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior,int tabs) {
        super(fm, behavior);
        fragmentManager = fm;
        mfragmentTags = new HashMap<Integer,String>();
        fm.beginTransaction();
        this.tabNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new PenFragment();
            case 1:
                return new StudentFragment();
            case 2:
                return new AdminFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return tabNumber;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object obj = super.instantiateItem(container,position);
        if(obj instanceof Fragment)
        {
            Fragment f = (Fragment)  obj;
            String tag = f.getTag();
            mfragmentTags.put(position,tag);
        }
        return obj;

    }
    public Fragment getFragment(int position)
    {
        String tag = mfragmentTags.get(position);
        if(tag == null)
            return null;
        return fragmentManager.findFragmentByTag(tag);
    }
}
