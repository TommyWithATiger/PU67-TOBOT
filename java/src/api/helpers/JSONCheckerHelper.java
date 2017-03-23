package api.helpers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
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
   * A helper for checking if the given HttpRequest contains the required field in the JSON,
   * and getting the field if it exists
   * @param httpRequest The HttpRequest to check
   * @param field Field to get
   * @param tClass Class of the value in the field
   * @throws APIBadRequestException Thrown if the JSONObject does not contain the field, or the value
   * of the field is not of the correct type.
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

  /**
   * A helper for checking if the given HttpRequest contains the required field in the JSON,
   * and getting the values in the field as a list if the field exists
   * @param httpRequest The HttpRequest to check
   * @param field Field to get
   * @param tClass Class of the values in the field
   * @throws APIBadRequestException Thrown if the JSONObject does not contain the field, or the field
   * is not an array where all the elements are of the correct type.
   * @throws APIErrorException Thrown if there is a IOException while reading the content
   * @return Value of field, casted to tClass
   */
  public static <T> List<T> getJSONArray(HttpRequest httpRequest, Class<T> tClass,
      String field) throws APIBadRequestException, APIErrorException{
    String requestContent = checkAndGetEntityContent(httpRequest);
    JSONObject jsonObject = checkAndGetJSON(requestContent);
    if (!jsonObject.has(field)){
      throw new APIBadRequestException(field + " must be set");
    }
    try{
      ArrayList<T> ret = new ArrayList<>();
      jsonObject.getJSONArray(field).iterator().forEachRemaining(o -> ret.add(tClass.cast(o)));
      return ret;
    }
    catch (ClassCastException cce){
      throw new APIBadRequestException(field + " must be array of " + tClass.getTypeName());
    }
  }

}
