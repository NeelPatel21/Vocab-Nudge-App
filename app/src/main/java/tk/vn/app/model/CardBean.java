package tk.vn.app.model;

public class CardBean {
    private long cardId;
    private long goalId;
    private long cardSequence;

    public CardBean() {}

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getCardSequence() {
        return cardSequence;
    }

    public void setCardSequence(long cardSequence) {
        this.cardSequence = cardSequence;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }
}
