package data.DataAccessObjects;

import data.Subject;
import data.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
   * Finds subjects that matches the given title
   *
   * @param title, the title to query for
   * @return List of Subject objects that match the title
   */
  public List<Subject> findSubjectByTitle(String title) {
    return super.find("findSubjectByTitle", "title", title);
  }

  /**
   * Finds one subject that matches the given title
   *
   * @param title, the title to query for
   * @return A Subject object that matches the title
   */
  public Subject findSingleSubjectByTitle(String title) {
    List<Subject> results = findSubjectByTitle(title);
    if (results != null) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
  }

  public List<Subject> findSubjectsByCode(String code) {
    return super.find("findSubjectByCode", "subjectCode", code);
  }

  public List<Subject> findSubjectByInstituton(String institution) {
    return super.find("findSubjectByInstitution", "institution", institution);
  }

  public List<Subject> findSubjectByInstitutionAndCode(String institution, String code) {
    List<Subject> entityList;
    try {
      EntityManager entityManager = emFactory.createEntityManager();
      TypedQuery<Subject> query = entityManager
          .createNamedQuery("findSubjectByInstitutionAndCode", Subject.class);
      query.setParameter("institution", institution);
      query.setParameter("subjectCode", code);
      entityList = query.getResultList();
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
  }

  public List<Subject> findByTopic(Topic topic) {
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
   * Finds subjects that matches the given subject code
   *
   * @param code, the subject code
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectsByCode(String code) {
    return super.find("findSubjectByCode", "subjectCode", code);
  }

  /**
   * Finds subjects that matches the given institution
   *
   * @param institution, the institution to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectByInstituton(String institution) {
    return super.find("findSubjectByInstitution", "institution", institution);
  }

  /**
   * Finds subjects that matches the given institution and subject code
   *
   * @param institution, the institution to query for
   * @param code, the subject code to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findSubjectByInstitutionAndCode(String institution, String code) {
    List<Subject> entityList;
    try {
      EntityManager entityManager = emFactory.createEntityManager();
      TypedQuery<Subject> query = entityManager
          .createNamedQuery("findSubjectByInstitutionAndCode", Subject.class);
      query.setParameter("institution", institution);
      query.setParameter("subjectCode", code);
      entityList = query.getResultList();
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
  }

  /**
   * Finds subjects contains the given topic
   *
   * @param topic, the topic to query for
   * @return List of Subject objects that match the query
   */
  public List<Subject> findByTopic(Topic topic) {
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
