package data;

import data.dao.TopicDAO;
import java.util.HashSet;
import org.json.JSONObject;

import javax.persistence.*;
import data.exercise.Exercise;
import java.util.Collection;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllTopics", query = "SELECT t FROM Topic t"),
    @NamedQuery(name = "findTopicsByTitle", query = "SELECT t FROM Topic t WHERE t.title LIKE CONCAT('%', :title, '%')"),
    @NamedQuery(name = "findTopicsByParentId", query = "SELECT t FROM Topic t WHERE t.parentId = :parerentId"),
    @NamedQuery(name = "findTopicBySubjectUserSortedByRating", query =
          " SELECT t FROM Rating r JOIN User u JOIN Subject s JOIN Topic t"
        + " WHERE t MEMBER OF s.topics"
        + " AND s.id = :subjectID"
        + " AND u.id = :userID AND r.ratingKeyPK.userID = :userID"
        + " AND t.id = r.ratingKeyPK.topicID"
        + " ORDER BY r.rating"
    )
})
@Table
public class Topic extends AbstractBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String description;
  private int parentId;

  @ManyToMany
  private Collection<Exercise> exercises;

  protected Topic() {
    super();
    exercises = new HashSet<>();
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
   * Get the exercises of the topic.
   *
   * @return the exercises of the topic.
   */
  public Collection<Exercise> getExercises() {
    return new HashSet<>(exercises);
  }

  /**
   * Set the parent of the topic.
   *
   * @param parentId Id of the new parent topic
   * @return true if valid parentId, else false
   */
  public boolean setParentId(int parentId) {
    if (TopicDAO.getInstance() != null && TopicDAO.getInstance().findById(parentId) != null
        || parentId != id) {
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
   * Adds this topic to a subject
   *
   * @param subject, the Subject you want to add this Topic to
   */
  public void addToSubject(Subject subject) {
    subject.addTopic(this);
  }

  /**
   * Adds an exercise to this topic
   *
   * @param exercise, the Exercise you want to add to this topic
   */
  public void addExercise(Exercise exercise) {
    exercises.add(exercise);
  }

  /**
   * Removes an exercise from this topic
   *
   * @param exercise, the Exercise you want to remove
   */
  public void removeExercise(Exercise exercise) {
    exercises.remove(exercise);
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
  public void create() {
    TopicDAO.getInstance().persist(this);
  }

  /**
   * Removes the topic from the database
   */
  public void delete() {
    TopicDAO.getInstance().remove(this);
  }

  /**
   * Updates the topics database entry
   */
  public void update() {
    TopicDAO.getInstance().merge(this);
  }

  /**
   * Creates a JSON object with information about the topic
   *
   * @return A JSON object with the following data:
   *        id (int): the topic id
   *        title (String): the topic title
   *        description (String): the topic description
   */
  public JSONObject createAbout(){
    JSONObject aboutTopic = new JSONObject();
    aboutTopic.put("id", id);
    aboutTopic.put("title", title);
    aboutTopic.put("description", description);
    return aboutTopic;
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
   *
   * @return the id of the object
   */
  @Override
  public int hashCode() {
    return id;
  }
}
