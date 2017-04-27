package data.exercise;

import data.AbstractBaseEntity;
import data.Topic;
import data.dao.ExerciseDAO;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllExercises", query = "SELECT e FROM Exercise e"),
    @NamedQuery(name = "findExerciseByTopic",
        query = " SELECT e FROM Exercise e  WHERE :topic MEMBER OF e.topics"),
    @NamedQuery(name = "getNextExercises", query =
          " SELECT e FROM Exercise e"
        + " LEFT JOIN ExerciseAttemptHistory eah ON eah.exercise = e"
        + " LEFT JOIN ExerciseRating er ON er.id.exerciseID = e.id"
        + " LEFT JOIN User u ON eah.user = u"
        + " WHERE u = :user OR u IS NULL"
        + " AND :topic MEMBER OF e.topics"
        + " GROUP BY e"
        + " ORDER BY SUM(eah.success), AVG(er.rating)"),
})
@Table
public class Exercise extends AbstractBaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String text;
  @Column(columnDefinition = "TEXT")
  private String solution;

  @ManyToMany(cascade=CascadeType.ALL, mappedBy="exercises")
  @JoinTable(name="TOPIC_EXERCISE")
  private Collection<Topic> topics;

  protected Exercise(){
    super();
    topics = new HashSet<>();
  }

  public Exercise(String title, String text) {
    this();
    setTitle(title);
    setText(text);
  }

  public Exercise(String title, String text, String solution) {
    this(title, text);
    setSolution(solution);
  }

  /**
   * Adds the Exercise to the database
   */
  public void create() {
    ExerciseDAO.getInstance().persist(this);
  }

  /**
   * Removes the Exercise from the database
   */
  public void delete() {
    ExerciseDAO.getInstance().remove(this);
  }

  /**
   * Updates the Exercise's database entry
   */
  public void update() {
    ExerciseDAO.getInstance().merge(this);
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  /**
   * Adds this exercise to all topics in a collection
   *
   * @param topics the topics to add this exercise to
   */
  public void addToTopics(Collection<Topic> topics){
    topics.forEach(topic ->
        {topic.addExercise(this);
         topic.update();
        }
    );
    this.topics.addAll(topics);
    update();
  }

  public JSONObject createAbout(){
    JSONObject response = new JSONObject();
    response.put("id", id);
    response.put("title", title);
    response.put("text", text);
    response.put("solution", solution);
    JSONArray tags = new JSONArray();
    for (Topic tag: topics) {
      tags.put(tag.createAbout());
    }
    response.put("tags", tags);
    return response;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Exercise)) {
      return false;
    }
    Exercise otherExercise = (Exercise) other;
    return otherExercise.id == this.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

}
