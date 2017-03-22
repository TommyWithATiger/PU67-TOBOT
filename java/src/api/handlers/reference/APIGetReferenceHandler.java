package api.handlers.reference;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;

import api.exceptions.APIBadRequestException;
import data.Topic;
import data.dao.ReferenceDAO;
import data.dao.TopicDAO;
import data.reference.Reference;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class APIGetReferenceHandler {

  /**
   * An API handler for getting information about a reference given its id. Require the following data:
   * id (int): the reference id
   *
   * @param httpRequest The request to handle
   * @return A JSON object on the form of the reference.createAbout method.
   */
  public static String getReferenceById(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    Reference reference = ReferenceDAO.getInstance()
        .findById(getIntegerURIField(httpRequest, "id"));

    if (reference == null) {
      throw new APIBadRequestException("No reference with the given id");
    }

    return reference.createAbout().toString();
  }

  /**
   * An API handler for getting information about references related to a topic. Require the
   * following data:
   * topic (int): the id of the topic to get references for
   *
   * @param httpRequest The request to handle
   * @return A JSON object with the variable references which is an array of JSON objects on the
   * form of the reference.createAbout method for each of the references.
   */
  public static String getReferencesByTopic(HttpRequest httpRequest) {
    checkRequestMethod("GET", httpRequest);

    Topic topic = TopicDAO.getInstance().findById(getIntegerURIField(httpRequest, "topic"));

    if (topic == null) {
      throw new APIBadRequestException("No topic with the given id");
    }

    List<Reference> references = ReferenceDAO.getInstance()
        .findReferenceByTag(topic);

    if (references == null) {
      throw new APIBadRequestException("Could not get related references");
    }

    JSONObject response = new JSONObject();
    response.put("references", references.stream().map(Reference::createAbout).collect(Collectors.toList()));

    return response.toString();
  }
}
