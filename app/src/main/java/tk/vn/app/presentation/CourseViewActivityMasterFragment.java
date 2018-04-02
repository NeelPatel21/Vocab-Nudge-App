package tk.vn.app.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.vn.app.R;

/**
 * A fragment with a Google +1 button.
 * Use the {@link CourseViewActivityMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseViewActivityMasterFragment extends Fragment {

    public CourseViewActivityMasterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CourseViewActivityMasterFragment newInstance(/*String param1, String param2*/) {
        CourseViewActivityMasterFragment fragment = new CourseViewActivityMasterFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_view_activity_master, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }


}
