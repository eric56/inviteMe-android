package br.com.android.invviteme.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.android.invviteme.R;
import br.com.android.invviteme.adapters.ViewPagerAdapter;

public class FragmentNewEventRoot extends Fragment {

    private TabLayout tabStepsNewEvent;
    private ViewPager pagesStepsNewEvent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_event_root,container,false);
        setupViews(view);
        setupViewPager(pagesStepsNewEvent);
        setupTabIcons();
        return view;
    }

    private void setupViews(View view){
        pagesStepsNewEvent = (ViewPager) view.findViewById(R.id.pagesStepsNewEvent);
        tabStepsNewEvent = (TabLayout) view.findViewById(R.id.tabStepsNewEvent);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.apps,
                R.drawable.map_marker,
                R.drawable.dots_vertical
        };
        tabStepsNewEvent.getTabAt(0).setIcon(tabIcons[0]);
        tabStepsNewEvent.getTabAt(1).setIcon(tabIcons[1]);
        tabStepsNewEvent.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new FragmentNewEventStep1(), "NewEventStep1");
        adapter.addFrag(new FragmentNewEventStep2(), "NewEventStep2");
        adapter.addFrag(new FragmentNewEventStep3(), "NewEventStep3");
        viewPager.setAdapter(adapter);
        tabStepsNewEvent.setupWithViewPager(viewPager);
    }

}
