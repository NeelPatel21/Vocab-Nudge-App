package tk.vn.app.presentation.adapters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tk.vn.app.com.CourseUtil;
import tk.vn.app.model.AssessmentCardBean;
import tk.vn.app.model.CardBean;
import tk.vn.app.model.CourseBean;
import tk.vn.app.model.TrainingCardBean;
import tk.vn.app.presentation.fragments.CardPlay_AssessmentFragment;
import tk.vn.app.presentation.fragments.CardPlay_TrainingFragment;
import tk.vn.app.presentation.fragments.CourseViewActivityAllFragment;
import tk.vn.app.presentation.fragments.CourseViewActivityLearningFragment;
import tk.vn.app.presentation.fragments.CourseViewActivityMasterFragment;
import tk.vn.app.presentation.fragments.CourseViewActivityWrongFragment;


/**
 * Created by neelp on 03-03-2018.
 */

public final class CardPlayActivityPagerAdapter extends FragmentStatePagerAdapter
        /*implements TabLayout.OnTabSelectedListener*/{

//    final static public String IC_COURSE_ACTIVE = "course_active";
//    final static public String IC_COURSE_INACTIVE = "course_inactive";
//    final static public String IC_HOME_ACTIVE = "home_active";
//    final static public String IC_HOME_INACTIVE = "home_inactive";

//    private Map<String,Integer> tabIcons = new HashMap<>();


    @NonNull private final List<Fragment> fragments = new ArrayList<>(2);
    @NonNull private final List<String> fragmentNames = new ArrayList<>(2);
    @NonNull private final List<TrainingCardBean> trainingCardBeans;
    @NonNull private final CourseUtil tool;
    @NonNull private final List<AssessmentCardBean> assessmentCardBeans;
    FragmentManager fm;
//    @NonNull private final Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CardPlayActivityPagerAdapter(@NonNull FragmentManager fm, @NonNull List<CardBean> cards,
                                        @NonNull CourseBean courseBean) {
        super(fm);
        this.fm=fm;
        tool = new CourseUtil(courseBean);
        trainingCardBeans = cards.stream()
                .map(x->tool.getTrainingCardBean(x.getGoalId(),x.getCardId()))
                .filter(x->x!=null)
                .collect(Collectors.toList());
        assessmentCardBeans = cards.stream()
                .map(x->tool.getAssessmentCardBean(x.getGoalId(),x.getCardId()))
                .filter(x->x!=null)
                .collect(Collectors.toList());
        setup();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setup(){
        for(TrainingCardBean tcb :trainingCardBeans)
            fragments.add(CardPlay_TrainingFragment.newInstance(tcb,
                    tool.getGoalBean(tcb.getGoalId()).getGoalName(),x->removeFragment(x)));

        for(AssessmentCardBean acb :assessmentCardBeans)
            fragments.add(CardPlay_AssessmentFragment.newInstance(acb,"Quiz",x->removeFragment(x)));
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void removeFragment(Fragment fragment){
        FragmentTransaction ft =fm.beginTransaction();
        ft.remove(fragment);
        fragments.remove(fragment);
        notifyDataSetChanged();
        ft.commitNow();
    }
}
