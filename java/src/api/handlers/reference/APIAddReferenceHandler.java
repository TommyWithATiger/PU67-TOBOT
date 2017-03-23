package api.handlers.reference;


import static api.helpers.JSONCheckerHelper.getJSONField;
import static api.helpers.RequestMethodHelper.checkRequestMethod;
import static api.helpers.isLoggedInHelper.getUserFromRequest;

import api.exceptions.APIBadRequestException;
import data.reference.Reference;
import data.reference.ReferenceType;
import data.reference.ReferenceTypeConverter;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;

public class APIAddReferenceHandler {

  /**
   * An API handler for adding references. Requires the user to be logged in and the following data:
   *        title (String): the reference title
   *        description (String): the reference description
   *        link (String): the reference link
   *        type (String): the reference type, has 7 valid values
   *                      Article, Video, Website, Image, Document, Slide, Notes
   *
   * @param httpRequest The request to handle
   * @return A JSON string with the data from reference.createAbout
   */
  public static String handleAddReferenceRequest(HttpRequest httpRequest) {
    checkRequestMethod("POST", httpRequest);

    // User must be logged in
    getUserFromRequest(httpRequest, ", cannot create a new reference");

    String title = getJSONField(httpRequest, String.class, "title");
    String description = getJSONField(httpRequest, String.class, "description");
    String link = getJSONField(httpRequest, String.class, "link");
    ReferenceType type;
    try{
      type = ReferenceTypeConverter.stringToReferenceType(
        getJSONField(httpRequest, String.class, "type"));
    } catch (IllegalArgumentException iae) {
      throw new APIBadRequestException("Reference type is not valid");
    }

    try {
      Reference reference = new Reference(title, description, link, type);
      reference.create();
      return reference.createAbout().toString();
    } catch (MalformedURLException mue){
      //TODO return response with error
      throw new APIBadRequestException("Link is not an URL");
    } catch (IOException ioe){
      //TODO return response with error
      throw new APIBadRequestException("Cannot read/write to socket");
    } catch (HttpException he){
      //TODO return response with error
      throw new APIBadRequestException("Cannot connect to URL");
    } catch (IllegalArgumentException iae){
      //TODO return response with error
      throw new APIBadRequestException("URL is not properly formatted:" + iae.getMessage());
    }
  }
}
