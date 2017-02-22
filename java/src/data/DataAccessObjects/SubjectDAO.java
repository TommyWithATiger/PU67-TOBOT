package data.DataAccessObjects;

import data.Subject;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class SubjectDAO extends AbstractBaseDAO<Subject, Integer> {

  public SubjectDAO(EntityManagerFactory emFactory) {
    super(Subject.class, emFactory);
  }

  protected static SubjectDAO instance;

  public List<Subject> findTopicByTitle(String title) {
    return super.find("findSubjectByTitle","title", title);
  }

  public Subject findSingleTopicByTitle(String title) {
    List<Subject> results = findTopicByTitle(title);
    if (results != null) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
  }

  public static SubjectDAO getInstance() {
    return instance;
  }

  public static SubjectDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new SubjectDAO(emFactory);
    }
    return instance;
  }

}
