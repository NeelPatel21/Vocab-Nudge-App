package tk.vn.app.com;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

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

public class CourseUtil {

    private final CourseBean courseBean;

    public CourseUtil(@NonNull CourseBean courseBean){
        this.courseBean = courseBean;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TrainingCardBean getTrainingCardBean(long goalId, long cardId){
        GoalBean goal = courseBean.getGoals().stream()
                            .filter(x->x.getGoalId() == goalId)
                            .findAny().orElse(null);
        if(goal==null)
            return null;

        return goal.getTrainingCards().stream()
                .filter(x->x.getCardId()==cardId)
                .findAny().orElse(null);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    public AssessmentCardBean getAssessmentCardBean(long goalId, long cardId){
        GoalBean goal = courseBean.getGoals().stream()
                            .filter(x->x.getGoalId() == goalId)
                            .findAny().orElse(null);
        if(goal==null)
            return null;

        return goal.getAssessmentCards().stream()
                .filter(x->x.getCardId()==cardId)
                .findAny().orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GoalBean getGoalBean(long goalId){
        return courseBean.getGoals().stream()
                .filter(x->x.getGoalId() == goalId)
                .findAny().orElse(null);
    }
}
