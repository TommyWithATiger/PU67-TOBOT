package api.helpers;

import api.exceptions.APIBadRequestException;
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
   * A helper for checking if the given JSONObject contains the required fields
   * @param jsonObject The JSONObject to check
   * @param fields Varargs of fields that must be set
   * @throws APIBadRequestException Thrown if the JSONObject does not contain one of the fields
   */
  public static void requireJSONFields(JSONObject jsonObject, String... fields) throws APIBadRequestException {
    for(String field : fields){
      if (!jsonObject.has(field)){
        throw new APIBadRequestException(field + " must be set");
      }
    }
  }

}
