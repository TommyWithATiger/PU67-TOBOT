package main;

import data.DataAccessObjects.TopicDAO;
import data.Topic;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerInitializer {

  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // TODO: do actual stuff here
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
    TopicDAO topicDAO = Topic.initialize(entityManagerFactory);

    Topic topic = new Topic("Dunno", "do more things");
    topicDAO.persist(topic);

  }
}
