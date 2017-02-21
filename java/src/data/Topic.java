package data;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Topic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String description;
  private int parentId;

  @Transient
  private static EntityManagerFactory entityManagerFactory;
  @Transient
  private EntityManager entityManager;


  public Topic() {
    super();
  }

  /**
   * Instantiates a Topic object
   *
   * @param  title,  the title of the topic
   * @param  description,  a description of the topic
   */
  public Topic(String title, String description) {
    this();
    this.title = title;
    this.description = description;
  }

  /**
   * Instantiates a Topic object
   *
   * @param  title,  the title of the topic
   * @param  description,  a description of the topic
   * @param  parentId,  the Id of the parent topic
   */
  public Topic(String title, String description, int parentId) {
    this(title, description);
    this.parentId = parentId;
  }

  /** Get the Id of the parent topic.
   *
   *  @return the Id of the parent topic
   */
  public int getParentId() {
    return parentId;
  }

  /** Set the parent of the topic.
   *
   *  @param parentId Id of the new parent topic
   */
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  /** Get the description of the topic.
   *
   *  @return the description of the topic
   */
  public String getDescription() {
    return description;
  }

  /** Set the description of the topic.
   *
   *  @param description New description of the topic
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /** Get the title of the topic.
   *
   *  @return Title of the topic
   */
  public String getTitle() {
    return title;
  }

  /** Set the title(name) of the topic.
   *
   *  @param title New Title of the topic
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /** Get the id of the topic.
   *
   *  @return The id of the topic
   */
  public int getId() {
    return id;
  }

}
