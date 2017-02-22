package data;

import data.DataAccessObjects.TopicDAO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;


@Entity
@NamedQueries({
    @NamedQuery(name = "findTopicByTitle", query = "SELECT t FROM Topic t WHERE t.title = :title"),
    @NamedQuery(name = "findTopicByParentId", query = "SELECT t FROM Topic t WHERE t.parentId = :parerentId"),
})
@Table
public class Topic {

  @PersistenceContext
  private static TopicDAO topicDAO;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String description;
  private int parentId;

  public Topic() {
    super();
    topicDAO = TopicDAO.getInstance();
  }

  /**
   * Instantiates a Topic object
   *
   * @param title, the title of the topic
   * @param description, a description of the topic
   */
  public Topic(String title, String description) {
    this();
    this.title = title;
    this.description = description;
  }

  /**
   * Instantiates a Topic object
   *
   * @param title, the title of the topic
   * @param description, a description of the topic
   * @param parentId, the Id of the parent topic
   */
  public Topic(String title, String description, int parentId) {
    this(title, description);
    this.parentId = parentId;
  }

  /**
   * Get the Id of the parent topic.
   *
   * @return the Id of the parent topic
   */
  public int getParentId() {
    return parentId;
  }

  /**
   * Set the parent of the topic.
   *
   * @param parentId Id of the new parent topic
   * @return true if valid parentId, else false
   */
  public boolean setParentId(int parentId) {
    if (topicDAO != null && topicDAO.findById(parentId) != null || parentId != id) {
      this.parentId = parentId;
      return true;
    }
    return false;
  }

  /**
   * Get the description of the topic.
   *
   * @return the description of the topic
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the description of the topic.
   *
   * @param description New description of the topic
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the title of the topic.
   *
   * @return Title of the topic
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the title(name) of the topic.
   *
   * @param title New Title of the topic
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get the id of the topic.
   *
   * @return The id of the topic
   */
  public int getId() {
    return id;
  }

  /**
   *  Adds this topic to a subject
   *
   * @param subject, the Subject you want to add this Topic to
   */
  public void addToSubject(Subject subject) {
    subject.addTopic(this);
  }

  /**
   * Removes this topic from the subject
   *
   * @param subject, the Subject this Topic will be removed from
   */
  public void removeFromSubject(Subject subject) {
    subject.removeTopic(this);
  }

  /**
   * Adds the topic to the database
   */
  public void create(){
    topicDAO.persist(this);
  }

  /**
   * Removes the topic from the database
   */
  public void delete(){
    topicDAO.remove(this);
  }

  /**
   * Updates the topics database entry
   */
  public void update(){
    topicDAO.merge(this);
  }

  /**
   * Overrides the default equals method
   *
   * @param other The object to check against
   * @return A boolean indicating if the other object is equal to this, that is has the same id
   */
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Topic)) {
      return false;
    }
    Topic otherTopic = (Topic) other;
    return otherTopic.id == this.id;
  }

  /**
   * Overrides the hashcode of the object to be equal to its id
   * @return the id of the object
   */
  @Override
  public int hashCode(){
    return id;
  }
}
