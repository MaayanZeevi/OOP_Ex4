package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class NodeData implements node_data, Serializable {
	private int key;
	private double weight;
	private Point3D location;
	private String info;
	private int tag;


	/**
	 * constractor
	 */
	public NodeData() {
		this.key=0;
		this.weight=0;
		this.location=null;
		this.info="";
		this.tag=0;
	}
	/**
	 constractor
	 */
	public NodeData(int key,double x, double y) {
		this.key = key;
		this.location = new Point3D (x,y);
	}


	public NodeData(int key, Point3D point3d) {
		// TODO Auto-generated constructor stub

		this.key = key;
		this.location = point3d;
	}
	/**
	 * Return the key (id) associated with this node.
	 * @return
	 */
	public int getKey() {
		return this.key;
	}

	/** Return the location (of applicable) of this node, if
	 * none return null.
	 *
	 * @return
	 */
	public Point3D getLocation() {
		return this.location;
	}

	/** Allows changing this node's location.
	 *
	 * @param p - new new location  (position) of this node.
	 */
	public void setLocation(Point3D p) {
		this.location = p;

	}
	/**
	 * Return the weight associated with this node.
	 * @return
	 */

	public double getWeight() {
		return this.weight;
	}

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	public void setWeight(double w) {
		this.weight = w;
	}

	/**
	 * return the remark (meta data) associated with this node.
	 * @return
	 */
	public String getInfo() {
		return this.info;
	}


	public void setInfo(String s) {
		this.info = s;
	}
	/**
	 * Temporal data (aka color: e,g, white, gray, black)
	 * which can be used be algorithms
	 * @return
	 */

	public int getTag() {
		return this.tag;
	}

	/**
	 * Allow setting the "tag" value for temporal marking an node - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	public void setTag(int t) {
		this.tag = t;
	}

	public String toString() {
		return ""+this.getKey();
	}
}
