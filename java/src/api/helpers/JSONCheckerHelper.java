package api.helpers;

import api.exceptions.APIBadRequestException;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONCheckerHelper {

  public static JSONObject checkAndGetJSON(String JSONString) throws APIBadRequestException {
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(JSONString);
    } catch (JSONException je) {
      throw new APIBadRequestException("JSON is malformed");
    }
    return jsonObject;
  }

}
