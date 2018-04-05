package tk.vn.app.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoalPlay extends GoalBean{

    private List<CardPlayBean> cardPlayBeans;

    public GoalPlay(GoalBean goalBean){
        setGoalId(goalBean.getGoalId());
        setGoalName(goalBean.getGoalName());
        setTrainingCards(goalBean.getTrainingCards());
        setAssessmentCards(goalBean.getAssessmentCards());
    }

    public List<CardPlayBean> getCardPlayBeans() {
        return cardPlayBeans;
    }

    public void setCardPlayBeans(List<CardPlayBean> cardPlayBeans) {
        this.cardPlayBeans = cardPlayBeans;
    }



}
