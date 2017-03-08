package data.rating;

import data.DataAccessObjects.RatingDAO;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;


@Entity
@NamedQueries({
    @NamedQuery(name = "findAllRatings", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "findRatingByUser", query = "SELECT r FROM Rating r WHERE r.ratingKeyPK.userID = :userID"),
    @NamedQuery(name = "findRatingByTopic", query = "SELECT r FROM Rating r WHERE r.ratingKeyPK.topicID = :topicID"),
    @NamedQuery(name = "findRatingByRatingKey", query = "SELECT r FROM Rating r WHERE r.ratingKeyPK.topicID = :topicID AND r.ratingKeyPK.userID = :userID")
})
@Table
public class Rating {

  @EmbeddedId
  private RatingKey ratingKeyPK;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = RatingConverter.class)
  private RatingEnum rating;

  public Rating() {
    super();
    ratingKeyPK = new RatingKey();
  }

  /**
   * Instantiates a Rating object
   *
   * @param userID, the id of the User that made the rating
   * @param topicID, the id of the topic that is being rated
   * @param rating, a rating
   */
  public Rating(Integer userID, Integer topicID, RatingEnum rating) {
    this();
    ratingKeyPK = new RatingKey(userID, topicID);
    this.rating = rating;
  }

  /**
   * Get the Id of the user that made the rating
   *
   * @return the Id of the user
   */
  public Integer getUserID() {
    return ratingKeyPK.getUserID();
  }

  /**
   * Set the Id of the user that made the rating
   *
   * @param userID the id of the user
   */
  public void setUserID(Integer userID) {
    ratingKeyPK.setUserID(userID);
  }

  /**
   * Get the Id of the topic that was rated
   *
   * @return the Id of the topic
   */
  public Integer getTopicID() {
    return ratingKeyPK.getTopicID();
  }

  /**
   * Set the Id of the topic that was rated
   *
   * @param topicID the id of the topic
   */
  public void setTopicID(Integer topicID) {
    ratingKeyPK.setTopicID(topicID);
  }

  /**
   * Get the rating
   *
   * @return the rating
   */
  public RatingEnum getRating() {
    return rating;
  }

  /**
   * Set the rating
   *
   * @param ratingEnum the rating
   */
  public void setRating(RatingEnum ratingEnum) {
    this.rating = ratingEnum;
  }

  /**
   * Adds the Rating to the database
   */
  public void create() {
    RatingDAO.getInstance().persist(this);
  }

  /**
   * Removes the Rating from the database
   */
  public void delete() {
    RatingDAO.getInstance().remove(this);
  }

  /**
   * Updates the Rating's database entry
   */
  public void update() {
    RatingDAO.getInstance().merge(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Rating) {
      return ratingKeyPK.equals(((Rating) other).ratingKeyPK);
    }
    return false;
  }

  @Override
  public int hashCode(){
    return ratingKeyPK.hashCode();
  }
}
