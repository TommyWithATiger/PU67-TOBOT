package data;

import data.DataAccessObjects.SubjectDAO;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
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

  public static SubjectDAO initialize(EntityManagerFactory entityManagerFactory) {
    subjectDAO = new SubjectDAO(entityManagerFactory);
    return subjectDAO;
  }

  public Subject() {
    super();
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
}
