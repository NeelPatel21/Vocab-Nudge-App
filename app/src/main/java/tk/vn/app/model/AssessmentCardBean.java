package tk.vn.app.model;

import java.util.Arrays;

public class AssessmentCardBean extends CardBean {

    private String question;

    private String[] options;

    private int correctOptionIndex;

    public AssessmentCardBean(){}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    @Override
    public String toString() {
        return "AssessmentCardBean{" +
                "question='" + question + '\'' +
                ", options=" + Arrays.toString(options) +
                ", correctOptionIndex=" + correctOptionIndex +
                '}';
    }
}
