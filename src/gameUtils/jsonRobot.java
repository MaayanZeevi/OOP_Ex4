package gameUtils;

import com.google.gson.Gson;

public class jsonRobot {
    String pos;
    int id;
    int dest;
    int src;
    int value;
    /*
     * Constructor for a temporary robot from json String.
     */
    public jsonRobot(String json) {
        Gson gson = new Gson();
        jsonRobot temp = gson.fromJson(json, jsonRobot.class);
        this.pos = temp.pos;
        this.id = temp.id;
        this.dest = temp.dest;
        this.src = temp.src;
        this.value = temp.value;
    }
    /*
       * return dest of the temporary robot
       */
    public int getdest(){
        return this.dest;
    }
    /*
           * return src of the temporary robot
           */
    public int getsrc(){
        return this.src;
    }

    /*
       * return id of the temporary robot
       */
    public int getid(){
        return this.id;
    }
    /*
           * return value of the temporary robot
           */
    public int getValue(){
        return this.value;
    }
}
