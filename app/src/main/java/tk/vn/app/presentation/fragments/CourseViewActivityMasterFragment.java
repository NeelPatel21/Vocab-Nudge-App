package tk.vn.app.presentation.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tk.vn.app.R;
import tk.vn.app.com.CardPlayProcessor;
import tk.vn.app.com.Const;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.model.GoalBean;
import tk.vn.app.presentation.adapters.CourseViewGoalListItemAdapter;

/**
 * A fragment with a Google +1 button.
 * Use the {@link CourseViewActivityMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseViewActivityMasterFragment extends Fragment {

    public CourseViewActivityMasterFragment() {
        // Required empty public constructor
    }

    private String title;
    private ListView listView;

//    private CardPlayProcessor cardPlayProcessor;

    // TODO: Rename and change types and number of parameters
    public static CourseViewActivityMasterFragment newInstance(String title) {
        CourseViewActivityMasterFragment fragment = new CourseViewActivityMasterFragment();
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_REF_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(Const.FRAGMENT_REF_TITLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_view_activity_master, container, false);

        TextView headerText = view.findViewById(R.id.header_text);
        listView = view.findViewById(R.id.list_view);
        headerText.setText(title);
        CardPlayProcessor processor = RunTimeStore.get(Const.CARD_PROCESSOR);
        if(processor == null){
            processor = new CardPlayProcessor(RunTimeStore.get(Const.COURSE_DETAIL),
                    RunTimeStore.get(Const.SUBSCRIPTION_DETAIL));
            RunTimeStore.storeObj(Const.CARD_PROCESSOR,processor);
        }
//
//        if(processor!=null){
        List<GoalBean> goalBeans = processor.getAllGoalPlays();
        System.out.println(goalBeans);
        inflateList(goalBeans);
//        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    private void inflateList(List<GoalBean> list){
        final CourseViewGoalListItemAdapter adapter =
                new CourseViewGoalListItemAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoalBean goalBean = ((GoalBean)adapterView.getItemAtPosition(i));
                Toast.makeText(getContext(),"goalId - "+goalBean.getGoalId(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
