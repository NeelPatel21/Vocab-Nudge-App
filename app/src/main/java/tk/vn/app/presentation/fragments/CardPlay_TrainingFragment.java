package tk.vn.app.presentation.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import org.springframework.http.HttpStatus;

import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.OnCloseListener;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.com.net.CardPlayTask;
import tk.vn.app.model.CardPlayBean;
import tk.vn.app.model.CardPlay_Result;
import tk.vn.app.model.SubscriptionBean;
import tk.vn.app.model.TrainingCardBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardPlay_TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPlay_TrainingFragment extends Fragment {

    private TrainingCardBean trainingCardBean;
    private CardPlayTask st;

    private String title;

    private OnCloseListener listener;

    TextView description;
    TextView cardLabel;
    TextView synonyms;
    TextView antonyms;
    View closeButton;

    View view;

    public CardPlay_TrainingFragment() {
        // Required empty public constructor
    }

    public static CardPlay_TrainingFragment newInstance(@NonNull TrainingCardBean trainingCardBean,
                                                        @NonNull String title,@NonNull OnCloseListener listener) {
        CardPlay_TrainingFragment fragment = new CardPlay_TrainingFragment();
        fragment.title = title;
        fragment.listener = listener;
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
        if(view!=null)
            return view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_card_play__training, container, false);
        description = view.findViewById(R.id.description_text);
        synonyms = view.findViewById(R.id.synonym_text);
        antonyms = view.findViewById(R.id.antonym_text);
        cardLabel = view.findViewById(R.id.card_label);
        closeButton = view.findViewById(R.id.close_view);

        cardLabel.setText(title);
        description.setText(trainingCardBean.getExplanation());
        synonyms.setText(trainingCardBean.getSynonyms());
        antonyms.setText(trainingCardBean.getAntonyms());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                play();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void play(){
        CardPlayBean cardPlayBean = new CardPlayBean();
        cardPlayBean.setCardId(trainingCardBean.getCardId());
        cardPlayBean.setGoalId(trainingCardBean.getGoalId());
        cardPlayBean.setSubscriptionId(((SubscriptionBean) RunTimeStore.get(Const.SUBSCRIPTION_DETAIL)).getSubscriptionId());
        cardPlayBean.setResult(CardPlay_Result.NOT_APPlY);
        // perform the update attempt.
        st = new CardPlayTask(getContext(), new Consumer<HttpStatus>() {
            @Override
            public void consume(HttpStatus httpStatus) {
                playPostProcessor(httpStatus);
            }
        });
        st.playCard(cardPlayBean);
    }

    private void playPostProcessor(HttpStatus httpStatus){
        if(httpStatus.equals(HttpStatus.CREATED))
            Log.i(CardPlay_TrainingFragment.class.getName(),"cardPlay successful");
        else
            Log.w(CardPlay_TrainingFragment.class.getName(),"error in card play status-"+httpStatus);
        listener.onClose(this);
    }
}
