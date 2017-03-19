package data;

import data.dao.SubjectDAO;
import data.user.User;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.json.JSONObject;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllSubjects", query = "SELECT s FROM Subject s"),
    @NamedQuery(name = "findSubjectsByTitle", query = "SELECT s FROM Subject s WHERE s.title LIKE CONCAT('%', :title, '%')"),
    @NamedQuery(name = "findSubjectsByCode", query = "SELECT s FROM Subject s WHERE s.subjectCode LIKE CONCAT('%', :subjectCode, '%')"),
    @NamedQuery(name = "findSubjectsByInstitution", query = "SELECT s FROM Subject s WHERE s.institution LIKE CONCAT('%', :institution, '%')"),
    @NamedQuery(name = "findSubjectsByInstitutionAndCode", query = "SELECT s FROM Subject s WHERE s.institution LIKE CONCAT('%', :institution, '%') AND s.subjectCode LIKE CONCAT('%', :subjectCode, '%')"),
    @NamedQuery(name = "findSubjectsByTopic",
        query = " SELECT s FROM Subject s"
              + " JOIN Topic t"
              + " WHERE t.id = :topicID"
              + " AND t MEMBER OF s.topics"),
    @NamedQuery(name = "findSubjectsByEditor",
        query = " SELECT s FROM Subject s"
            + " JOIN User u"
            + " WHERE u.id = :editorID"
            + " AND u MEMBER OF s.editors")
})
@Table
public class Subject extends AbstractBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String institution;
  private String subjectCode;
  private String description;

  @ManyToMany
  private Collection<Topic> topics;

  @ManyToMany
  @JoinTable(name="EDITORS")
  private Collection<User> editors;

  @ManyToMany
  @JoinTable(name="PARTICIPANTS")
  private Collection<User> participants;

  protected Subject() {
    super();
    topics = new HashSet<>();
    editors = new HashSet<>();
    participants = new HashSet<>();
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
    return new HashSet<>(topics);
  }

  /**
   * Returns true if the subject contains the topic
   *
   * @param topic, the topic to check for
   * @return boolean, true if contains
   */
  public boolean hasTopic(Topic topic) {
    return topics.contains(topic);
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

  /**
   * Returns true if the user as an editor of this subject or is an admin
   *
   * @param user, the user to check for
   * @return boolean, true if editor or admin
   */
  public boolean isEditor(User user) {
    return user.isAdmin() || editors.contains(user);
  }

  /**
   * Adds a user to the subjects editor list
   *
   * @param user, the User to be added
   */
  public void addEditor(User user) {
    if (!editors.contains(user)) {
      editors.add(user);
    }
  }

  /**
   * Removes a user from the subject editor list
   *
   * @param user, the User to be removed
   */
  public void removeEditor(User user) {
    if (editors.contains(user)) {
      editors.remove(user);
    }
  }

  /**
   * Get the participants of this subject
   *
   * @return Collection of User Objects
   */
  public Collection<User> getParticipants() {
    return new HashSet<>(participants);
  }


  /**
   * Adds a user to the subjects participant list
   *
   * @param user, the User to be added
   */
  public void addParticipant(User user) {
    if (!participants.contains(user)) {
      participants.add(user);
    }
  }

  /**
   * Removes a user from the subject participant list
   *
   * @param user, the User to be removed
   */
  public void removeParticipant(User user) {
    if (participants.contains(user)) {
      participants.remove(user);
    }
  }

  /**
   * Adds the subject to the database
   */
  public void create() {
    SubjectDAO.getInstance().persist(this);
  }

  /**
   * Removes the subject from the database
   */
  public void delete() {
    SubjectDAO.getInstance().remove(this);
  }

  /**
   * Updates the subjects database entry
   */
  public void update() {
    SubjectDAO.getInstance().merge(this);
  }

  /**
   * Creates a JSON object about the subject
   *
   * @return A JSON object with the following data
   *        id (int): the subject id
   *        title (String): the subject title
   *        description (String): the subject description
   *        institution (String): the institution that arranges the subject
   *        subjectCode (String): the subject code
   */
  public JSONObject createAbout() {
    JSONObject aboutSubject = new JSONObject();
    aboutSubject.put("id", id);
    aboutSubject.put("title", title);
    aboutSubject.put("description", description);
    aboutSubject.put("institution", institution);
    aboutSubject.put("subjectCode", subjectCode);
    return aboutSubject;
  }

  /**
   * Overrides the default equals method
   *
   * @param other The object to check against
   * @return A boolean indicating if the other object is equal to this, that is has the same id
   */
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Subject)) {
      return false;
    }
    Subject otherSubject = (Subject) other;
    return otherSubject.id == this.id;
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
