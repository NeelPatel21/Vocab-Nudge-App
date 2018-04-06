package tk.vn.app.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.vn.app.R;
import tk.vn.app.model.TrainingCardBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardPlay_TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPlay_TrainingFragment extends Fragment {

    private TrainingCardBean trainingCardBean;

    private String title;

    TextView description;
    TextView cardLabel;
    TextView synonyms;
    TextView antonyms;

    public CardPlay_TrainingFragment() {
        // Required empty public constructor
    }

    public static CardPlay_TrainingFragment newInstance(TrainingCardBean trainingCardBean, String title) {
        CardPlay_TrainingFragment fragment = new CardPlay_TrainingFragment();
        fragment.title = title;
        fragment.trainingCardBean = trainingCardBean;
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
        View view = inflater.inflate(R.layout.fragment_card_play__training, container, false);
        description = view.findViewById(R.id.description_text);
        synonyms = view.findViewById(R.id.synonym_text);
        antonyms = view.findViewById(R.id.antonym_text);
        cardLabel = view.findViewById(R.id.card_label);
        cardLabel.setText(title);

        description.setText(trainingCardBean.getExplanation());
        synonyms.setText(trainingCardBean.getSynonyms());
        antonyms.setText(trainingCardBean.getAntonyms());
        return view;
    }

}
