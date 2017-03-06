package api.handlers.subject;

import static api.handlers.subject.APIGetSubjectHandler.getAllSubjects;
import static api.handlers.subject.APIGetSubjectHandler.getSubjectByID;
import static api.handlers.subject.APIGetSubjectHandler.getSubjectsByTitle;
import static org.junit.Assert.*;

import api.exceptions.APIBadMethodException;
import api.exceptions.APIBadRequestException;
import base.BaseTest;
import data.Subject;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;

public class APIGetSubjectHandlerTest extends BaseTest {

  private Subject subject;

  @Before
  public void setUp() throws Exception {
    subject = new Subject("Test title", "IDI-NTNU", "TDT4100", "Test description");
    subject.create();
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetAllSubjectsWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "subjects/all/url");
    getAllSubjects(httpRequest);
  }

  @Test
  public void testGetAllSubjects() {
    Subject subject2 = new Subject("Test subject 2", "Institute", "MA1201", "Something");
    subject2.create();

    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/all/url");
    String response = getAllSubjects(httpRequest);
    assertEquals(
        "{\"subjects\":[{\"institution\":\"Institute\",\"description\":\"Something\",\"id\":"
            + subject2.getId()
            + ",\"title\":\"Test subject 2\",\"subjectCode\":\"MA1201\"},{\"institution\":\"IDI-NTNU\",\"description\":\"Test description\",\"id\":"
            + subject.getId()
            + ",\"title\":\"Test title\",\"subjectCode\":\"TDT4100\"}]}",
        response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetSubjectsByTitleWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "subjects/title/url");
    getSubjectsByTitle(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectsByTitleNoUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/title/url");
    getSubjectsByTitle(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectsByTitleWrongUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/title/url?test=test");
    getSubjectsByTitle(httpRequest);
  }

  @Test
  public void testGetSubjectsByTitle() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/title/url?title=Test title");
    String response = getSubjectsByTitle(httpRequest);
    assertEquals(
        "{\"subjects\":[{\"institution\":\"IDI-NTNU\",\"description\":\"Test description\",\"id\":"
            + subject.getId() + ",\"title\":\"Test title\",\"subjectCode\":\"TDT4100\"}]}",
        response);
  }

  @Test
  public void testGetSubjectsByTitleNonApplicable() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/title/url?title=abcd");
    String response = getSubjectsByTitle(httpRequest);
    assertEquals("{\"subjects\":[]}", response);
  }

  @Test
  public void testGetSubjectsByTitlePartialTitle() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subjects/title/url?title=Test");
    String response = getSubjectsByTitle(httpRequest);
    assertEquals(
        "{\"subjects\":[{\"institution\":\"IDI-NTNU\",\"description\":\"Test description\",\"id\":"
            + subject.getId() + ",\"title\":\"Test title\",\"subjectCode\":\"TDT4100\"}]}",
        response);
  }

  @Test(expected = APIBadMethodException.class)
  public void testGetSubjectByIDWrongMethod() {
    HttpRequest httpRequest = new BasicHttpRequest("POST", "subject/id/url");
    getSubjectByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectByIDNoUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subject/id/url");
    getSubjectByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectByIDWrongUrlArguments() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subject/id/url?test=test");
    getSubjectByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectByIDIDNotInteger() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subject/id/url?id=test");
    getSubjectByID(httpRequest);
  }

  @Test(expected = APIBadRequestException.class)
  public void testGetSubjectByIDIDNotValid() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subject/id/url?id=-12");
    getSubjectByID(httpRequest);
  }

  @Test
  public void testGetSubjectByID() {
    HttpRequest httpRequest = new BasicHttpRequest("GET", "subject/id/url?id=" + subject.getId());
    String response = getSubjectByID(httpRequest);
    assertEquals("{\"institution\":\"IDI-NTNU\",\"description\":\"Test description\",\"id\":"
            + subject.getId() + ",\"title\":\"Test title\",\"subjectCode\":\"TDT4100\"}",
        response);
  }

}