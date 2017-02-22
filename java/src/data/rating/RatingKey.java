package data.rating;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class RatingKey implements Serializable {

  private Integer userID;
  private Integer topicID;

  public RatingKey() {}

  public RatingKey(Integer userID, Integer topicID) {
    this.userID = userID;
    this.topicID = topicID;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getTopicID() {
    return topicID;
  }

  public void setTopicID(Integer topicID) {
    this.topicID = topicID;
  }
}
