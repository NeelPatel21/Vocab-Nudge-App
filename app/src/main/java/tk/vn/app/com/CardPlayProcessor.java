package tk.vn.app.com;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tk.vn.app.model.AssessmentCardBean;
import tk.vn.app.model.CardPlayBean;
import tk.vn.app.model.CardPlay_Result;
import tk.vn.app.model.CourseBean;
import tk.vn.app.model.GoalBean;
import tk.vn.app.model.GoalPlay;
import tk.vn.app.model.SubscriptionBean;
import tk.vn.app.model.TrainingCardBean;

public class CardPlayProcessor {

    private final CourseBean courseBean;

    private final SubscriptionBean subscriptionBean;

    private List<GoalPlay> goalPlays;

    public CardPlayProcessor(@NonNull CourseBean courseBean,
                             @NonNull SubscriptionBean subscriptionBean){
        this.courseBean = courseBean;
        this.subscriptionBean = subscriptionBean;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<GoalBean> getAllGoalPlays(){
//        List<GoalBean> goalBeans = new ArrayList<>(courseBean.getNumberOfGoals());
        return courseBean.getGoals().stream()
                .map(x->{
                    GoalBean newGoal = new GoalBean();
                    newGoal.setGoalId(x.getGoalId());
                    newGoal.setGoalName(x.getGoalName());
                    newGoal.setAssessmentCards(getActivatedAssessmentCards(x));
                    newGoal.setTrainingCards(getActivatedTrainingCards(x));
                    return newGoal;
                }).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<GoalBean> getLearningGoalPlays(){
//        List<GoalBean> goalBeans = new ArrayList<>(courseBean.getNumberOfGoals());
        return courseBean.getGoals().stream()
                .map(x->{
                    GoalBean newGoal = new GoalBean();
                    newGoal.setGoalId(x.getGoalId());
                    newGoal.setGoalName(x.getGoalName());
                    newGoal.setAssessmentCards(getActivatedAssessmentCards(x));
                    newGoal.setTrainingCards(getActivatedTrainingCards(x));
                    return newGoal;
                })
                .filter(x->!x.getAssessmentCards().isEmpty()||!x.getTrainingCards().isEmpty())
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<GoalBean> getWrongGoalPlay(){
//        List<GoalBean> goalBeans = new ArrayList<>(courseBean.getNumberOfGoals());
        return courseBean.getGoals().stream()
                .map(x->{
                    GoalBean newGoal = new GoalBean();
                    newGoal.setGoalId(x.getGoalId());
                    newGoal.setGoalName(x.getGoalName());
                    newGoal.setAssessmentCards(getWrongAssessmentCards(x));
                    return newGoal;
                })
                .filter(x->!x.getAssessmentCards().isEmpty())
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<TrainingCardBean> getActivatedTrainingCards(GoalBean goalBean){
//        List<TrainingCardBean> trainingCardBeans = new ArrayList<>();
        return goalBean.getTrainingCards().stream()
                .filter(x->isActive(x.getCardId()))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<AssessmentCardBean> getActivatedAssessmentCards(GoalBean goalBean){
//        List<TrainingCardBean> trainingCardBeans = new ArrayList<>();
        return goalBean.getAssessmentCards().stream()
                .filter(x->isActive(x.getCardId()))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<AssessmentCardBean> getWrongAssessmentCards(GoalBean goalBean){
//        List<TrainingCardBean> trainingCardBeans = new ArrayList<>();
        return getActivatedAssessmentCards(goalBean).stream()
                .filter(x->!isRight(x.getCardId()))
                .collect(Collectors.toList());
    }

    private boolean isActive(long cardId){
        for(CardPlayBean cardPlayBean : subscriptionBean.getCardPlayBeans())
            if(cardPlayBean.getCardId()==cardId)
                return true;
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isRight(long cardId){
        return subscriptionBean.getCardPlayBeans().stream()
                .filter(x->x.getCardId()==cardId)
                .anyMatch(x->x.getResult().equals(CardPlay_Result.PASS));
    }
}
