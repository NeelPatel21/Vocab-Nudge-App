package tk.vn.app.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.model.UserBean;
import tk.vn.app.presentation.CardPlayActivity;
import tk.vn.app.presentation.CourseViewActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainActivityHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityHomeFragment extends Fragment {

    public MainActivityHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainActivityHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivityHomeFragment newInstance() {
        MainActivityHomeFragment fragment = new MainActivityHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // bind view using butter knife
//        unbinder = ButterKnife.bind(this, view);


        TextView courseCard = view.findViewById(R.id.next_card_timing);

        TextView creditText = view.findViewById(R.id.text_credit);
        creditText.setText(((UserBean)RunTimeStore.get(Const.USER_DETAIL)).getCredits()+"");

        courseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardPlay(v);
            }
        });


        return view;
    }

    private void openCardPlay(View view){
        Log.i("test","onclick");
//        Toast.makeText(getContext(), "cacacsacac", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), CardPlayActivity.class);
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

}
