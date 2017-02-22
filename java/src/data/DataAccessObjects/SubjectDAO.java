package data.DataAccessObjects;

import data.Subject;
import java.util.List;
import javax.persistence.EntityManagerFactory;

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
