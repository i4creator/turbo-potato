package com.LukeGackle;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

@DesignerComponent(version = 0, description = "A collection of methods for reading JSON Arrays. Developed by Luke Gackle.", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "https://1.bp.blogspot.com/-d-xyqbKFyAY/WSDvpMEG-tI/AAAAAAABYTk/I9gjYEgABZYxjwi2pzmlqbvQg6eMJhSeQCLcB/s1600/ExtensionsIcons.png")
@SimpleObject(external = true)
public class JSONTools extends AndroidNonvisibleComponent implements Component {
  public static final int VERSION = 0;
  
  public static final String DEVELOPER = "Luke Gackle";
  
  private ComponentContainer container;
  
  private static final String LOG_TAG = "JSONToolsExtension-0-Luke Gackle";
  
  private JSONObject rootObject;
  
  private JSONArray rootArray;
  
  private JSONObject currentObject;
  
  private JSONArray currentArray;
  
  private static final int ROOT = 1;
  
  private static final int CURRENT_OBJECT = 2;
  
  private static final int CURRENT_ARRAY = 3;
  
  private static final int ROOT_TYPE_ARRAY = 1;
  
  private static final int ROOT_TYPE_OBJECT = 2;
  
  private int currentPosition;
  
  private int rootType;
  
  public JSONTools(ComponentContainer container) {
    super(container.$form());
    this.container = container;
  }
  
  @SimpleFunction(description = "Parse JSON takes a String and internally creates a JSON Object or Array. ParseJSON also internally establishes this Array or Object as the root.")
  public void ParseJSON(String JSONString) throws JSONException {
    try {
      Object json = (new JSONTokener(JSONString)).nextValue();
      if (json instanceof JSONObject) {
        this.rootObject = new JSONObject(JSONString);
        this.rootType = 2;
      } else if (json instanceof JSONArray) {
        this.rootArray = new JSONArray(JSONString);
        this.rootType = 1;
      } 
      this.currentPosition = 1;
    } catch (Exception e) {
      throw new YailRuntimeError("Argument must be valid JSON", "Check your JSON and try again.");
    } 
  }
  
  @SimpleFunction(description = "Use this method to open a JSON Object denoted by curly braces. This opens a JSON Object from the parsed JSON String. To open an Object inside of the current Array or Object use the \"OpenSubJSONObject\" block.")
  public void OpenJSONObject(String JSONObjectName) throws JSONException {
    if (this.rootType == 1) {
      this.currentObject = this.rootArray.getJSONObject(0).getJSONObject(JSONObjectName);
    } else if (this.rootType == 2) {
      this.currentObject = this.rootObject.getJSONObject(JSONObjectName);
    } 
    this.currentPosition = 2;
  }
  
  @SimpleFunction(description = "Use this method to open a JSON Array denoted by square brackets. This opens a JSON Array from the parsed JSON String. To open an Array inside of the current Array or Object use the \"OpenSubJSONArray\" block.")
  public void OpenJSONArray(String JSONArrayName) throws JSONException {
    if (this.rootType == 1) {
      this.currentArray = this.rootArray.getJSONObject(0).getJSONArray(JSONArrayName);
    } else if (this.rootType == 2) {
      this.currentArray = this.rootObject.getJSONArray(JSONArrayName);
    } 
    this.currentPosition = 3;
  }
  
  @SimpleFunction(description = "Opens a sub array inside the current JSON array or Object. If the current object is a JSON array it will open the sub array at the specified index. If the current object is a JSON Object then the index does nothing. NOTE: If you use this method inside a for loop note that it will affect the method \"GetCurrentArrayLength\", be sure to set the array back to the desired array using the block \"OpenJSONArray\".")
  public void OpenSubJSONArray(String JSONArrayName, int index) throws JSONException {
    if (this.currentPosition == 3) {
      this.currentArray = this.currentArray.getJSONObject(index - 1).getJSONArray(JSONArrayName);
    } else if (this.currentPosition == 2) {
      this.currentArray = this.currentObject.getJSONArray(JSONArrayName);
    } 
    if (this.currentPosition != 1)
      this.currentPosition = 3; 
  }
  
  @SimpleFunction(description = "")
  public void OpenSubJSONObject(String JSONObjectName) throws JSONException {
    if (this.currentPosition == 3) {
      this.currentObject = this.currentArray.getJSONObject(0).getJSONObject(JSONObjectName);
    } else if (this.currentPosition == 2) {
      this.currentObject = this.currentObject.getJSONObject(JSONObjectName);
    } 
    if (this.currentPosition != 1)
      this.currentPosition = 2; 
  }
  
  @SimpleFunction(description = "Retrieves the String value for the given attribute for the first occurance in the current JSON Object or Array. If the current JSON type is Array the index defaults to 1(The first item in the array) If you need to get the value from other indexes use the method \"GetStringInArray\".")
  public String GetStringValue(String attributeName) throws JSONException {
    if (this.currentPosition == 1) {
      if (this.rootType == 1)
        return this.rootArray.getJSONObject(0).getString(attributeName).toString(); 
      if (this.rootType == 2)
        return this.rootObject.getString(attributeName).toString(); 
    } else {
      if (this.currentPosition == 3)
        return this.currentArray.getJSONObject(0).getString(attributeName).toString(); 
      if (this.currentPosition == 2)
        return this.currentObject.getString(attributeName).toString(); 
    } 
    return "";
  }
  
