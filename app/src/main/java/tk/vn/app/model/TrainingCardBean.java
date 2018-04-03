package tk.vn.app.model;

public class TrainingCardBean extends CardBean {

    private String explanation;

    private String synonyms;

    private String antonyms;


    public TrainingCardBean() {}

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String antonyms) {
        this.antonyms = antonyms;
    }
}
