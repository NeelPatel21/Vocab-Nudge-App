package tk.vn.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by neelp on 28-03-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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