package gameUtils;

import com.google.gson.Gson;

public class jsonFruit {


    String pos;
    int type;
    double value;
    /*
     * Builds a temporary fruit from a json string.
     */
    public jsonFruit(String json) {
        Gson gson = new Gson();
        jsonFruit temp = gson.fromJson(json, jsonFruit.class);
        this.pos = temp.pos;
        this.type = temp.type;
        this.value = temp.value;
    }
    /*
     * return location of temporary fruit
     */

    public String getpos(){
        return this.pos;
    }
    /*
     *return type of temporary fruit
     */
    public int gettype(){
        return this.type;
    }
     /*
     *return value of temporary fruit
     */


    public double getValue(){
        return this.value;
    }
}
