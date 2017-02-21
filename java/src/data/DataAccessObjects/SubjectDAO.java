package data.DataAccessObjects;

import data.Subject;
import javax.persistence.EntityManagerFactory;

public class SubjectDAO extends DAOBase<Subject, Integer> {

  public SubjectDAO(EntityManagerFactory entityManagerFactory) {
    super(Subject.class, entityManagerFactory);
  }

}
