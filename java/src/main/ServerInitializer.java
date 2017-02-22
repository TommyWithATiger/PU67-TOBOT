package main;

import data.DataAccessObjects.SubjectDAO;
import data.DataAccessObjects.TopicDAO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import server.connection.SocketHandler;

public class ServerInitializer {

  public static void main(String[] args) throws InterruptedException {

    setup();


    // Place all setup before this call, this will run forever
    SocketHandler server = new SocketHandler();
    server.start();

    Thread.sleep(Long.MAX_VALUE);

    server.stopServer();
    // Setup calls

  }

  public static void setup(){
    System.setProperty("javax.xml.accessExternalDTD", "all");

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");

    TopicDAO topicDAO = TopicDAO.getInstance(entityManagerFactory);
    SubjectDAO subjectDAO = SubjectDAO.getInstance(entityManagerFactory);

  }
}