  @SimpleFunction(description = "Retrieves the int value for the given attribute for the first occurance in the current JSON Object or Array. If the current JSON type is Array the index defaults to 1(The first item in the array) If you need to get the value from other indexes use the method \"GetStringInArray\".")
  public int GetIntValue(String attributeName) throws JSONException {
    if (this.currentPosition == 1) {
      if (this.rootType == 1)
        return this.rootArray.getJSONObject(0).getInt(attributeName); 
      if (this.rootType == 2)
        return this.rootObject.getInt(attributeName); 
    } else {
      if (this.currentPosition == 3)
        return this.currentArray.getJSONObject(0).getInt(attributeName); 
      if (this.currentPosition == 2)
        return this.currentObject.getInt(attributeName); 
    } 
    return -1;
  }
  
  @SimpleFunction(description = "Retrieves the int value for the given attribute for the first occurance in the current JSON Object or Array. If the current JSON type is Array the index defaults to 1(The first item in the array) If you need to get the value from other indexes use the method \"GetStringInArray\".")
  public boolean GetBooleanValue(String attributeName) throws JSONException {
    if (this.currentPosition == 1) {
      if (this.rootType == 1)
        return this.rootArray.getJSONObject(0).getBoolean(attributeName); 
      if (this.rootType == 2)
        return this.rootObject.getBoolean(attributeName); 
    } else {
      if (this.currentPosition == 3)
        return this.currentArray.getJSONObject(0).getBoolean(attributeName); 
      if (this.currentPosition == 2)
        return this.currentObject.getBoolean(attributeName); 
    } 
    return false;
  }
  
  @SimpleFunction(description = "Retrieves the value for the given attribute for the given index in the JSON array. To bring this inline with App Inventor conventions, the index starts at 1.")
  public String GetStringInArray(String JSONArrayAttribute, int index) throws JSONException {
    if (index <= 0 || index > GetCurrentArrayLength() + 1)
      return ""; 
    try {
      if (this.currentPosition == 1) {
        if (this.rootType == 1)
          return this.rootArray.getJSONObject(index - 1).getString(JSONArrayAttribute).toString(); 
      } else if (this.currentPosition == 3) {
        return this.currentArray.getJSONObject(index - 1).getString(JSONArrayAttribute).toString();
      } 
    } catch (JSONException jSONException) {}
    return "";
  }
  
  @SimpleFunction(description = "Retrieves the value at the given index in the current JSON array. This method is for use when the elements in the array dont have a name to reference. To bring this inline with App Inventor conventions, the index starts at 1.")
  public String GetStringInArrayByIndex(int index) throws JSONException {
    if (index <= 0 || index > GetCurrentArrayLength() + 1)
      return ""; 
    try {
      if (this.currentPosition == 1) {
        if (this.rootType == 1)
          return this.rootArray.getString(index).toString(); 
      } else if (this.currentPosition == 3) {
        return this.currentArray.getString(index).toString();
      } 
    } catch (JSONException jSONException) {}
    return "";
  }
  
  @SimpleFunction(description = "This method can be used in cases where the JSON Object does not have a name to reference it, you can use the index parameter to specify at which index in the array the desired object is. You can then use the regular methods to get the attributes inside the object. JSON Arrays cannot be placed inside objects without a name and as such there is no method for getting an array by index.")
  public void OpenObjectInArrayByIndex(int index) throws JSONException {
    if (index <= 0 || index > GetCurrentArrayLength() + 1)
      return; 
    try {
      if (this.currentPosition == 1) {
        if (this.rootType == 1) {
          this.currentObject = this.rootArray.getJSONObject(index);
          this.currentPosition = 2;
        } 
      } else if (this.currentPosition == 3) {
        this.currentObject = this.currentArray.getJSONObject(index);
        this.currentPosition = 2;
      } 
    } catch (JSONException jSONException) {}
  }
  
  @SimpleFunction(description = "Internally sets the current position to the \"root\" JSON, in other words the JSON Array or Object that was originally parsed.")
  public void SetCurrentPositionToRoot() {
    this.currentPosition = 1;
  }
  
  @SimpleFunction(description = "Returns the value for the given attrubute, in the given sub array, without resetting the current location.")
  public String GetStringInSubJSONArray(int index, String JSONArrayName, String attributeName) throws JSONException {
    JSONArray tempSubArray = this.currentArray.getJSONObject(index - 1).getJSONArray(JSONArrayName);
    return tempSubArray.getJSONObject(0).getString(attributeName).toString();
  }
  
  @SimpleFunction(description = "")
  public String GetStringInSubJSONObject(int index, String JSONObjectName, String attributeName) throws JSONException {
    JSONObject tempSubObject = this.currentArray.getJSONObject(index - 1).getJSONObject(JSONObjectName);
    return tempSubObject.getString(attributeName).toString();
  }
  
  @SimpleFunction(description = "Returns the length of the current JSON Array, if the JSON type is Object, this method returns -1.")
  public int GetCurrentArrayLength() throws JSONException {
    if (this.currentPosition == 1) {
      if (this.rootType == 1)
        return this.rootArray.length(); 
    } else if (this.currentPosition == 3) {
      return this.currentArray.length();
    } 
    return -1;
  }
}
