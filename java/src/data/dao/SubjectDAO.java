package data.dao;

import data.dao.util.FieldTuple;
import data.Subject;
import data.Topic;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class SubjectDAO extends AbstractBaseDAO<Subject, Integer> {

  private static SubjectDAO instance;

  /**
   * Instantiates the SubjectDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  private SubjectDAO(EntityManagerFactory emFactory) {
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
    return super.find("findSubjectsByTopic",
        new FieldTuple("topicID", topic.getId()));
  }

  /**
   * Finds subjects with user as an editor
   *
   * @param editor, the user to query for
   * @return List of Subject objects that has user as an editor
   */
  public List<Subject> findSubjectsByEditor(User editor) {
    return super.find("findSubjectsByEditor",
        new FieldTuple("editorID", editor.getId()));
  }

  /**
   * Finds subjects with user as an participant
   *
   * @param participant, the user to query for
   * @return List of Subject objects that has the user as an participant
   */
  public List<Subject> findSubjectsByParticipant(User participant) {
    return super.find("findSubjectsByParticipant",
        new FieldTuple("participantID", participant.getId()));
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
   * Creates the static SubjectDAO instance,
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
      if (instance == null || !SubjectDAO.emFactory.isOpen()) {
      instance = new SubjectDAO(emFactory);
    }
  }

}
