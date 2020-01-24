package gameUtils;

import utils.Point3D;

public class Fruit {

	public Point3D location;
	public String image;
	public double value;
	public int type;

	/*
     * Builds the object fruit from a json String
     */
	public Fruit(String json) { //read from json file
		json = json.substring(9, json.length()-1);
		jsonFruit temp = new jsonFruit(json);
		this.location = new Point3D(temp.pos);
		this.value = temp.value;
		this.type = temp.type;
		if (temp.type == -1) {
			this.image = "Images/banana.png";
		}
		else {
			this.image = "Images/apple.png";
		}
	}


/*
     * return location of fruit Point3D
     */

	public Point3D getLocation() {
		return location;
	}
	/*
         * return immage of fruit apple or banana
         */
	public String getImage() {
		return image;
	}
}