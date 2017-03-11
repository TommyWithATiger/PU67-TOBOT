package api.helpers;

import static api.helpers.EntityContentHelper.checkAndGetEntityContent;

import api.exceptions.APIBadRequestException;
import api.exceptions.APIErrorException;
import java.util.ArrayList;
import java.util.List;
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
   * @param fields Varargs of fields that must be set
   * @param tClass Class of the values in the fields
   * @throws APIBadRequestException Thrown if the JSONObject does not contain one of the fields
   * @throws APIErrorException Thrown if there is a IOException while reading the content
   * @returns List of tClass corresponding to the fields in the fields argument
   */
  public static <T> List<T> getJSONFields(HttpRequest httpRequest, Class<T> tClass,
      String... fields) throws APIBadRequestException, APIErrorException{
    String requestContent = checkAndGetEntityContent(httpRequest);
    JSONObject jsonObject = checkAndGetJSON(requestContent);
    return getJSONFields(jsonObject, tClass, fields);
  }

  /**
   * A helper for checking if the given JSONObject contains the required fields in the JSON,
   * and getting the fields if they all exist
   * @param jsonObject The HttpRequest to check
   * @param fields Varargs of fields that must be set
   * @param tClass Class of the values in the fields
   * @throws APIBadRequestException Thrown if the JSONObject does not contain one of the fields
   * @returns List of tClass corresponding to the fields in the fields argument
   */
  public static <T> List<T> getJSONFields(JSONObject jsonObject, Class<T> tClass, String... fields)
      throws APIBadRequestException {
    List<T> ret = new ArrayList<>();
    for(String field : fields){
      if (!jsonObject.has(field)){
        throw new APIBadRequestException(field + " must be set");
      }
      try{
        ret.add(tClass.cast(jsonObject.get(field)));
      }
      catch (ClassCastException cce){
        throw new APIBadRequestException(field + " must be " + tClass.getTypeName());
      }
    }
    return ret;
  }

}
