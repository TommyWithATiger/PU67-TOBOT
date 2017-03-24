package data.rating;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * This is the key class for Rating.
 * userID and topicID together form a composite key.
 */
@Embeddable
public class RatingKey implements Serializable {

  private Integer userID;
  private Integer topicID;

  public RatingKey() {
  }

  /**
   * Instantiates a RatingKey object
   *
   * @param userID, the id of the User that made the rating
   * @param topicID, the id of the topic that is being rated
   */
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

  @Override
  public boolean equals(Object other){
    if(userID == null || topicID == null){
      return super.equals(other);
    }
    else if(other instanceof RatingKey){
      RatingKey otherRatingKey = (RatingKey) other;
      return userID.equals(otherRatingKey.userID) && topicID.equals(otherRatingKey.topicID);
    }
    return false;
  }

  @Override
  public int hashCode(){
    // http://stackoverflow.com/a/11742634/1428218
    return 31 * userID + topicID;
  }

}
