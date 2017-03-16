package api.helpers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONCheckerHelper {

  /**
   * A helper for checking if the given string is correct JSON
   * @param JSONString The string to check
   * @return A JSON object based on the string
   * @throws APIBadRequestException Thrown if the string is not a correct JSON string
   */
  public static JSONObject checkAndGetJSON(String JSONString) throws APIBadRequestException {
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(JSONString);
    } catch (JSONException je) {
      throw new APIBadRequestException("JSON is malformed");
    }
    return jsonObject;
  }

  /**
   * A helper for checking if the given HttpRequest contains the required fields in the JSON,
   * and getting the fields if they all exist
   * @param httpRequest The HttpRequest to check
   * @param field Field to get
   * @param tClass Class of the values in the fields
   * @throws APIBadRequestException Thrown if the JSONObject does not contain one of the fields
   * @throws APIErrorException Thrown if there is a IOException while reading the content
   * @return Value of field, casted to tClass
   */
  public static <T> T getJSONField(HttpRequest httpRequest, Class<T> tClass,
      String field) throws APIBadRequestException, APIErrorException{
    String requestContent = checkAndGetEntityContent(httpRequest);
    JSONObject jsonObject = checkAndGetJSON(requestContent);
    if (!jsonObject.has(field)){
      throw new APIBadRequestException(field + " must be set");
    }
    try{
      return tClass.cast(jsonObject.get(field));
    }
    catch (ClassCastException cce){
      throw new APIBadRequestException(field + " must be " + tClass.getTypeName());
    }
  }

}
