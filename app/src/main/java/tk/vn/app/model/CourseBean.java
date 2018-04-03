package tk.vn.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseBean extends CompactCourseBean {
    private List<GoalBean> goals;

    public CourseBean(){}

    public List<GoalBean> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalBean> goals) {
        this.goals = goals;
    }

    public int getNumberOfGoals(){
        return goals!=null?goals.size():0;
    }

    @Override
    public String toString() {
        return "[CourseBean] id-"+getCourseId()+", goals-"+goals;
    }
}
