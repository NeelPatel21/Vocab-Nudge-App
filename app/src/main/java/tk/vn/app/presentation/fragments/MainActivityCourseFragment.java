package tk.vn.app.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.vn.app.R;
import tk.vn.app.presentation.CourseViewActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainActivityCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityCourseFragment extends Fragment {

//    Unbinder unbinder;
//
//    @BindView(R.id.course_progress_card)
    View courseCard;

    public MainActivityCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainActivityCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivityCourseFragment newInstance() {
        MainActivityCourseFragment fragment = new MainActivityCourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        // bind view using butter knife
//        unbinder = ButterKnife.bind(this, view);

        courseCard = view.findViewById(R.id.course_progress_card);

        courseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseView(v);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void openCourseView(View view){
        Log.i("test","onclick");
//        Toast.makeText(getContext(), "cacacsacac", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), CourseViewActivity.class);
        startActivity(i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
//        unbinder.unbind();
    }
}
