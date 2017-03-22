package api.handlers.reference;

import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.UrlArgumentHelper.getIntegerURIField;

import api.exceptions.APIBadRequestException;
import data.dao.ReferenceDAO;
import data.reference.Reference;
import org.apache.http.HttpRequest;

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
}
