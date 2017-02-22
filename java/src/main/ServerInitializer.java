package main;

import data.DataAccessObjects.SubjectDAO;
import data.DataAccessObjects.TopicDAO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerInitializer {

  public static void main(String[] args) {
    setup();

  }

  public static void setup(){
    System.setProperty("javax.xml.accessExternalDTD", "all");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");

    TopicDAO topicDAO = TopicDAO.getInstance(entityManagerFactory);
    SubjectDAO subjectDAO = SubjectDAO.getInstance(entityManagerFactory);

  }
}
