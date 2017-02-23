package data.DataAccessObjects.util;

public class FieldTuple {

  public final String fieldName;
  public final Object fieldValue;

  /**
   * Creates a FieldTuple, that combines the name of a field with the value of a field
   *
   * @param fieldName, a field to be matched in a query
   * @param fieldValue, the value of the field
   */
  public FieldTuple(String fieldName, Object fieldValue){
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

}
