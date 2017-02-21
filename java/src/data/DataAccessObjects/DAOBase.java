package data.DataAccessObjects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

public abstract class DAOBase<E, K> {

  protected Class<E> entityClass;

  @PersistenceContext
  protected static EntityManagerFactory emFactory;

  public DAOBase(Class<E> entityClass, EntityManagerFactory entityManagerFactory) {
    this.emFactory = entityManagerFactory;
    this.entityClass = entityClass;
  }

  public void persist(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.persist(entity);
    entityTransaction.commit();
    entityManager.close();
  }

  public E merge(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.merge(entity);
    entityTransaction.commit();
    entityManager.close();
    return entity;
  }

  public void remove(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.remove(entity);
    entityTransaction.commit();
    entityManager.close();
  }

  public E findById(K id) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    E entity = entityManager.find(entityClass, id);
    entityTransaction.commit();
    entityManager.close();
    return entity;
  }

}
