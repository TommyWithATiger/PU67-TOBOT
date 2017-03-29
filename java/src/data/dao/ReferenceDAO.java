package data.dao;

import data.Topic;
import data.dao.util.FieldTuple;
import data.reference.Reference;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ReferenceDAO extends AbstractBaseDAO<Reference, Integer> {

  private static ReferenceDAO instance;

  /**
   * Instantiates the ReferenceDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public ReferenceDAO(EntityManagerFactory emFactory) {
    super(Reference.class, emFactory);
  }

  /**
   * Finds all references in the database
   *
   * @return List of Reference objects
   */
  public List<Reference> findAll() {
    return super.find("findAllReferences");
  }

  /**
   * Finds References related to a topic
   *
   * @param tag, the related topic object
   * @return List of Reference objects
   */
  public List<Reference> findReferenceByTag(Topic tag) {
      return super.find("findReferencesByTag",
          new FieldTuple("tag", tag));
  }

  /**
   * Returns the static ReferenceDAO instance
   *
   * @return ReferenceDAO, null if not instantiated
   */
  public static ReferenceDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static ReferenceDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !ReferenceDAO.emFactory.isOpen()) {
      instance = new ReferenceDAO(emFactory);
    }
  }

}
