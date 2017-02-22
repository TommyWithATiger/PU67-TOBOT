package main;

import data.DataAccessObjects.SubjectDAO;
import data.DataAccessObjects.TopicDAO;
import data.Topic;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerInitializer {

  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // TODO: do actual stuff here
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
    TopicDAO topicDAO = TopicDAO.getInstance(entityManagerFactory);
    SubjectDAO subjectDAO = SubjectDAO.getInstance(entityManagerFactory);

    Topic topic = new Topic("Test2", "do more things");
    topicDAO.persist(topic);

  }
}
