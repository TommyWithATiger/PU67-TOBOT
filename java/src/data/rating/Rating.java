package data.rating;

import data.DataAccessObjects.RatingDAO;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;


@Entity
@NamedQueries({
})
@Table
public class Rating {

  @PersistenceContext
  public static RatingDAO ratingDAO;


  @EmbeddedId
  private RatingKey ratingKeyPK;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = RatingConverter.class)
  private RatingEnum ratingEnum;

  public Rating() {
    super();
    ratingDAO = RatingDAO.getInstance();
    ratingKeyPK = new RatingKey();
  }

  /**
   * Instantiates a Rating object
   *
   * @param userID, the id of the User that made the rating
   * @param topicID, the id of the topic that is being rated
   * @param ratingEnum, a rating
   */
  public Rating(Integer userID, Integer topicID, RatingEnum ratingEnum) {
    this();
    ratingKeyPK = new RatingKey(userID, topicID);
    this.ratingEnum = ratingEnum;
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
  public RatingEnum getRatingEnum() {
    return ratingEnum;
  }

  /**
   * Set the rating
   *
   * @param ratingEnum the rating
   */
  public void setRatingEnum(RatingEnum ratingEnum) {
    this.ratingEnum = ratingEnum;
  }

  /**
   * Adds the Rating to the database
   */
  public void create() {
    ratingDAO.persist(this);
  }

  /**
   * Removes the Rating from the database
   */
  public void delete() {
    ratingDAO.remove(this);
  }

  /**
   * Updates the Rating's database entry
   */
  public void update() {
    ratingDAO.merge(this);
  }
}
