package data.dao;

import data.dao.util.FieldTuple;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractBaseDAO<E, K> {

  private Class<E> entityClass;

  @PersistenceContext
  static EntityManagerFactory emFactory;

  /**
   * Instantiates a DAO, Only called through sub-classes
   *
   * @param entityClass, Class type of the entity this DAO manages
   * @param entityManagerFactory, the static EntityManagerFactory for the server instance
   */
  AbstractBaseDAO(Class<E> entityClass, EntityManagerFactory entityManagerFactory) {
    emFactory = entityManagerFactory;
    this.entityClass = entityClass;
  }

  /**
   * Sends entity to be persisted in the database
   *
   * @param entity, the entity to be persisted
   **/
  public void persist(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.persist(entity);
    entityTransaction.commit();
    entityManager.close();
  }

  /**
   * Merges entity with the equivalent entity in the database
   *
   * @param entity, the entity to be merged
   **/
  public E merge(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.merge(entity);
    entityTransaction.commit();
    entityManager.close();
    return entity;
  }

  /**
   * Removes entity from the database
   *
   * @param entity, the entity to be removed
   **/
  public void remove(E entity) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    entityManager.remove(entityManager.merge(entity));
    entityTransaction.commit();
    entityManager.close();
  }

  /**
   * Returns an entity given its key value
   *
   * @param id, the identifying key value of the identity
   **/
  public E findById(K id) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    E entity = entityManager.find(entityClass, id);
    entityTransaction.commit();
    entityManager.close();
    return entity;
  }

  /**
   * Returns a list of entities that matches the query
   * This method is extended into specialized ones
   *
   * @param namedQueryString, the query to be performed
   * @param fieldTuples, varargs of tuples consisting of the name of the field the query is executed on, and
   * the value that is looked for in the given field
   * @return A list of entities that matches the query, null if there is none
   **/
  public List<E> find(String namedQueryString, FieldTuple... fieldTuples) {
    EntityManager entityManager = emFactory.createEntityManager();
    TypedQuery<E> query = entityManager.createNamedQuery(namedQueryString, entityClass);
    setParameters(query, fieldTuples);
    return executeQuery(query, entityManager);
  }

  /**
   * Returns the first n results that matches the query. This method should only be used on queries
   * where the order of the results is well defined, like ordered queries.
   *
   * @param namedQueryString, the query to be performed
   * @param limit, the maximum number of results to get
   * @param fieldTuples, varargs of tuples consisting of the name of the field the query is executed on, and
   * the value that is looked for in the given field
   * @return The first entity that matches the query, null if there is none
   **/
  List<E> findLimited(String namedQueryString, int limit, FieldTuple... fieldTuples) {
    EntityManager entityManager = emFactory.createEntityManager();
    TypedQuery<E> query = entityManager.createNamedQuery(namedQueryString, entityClass);
    query.setMaxResults(limit);
    setParameters(query, fieldTuples);
    return executeQuery(query, entityManager);
  }

  /**
   * Returns the first result that matches the query.This method should only be used on queries
   * where the first result is well defined, like ordered queries, or queries that should only have
   * a single match.
   *
   * @param namedQueryString, the query to be performed
   * @param fieldTuples, varargs of tuples consisting of the name of the field the query is executed on, and
   * the value that is looked for in the given field
   * @return The first entity that matches the query, null if there is none
   **/
  E findSingle(String namedQueryString, FieldTuple... fieldTuples) {
    List<E> resultList = findLimited(namedQueryString, 1, fieldTuples);
    if (resultList.size() == 0){
      return null;
    } else {
      return resultList.get(0);
    }
  }

  /**
   * Helper method to set the parameters of a query
   *
   * @param query, the query to be performed
   * @param fieldTuples, varargs of tuples consisting of the name of the field the query is executed on, and
   * the value that is looked for in the given field
   **/
  private void setParameters(TypedQuery<E> query, FieldTuple... fieldTuples){
    for(FieldTuple fieldTuple : fieldTuples){
      query.setParameter(fieldTuple.fieldName, fieldTuple.fieldValue);
    }
  }

  /**
   * Helper method to get a list of entities that matches the query
   *
   * @param query, the query to be performed
   * @param entityManager, the EntityManager that the query is attached to
   * @return A list of entities that matches the query, null if there is none
   **/
  private List<E> executeQuery(TypedQuery<E> query, EntityManager entityManager){
    List<E> entityList;
    try {
      entityList = query.getResultList();
      entityManager.close();
    } catch (Exception e) {
      e.printStackTrace();
      entityList = null;
    }
    return entityList;
  }

}
