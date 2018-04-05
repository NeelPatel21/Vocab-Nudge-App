package tk.vn.app.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.vn.app.R;
import tk.vn.app.com.CardPlayProcessor;
import tk.vn.app.com.Const;

/**
 * A fragment with a Google +1 button.
 * Use the {@link CourseViewActivityLearningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseViewActivityLearningFragment extends Fragment {

    public CourseViewActivityLearningFragment() {
        // Required empty public constructor
    }

    private String title;

//    private CardPlayProcessor cardPlayProcessor;

    // TODO: Rename and change types and number of parameters
    public static CourseViewActivityLearningFragment newInstance(String title) {
        CourseViewActivityLearningFragment fragment = new CourseViewActivityLearningFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_view_activity_learning, container, false);

        TextView headerText = view.findViewById(R.id.header_text);
        headerText.setText(title);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }


}
