package data;

import data.DataAccessObjects.SubjectDAO;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(name = "findSubjectByTitle", query = "SELECT s FROM Subject s WHERE s.title = :title"),
    @NamedQuery(name = "findSubjectByCode", query = "SELECT s FROM Subject s WHERE s.subjectCode = :subjectCode"),
    @NamedQuery(name = "findSubjectByInstitution", query = "SELECT s FROM Subject s WHERE s.institution = :institution"),
    @NamedQuery(name = "findSubjectByInstitutionAndCode", query = "SELECT s FROM Subject s WHERE s.institution = :institution AND s.subjectCode = :subjectCode")
})
@Table
public class Subject {

  @PersistenceContext
  public static SubjectDAO subjectDAO;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String institution;
  private String subjectCode;
  private String description;

  @ManyToMany
  private Collection<Topic> topics;

  public Subject() {
    super();
    subjectDAO = SubjectDAO.getInstance();
  }

  /**
   * Instantiates a Subject object
   *
   * @param title, the title of the subject
   * @param institution, the institution where the subject is held
   * @param subjectCode, the subjectCode of the subject
   * @param description, a description of the subject
   */
  public Subject(String title, String institution, String subjectCode, String description) {
    this(title, description);
    this.institution = institution;
    this.subjectCode = subjectCode;
  }

  /**
   * Instantiates a Subject object
   *
   * @param title, the title of the subject
   * @param description, a description of the subject
   */
  public Subject(String title, String description) {
    this();
    this.title = title;
    this.description = description;
  }

  /**
   * Get the Id of the subject.
   *
   * @return the Id of the subject
   */
  public int getId() {
    return id;
  }

  /**
   * Get the title of the subject.
   *
   * @return the title of the subject
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the title of the subject.
   *
   * @param title New title of the subject
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get the institution of the subject.
   *
   * @return the institution of the subject
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Set the institution of the subject.
   *
   * @param institution New institution of the subject
   */
  public void setInstitution(String institution) {
    this.institution = institution;
  }

  /**
   * Get the subjectCode of the subject.
   *
   * @return the Id of the subject
   */
  public String getSubjectCode() {
    return subjectCode;
  }

  /**
   * Set the subject code of the subject.
   *
   * @param subjectCode New subject code of the subject
   */
  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  /**
   * Get the description of the subject.
   *
   * @return the description of the subject
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the description of the subject.
   *
   * @param description New description of the subject
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the topics of this subject
   *
   * @return Collection of Topic Objects
   */
  public Collection<Topic> getTopics() {
    return new ArrayList<Topic>(topics);
  }

  /**
   * Adds a topic to the subject
   *
   * @param topic, the Topic to be added
   */
  public void addTopic(Topic topic) {
    if (!topics.contains(topic)) {
      topics.add(topic);
    }
  }

  /**
   * Removes a topic from the subject
   *
   * @param topic, the Topic to be removed
   */
  public void removeTopic(Topic topic) {
    if (topics.contains(topic)) {
      topics.remove(topic);
    }
  }
}
