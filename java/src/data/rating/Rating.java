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
  private RatingEnum rating;

  public Rating() {
    super();
    ratingDAO = RatingDAO.getInstance();
    ratingKeyPK = new RatingKey();
  }

  public Rating(Integer userID, Integer topicID, RatingEnum rating) {
    this();
    ratingKeyPK = new RatingKey(userID, topicID);
    this.rating = rating;
  }

  public Integer getUserID() {
    return ratingKeyPK.getUserID();
  }

  public void setUserID(Integer userID) {
    ratingKeyPK.setUserID(userID);
  }

  public Integer getTopicID() {
    return ratingKeyPK.getTopicID();
  }

  public void setTopicID(Integer topicID) {
    ratingKeyPK.setTopicID(topicID);
  }

  public RatingEnum getRating() {
    return rating;
  }

  public void setRating(RatingEnum rating) {
    this.rating = rating;
  }

  /**
   * Adds the rating to the database
   */
  public void create(){
    ratingDAO.persist(this);
  }

  /**
   * Removes the rating from the database
   */
  public void delete(){
    ratingDAO.remove(this);
  }

  /**
   * Updates the rating's database entry
   */
  public void update(){
    ratingDAO.merge(this);
  }
}
