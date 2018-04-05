package tk.vn.app.model;

import java.util.List;

public class SubscriptionBean extends CompactSubscriptionBean{

    private List<CardPlayBean> cardPlayBeans;
    
    public SubscriptionBean(){}

    public List<CardPlayBean> getCardPlayBeans() {
        return cardPlayBeans;
    }

    public void setCardPlayBeans(List<CardPlayBean> cardPlayBeans) {
        this.cardPlayBeans = cardPlayBeans;
    }

    @Override
    public String toString() {
        return "[SubscriptionBean] id-"+getSubscriptionId()+", CardPlays-"+cardPlayBeans;
    }
}
