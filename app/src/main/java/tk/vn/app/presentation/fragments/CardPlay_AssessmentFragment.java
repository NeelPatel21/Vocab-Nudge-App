package tk.vn.app.presentation.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simple_rest.s_rest.restapi.request.HeaderTools;

import org.springframework.http.HttpStatus;

import butterknife.OnClick;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.OnCloseListener;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.com.net.CardPlayListFetchTask;
import tk.vn.app.com.net.CardPlayTask;
import tk.vn.app.model.AssessmentCardBean;
import tk.vn.app.model.CardPlayBean;
import tk.vn.app.model.CardPlay_Result;
import tk.vn.app.model.SubscriptionBean;
import tk.vn.app.model.TrainingCardBean;
import tk.vn.app.presentation.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardPlay_AssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPlay_AssessmentFragment extends Fragment {

    private AssessmentCardBean assessmentCardBean;
    private CardPlayTask st;
    private OnCloseListener listener;

    private String title;
    private int markIndex = -1;

    TextView card_label;
    TextView question;
    TextView option1;
    TextView option2;
    TextView option3;
    TextView option4;
    ImageView opImg1;
    ImageView opImg2;
    ImageView opImg3;
    ImageView opImg4;
    View closeButton;

    View view;

    public CardPlay_AssessmentFragment() {
        // Required empty public constructor
    }

    public static CardPlay_AssessmentFragment newInstance(@NonNull AssessmentCardBean assessmentCardBean,
                                                          @NonNull String title, @NonNull OnCloseListener listener) {
        CardPlay_AssessmentFragment fragment = new CardPlay_AssessmentFragment();
        fragment.title = title;
        fragment.listener = listener;
        fragment.assessmentCardBean = assessmentCardBean;
        System.out.println("AssessmentCard :- "+assessmentCardBean);
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
        view = inflater.inflate(R.layout.fragment_card_play__assessment, container, false);
        card_label = view.findViewById(R.id.card_label);
        question = view.findViewById(R.id.question_text);
        option1 = view.findViewById(R.id.option1_text);
        option2 = view.findViewById(R.id.option2_text);
        option3 = view.findViewById(R.id.option3_text);
        option4 = view.findViewById(R.id.option4_text);
        opImg1 = view.findViewById(R.id.option1_img);
        opImg2 = view.findViewById(R.id.option2_img);
        opImg3 = view.findViewById(R.id.option3_img);
        opImg4 = view.findViewById(R.id.option4_img);
        closeButton = view.findViewById(R.id.close_view);

        card_label.setText(title);
        question.setText(assessmentCardBean.getQuestion());
        option1.setText(assessmentCardBean.getOptions()[0]);
        option2.setText(assessmentCardBean.getOptions()[1]);
        option3.setText(assessmentCardBean.getOptions()[2]);
        option4.setText(assessmentCardBean.getOptions()[3]);

        opImg1.setImageResource(R.drawable.wrong);
        opImg2.setImageResource(R.drawable.wrong);
        opImg3.setImageResource(R.drawable.wrong);
        opImg4.setImageResource(R.drawable.wrong);

        switch (assessmentCardBean.getCorrectOptionIndex()){
            case 0:
                opImg1.setImageResource(R.drawable.right);break;
            case 1:
                opImg2.setImageResource(R.drawable.right);break;
            case 2:
                opImg3.setImageResource(R.drawable.right);break;
            case 3:
                opImg4.setImageResource(R.drawable.right);break;
        }

        if(markIndex==-1){
            option1.setOnClickListener(x->play(0));
            option2.setOnClickListener(x->play(1));
            option3.setOnClickListener(x->play(2));
            option4.setOnClickListener(x->play(3));
        }else{
            showImage(markIndex);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClose(CardPlay_AssessmentFragment.this);
            }
        });

        return view;
    }

    private void showImage(int markOptionIndex){
        markIndex = markOptionIndex;
        switch (assessmentCardBean.getCorrectOptionIndex()){
            case 0:
                opImg1.setVisibility(View.VISIBLE);break;
            case 1:
                opImg2.setVisibility(View.VISIBLE);break;
            case 2:
                opImg3.setVisibility(View.VISIBLE);break;
            case 3:
                opImg4.setVisibility(View.VISIBLE);break;
        }

        switch (markOptionIndex){
            case 0:
                opImg1.setVisibility(View.VISIBLE);break;
            case 1:
                opImg2.setVisibility(View.VISIBLE);break;
            case 2:
                opImg3.setVisibility(View.VISIBLE);break;
            case 3:
                opImg4.setVisibility(View.VISIBLE);break;
        }
        option1.setOnClickListener(null);
        option2.setOnClickListener(null);
        option3.setOnClickListener(null);
        option4.setOnClickListener(null);
    }

    private void play(int markIndex){
        CardPlayBean cardPlayBean = new CardPlayBean();
        cardPlayBean.setCardId(assessmentCardBean.getCardId());
        cardPlayBean.setGoalId(assessmentCardBean.getGoalId());
        cardPlayBean.setSubscriptionId(((SubscriptionBean)RunTimeStore.get(Const.SUBSCRIPTION_DETAIL)).getSubscriptionId());
        if(markIndex == assessmentCardBean.getCorrectOptionIndex())
            cardPlayBean.setResult(CardPlay_Result.PASS);
        else
            cardPlayBean.setResult(CardPlay_Result.FAIL);

        // perform the update attempt.
        st = new CardPlayTask(getContext(), new Consumer<HttpStatus>() {
            @Override
            public void consume(HttpStatus httpStatus) {
                playPostProcessor(httpStatus);
                showImage(markIndex);
            }
        });
        st.playCard(cardPlayBean);
    }

    private void playPostProcessor(HttpStatus httpStatus){
        if(httpStatus.equals(HttpStatus.CREATED))
            Log.i(CardPlay_AssessmentFragment.class.getName(),"cardPlay successful");
        else
            Log.w(CardPlay_AssessmentFragment.class.getName(),"error in card play status-"+httpStatus);
    }

}
