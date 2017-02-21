package data.DataAccessObjects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

public abstract class DAOBase<E, K> {

  protected Class<E> entityClass;

  protected EntityManagerFactory emFactory;
  @PersistenceContext
  protected EntityManager entityManager;

  public DAOBase(Class<E> entityClass, EntityManagerFactory emFactory) {
    this.emFactory = emFactory;
    this.entityManager = emFactory.createEntityManager();
    this.entityClass = entityClass;
  }

  public void persist(E entity) {
    entityManager.persist(entity);
  }

  public void remove(E entity) {
    entityManager.remove(entity);
  }

  public E findById(K id) {
    return entityManager.find(entityClass, id);
  }

}
