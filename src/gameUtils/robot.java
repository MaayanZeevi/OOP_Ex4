package gameUtils;

import com.google.gson.Gson;
import utils.Point3D;

public class robot {



	public Point3D location;
	public String image;
	public int dest;
	public int id;
	public int src;
	public int points;

	/*
	 * Constructs the robot from a given json string.
	 */
	public robot(String json) {
		json = json.substring(9, json.length()-1);
		jsonRobot temp = new jsonRobot(json);
		this.location = new Point3D(temp.pos);
		this.image = "Images/robot.png";
		this.dest = temp.dest;
		this.id = temp.id;
		this.src = temp.src;
		this.points = temp.value;
	}

	public Point3D getLocation() { // return the location of the Pikachu as a 3D point
		return location;
	}

	public String getImage() {
		return image;
	}
	public int getDest() { //return the id (key) of the destination vertex (node).
		//If there is no destination return -1
		return dest;
	}

	//return the id of the robot.
	//(sometimes there is more then one Pikachu on the graph).
	public int getId() {
		return id;
	}

	public int getSrc() {
		return src;
	}

	public int getPoints() {
		return points;
	}
}