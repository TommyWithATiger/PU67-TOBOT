package data.DataAccessObjects;

import data.DataAccessObjects.util.FieldTuple;
import data.Subject;
import data.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class SubjectDAO extends AbstractBaseDAO<Subject, Integer> {

  protected static SubjectDAO instance;

  /**
   * Instantiates the SubjectDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public SubjectDAO(EntityManagerFactory emFactory) {
    super(Subject.class, emFactory);
  }


  /**
   * Get all database occurrences of Subject
   *
   * @return List of all Subjects
   */
  public List<Subject> findAll() {
    return super.find("findAllSubjects");
  }

  /**
   * Finds subjects that matches the given title
   *
   * @param title, the title to query for
   * @return List of Subject objects that match the title
   */
  public List<Subject> findSubjectsByTitle(String title) {
    return super.find("findSubjectsByTitle", new FieldTuple("title", title));
  }

  /**
   * Finds one subject that matches the given title
   *
   * @param title, the title to query for
   * @return A Subject object that matches the title
   */
  public Subject findSingleSubjectByTitle(String title) {
    List<Subject> results = findSubjectsByTitle(title);
    if (!results.isEmpty()) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
  }

  /**
   * Finds subjects that matches the given subject code
   *
   * @param code, the subject code
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectsByCode(String code) {
    return super.find("findSubjectsByCode", new FieldTuple("subjectCode", code));
  }

  /**
   * Finds subjects that matches the given institution
   *
   * @param institution, the institution to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectsByInstituton(String institution) {
    return super.find("findSubjectsByInstitution", new FieldTuple("institution", institution));
  }

  /**
   * Finds subjects that matches the given institution and subject code
   *
   * @param institution, the institution to query for
   * @param code, the subject code to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectsByInstitutionAndCode(String institution, String code) {
    return super.find("findSubjectsByInstitutionAndCode",
        new FieldTuple("institution", institution),
        new FieldTuple("subjectCode", code));
  }

  /**
   * Finds subjects contains the given topic
   *
   * @param topic, the topic to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectsByTopic(Topic topic) {
    List<Subject> entityList;
    try {
      EntityManager entityManager = emFactory.createEntityManager();
      Query query = entityManager.createNativeQuery(
          "SELECT * FROM TOBOT.SUBJECT s WHERE s.id IN (SELECT * FROM TOBOT.SUBJECT_TOPIC st WHERE st.topicID = :topicId)",
          Subject.class);
      query.setParameter("topicId", topic.getId());
      entityList = (List<Subject>) query.getResultList(); //FIXME find a better fix
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
  }

  /**
   * Returns the static SubjectDAO instance
   *
   * @return SubjectDAO, null if not instantiated
   */
  public static SubjectDAO getInstance() {
    return instance;
  }

  /**
   * Returns the static SubjectDAO instance,
   * Creates one if null
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   * @return the Static SubjectDAO instance for this server instance
   */
  public static SubjectDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new SubjectDAO(emFactory);
    }
    return instance;
  }

}
