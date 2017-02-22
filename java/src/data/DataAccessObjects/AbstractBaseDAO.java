package data.DataAccessObjects;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractBaseDAO<E, K> {

  private Class<E> entityClass;

  @PersistenceContext
  private static EntityManagerFactory emFactory;

  AbstractBaseDAO(Class<E> entityClass, EntityManagerFactory entityManagerFactory) {
    emFactory = entityManagerFactory;
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

  E merge(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.merge(entity);
    entityTransaction.commit();
    entityManager.close();
    return entity;
  }

  void remove(E entity) {
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

  List<E> find(String namedQueryString, String fieldName, String fieldValue) {
    List<E> entityList;
    try {
      EntityManager entityManager = emFactory.createEntityManager();
      TypedQuery<E> query = entityManager.createNamedQuery(namedQueryString, entityClass);
      query.setParameter(fieldName, fieldValue);
      entityList = query.getResultList();
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
  }

}
