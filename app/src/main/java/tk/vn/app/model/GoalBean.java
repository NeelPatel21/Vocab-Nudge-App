package tk.vn.app.model;

import java.util.List;

public class GoalBean {
    private long goalId;
    private String goalName;
    private List<TrainingCardBean> trainingCards;
    private List<AssessmentCardBean> assessmentCards;

    public GoalBean(){}

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public List<TrainingCardBean> getTrainingCards() {
        return trainingCards;
    }

    public void setTrainingCards(List<TrainingCardBean> trainingCards) {
        this.trainingCards = trainingCards;
    }

    public List<AssessmentCardBean> getAssessmentCards() {
        return assessmentCards;
    }

    public void setAssessmentCards(List<AssessmentCardBean> assessmentCards) {
        this.assessmentCards = assessmentCards;
    }

    @Override
    public String toString() {
        return "[GoalBean] id-"+getGoalId()+", trainingCards-"+trainingCards
                +", assessmentCards-"+assessmentCards;
    }
}
