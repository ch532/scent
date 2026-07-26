package org.json;
public class JSONObject {
    public JSONObject() {}
    public JSONObject(String s) {}
    public void put(String k, Object v) {}
    public String getString(String k) { return ""; }
    public JSONArray getJSONArray(String k) { return new JSONArray(); }
    public String toString() { return "{}"; }
}
