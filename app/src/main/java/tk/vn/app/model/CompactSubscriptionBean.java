package tk.vn.app.model;

/**
 * Created by neelp on 28-03-2018.
 */

public class CompactSubscriptionBean extends CompactCourseBean{
    private long subscriptionId = -1;
    private String subscriptionStatus;

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }
}