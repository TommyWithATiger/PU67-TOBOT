package data.managers;

import data.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TopicManager {

  public void addTopic(Topic topic) {
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
    EntityManager entitymanager = emfactory.createEntityManager();

    entitymanager.getTransaction().begin();
    entitymanager.persist(topic);
    entitymanager.getTransaction().commit();

    entitymanager.close();
    emfactory.close();
  }

  public Topic findTopic(int id) {
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
    EntityManager entitymanager = emfactory.createEntityManager();

    Topic topic = entitymanager.find(Topic.class, id);
    entitymanager.close();

    emfactory.close();
    return topic;
  }

  public void updateTopic(Topic topic) {
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
    EntityManager entitymanager = emfactory.createEntityManager( );

    entitymanager.getTransaction( ).begin( );
    entitymanager.getTransaction( ).commit( );
    entitymanager.close();

    emfactory.close();
  }

  public void deleteTopic(Topic topic) {
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
    EntityManager entitymanager = emfactory.createEntityManager( );

    entitymanager.getTransaction( ).begin( );
    entitymanager.remove( topic );
    entitymanager.getTransaction( ).commit( );

    entitymanager.close( );
    emfactory.close( );
  }
}
