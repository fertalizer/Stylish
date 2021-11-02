package com.mark.stylish.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mark.stylish.R;
import com.mark.stylish.adapters.CatalogViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;


public class CatalogFragment extends Fragment {
    private TabLayout mTableLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        mTableLayout = (TabLayout) view.findViewById(R.id.tabLayout_catalog);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager_catalog);

        setViewPager();
        mTableLayout.setupWithViewPager(mViewPager);
        //setupWithViewPager() will replace origin tab by new tab
        /*
        public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
            removeAllTabs();
            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                addTab(newTab().setText(adapter.getPageTitle(i)));
            }
        }
         */
        mTableLayout.getTabAt(0).setText(R.string.catalogWomenApparel);
        mTableLayout.getTabAt(1).setText(R.string.catalogMenApparel);
        mTableLayout.getTabAt(2).setText(R.string.catalogAccessory);

        return view;
    }

    private void setViewPager() {
        CatalogWomenApparelFragment catalogWomenApparelFragment = new CatalogWomenApparelFragment();
        CatalogMenApparelFragment catalogMenApparelFragment = new CatalogMenApparelFragment();
        CatalogAccessoryFragment catalogAccessoryFragment = new CatalogAccessoryFragment();
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(catalogWomenApparelFragment);
        fragmentList.add(catalogMenApparelFragment);
        fragmentList.add(catalogAccessoryFragment);

        CatalogViewPagerFragmentAdapter catalogViewPagerFragmentAdapter =
                new CatalogViewPagerFragmentAdapter(getChildFragmentManager(),fragmentList);

        mViewPager.setAdapter(catalogViewPagerFragmentAdapter);
    }
}
