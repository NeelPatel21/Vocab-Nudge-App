package tk.vn.app.model;

public class CardPlayBean {

    private long cardPlayId;

    private CardPlay_Result result = CardPlay_Result.NOT_APPlY;

    private long cardId;

    private long goalId;

    private long subscriptionId;

    public CardPlayBean(){}

    public long getCardPlayId() {
        return cardPlayId;
    }

    public void setCardPlayId(long cardPlayId) {
        this.cardPlayId = cardPlayId;
    }

    public CardPlay_Result getResult() {
        return result;
    }

    public void setResult(CardPlay_Result result) {
        this.result = result;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    @Override
    public String toString() {
        return "[CardPlayBean] id-"+getCardPlayId()+", cardId-"+cardId;
    }
}
