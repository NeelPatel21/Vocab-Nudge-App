package tk.vn.app.presentation;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tk.vn.app.R;


/**
 * Created by neelp on 03-03-2018.
 */

public final class MainActivityPagerAdapter extends FragmentPagerAdapter
        implements TabLayout.OnTabSelectedListener{

    final static public String IC_COURSE_ACTIVE = "course_active";
    final static public String IC_COURSE_INACTIVE = "course_inactive";
    final static public String IC_HOME_ACTIVE = "home_active";
    final static public String IC_HOME_INACTIVE = "home_inactive";

    private Map<String,Integer> tabIcons = new HashMap<>();


    @NonNull private final List<Fragment> fragments = new ArrayList<>(2);
    @NonNull private final List<String> fragmentNames = new ArrayList<>(2);
    @NonNull private final TabLayout tabLayout;
    @NonNull private final Toolbar toolbar;

    public MainActivityPagerAdapter(@NonNull FragmentManager fm,
                                    @NonNull TabLayout tabLayout, Toolbar toolbar) {
        super(fm);
        this.tabLayout=tabLayout;
        this.toolbar=toolbar;
        setup();
    }

    private void setup(){
        fragments.add(HomeFragment.newInstance());
        fragments.add(CourseFragment.newInstance());

        fragmentNames.add("Home");
        fragmentNames.add("My Course");

        tabIcons.put(IC_COURSE_ACTIVE, R.drawable.course_active);
        tabIcons.put(IC_COURSE_INACTIVE, R.drawable.course_inactive);
        tabIcons.put(IC_HOME_ACTIVE, R.drawable.home_active);
        tabIcons.put(IC_HOME_INACTIVE, R.drawable.home_inactive);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    void updateTabIcons(int activeTabIndex) {
        if(activeTabIndex == 0){
            tabLayout.getTabAt(0).setIcon(tabIcons.get(IC_HOME_ACTIVE));
            toolbar.setTitle(fragmentNames.get(0));
        }else{
            tabLayout.getTabAt(0).setIcon(tabIcons.get(IC_HOME_INACTIVE));
        }
        if(activeTabIndex == 1){
            tabLayout.getTabAt(1).setIcon(tabIcons.get(IC_COURSE_ACTIVE));
            toolbar.setTitle(fragmentNames.get(1));
        }else{
            tabLayout.getTabAt(1).setIcon(tabIcons.get(IC_COURSE_INACTIVE));
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateTabIcons(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
