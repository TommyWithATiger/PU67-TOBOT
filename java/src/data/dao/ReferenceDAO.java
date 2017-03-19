package data.dao;

import data.Topic;
import data.reference.Reference;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

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
    List<Reference> entityList;
    try {
      EntityManager entityManager = emFactory.createEntityManager();
      Query query = entityManager.createNativeQuery(
          "SELECT * FROM Reference WHERE id IN (SELECT Reference_id FROM REFERENCE_TAGS WHERE tags_id = ?)",
          Reference.class);
      query.setParameter(1, tag.getId());
      entityList = (List<Reference>) query.getResultList(); //FIXME find a better fix
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
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
