
package algorithms;
import java.io.*;
import java.util.*;

import dataStructure.*;
import utils.Point3D;


public class Graph_Algo implements graph_algorithms, Serializable{
	private graph _graph;

	//constructors
	public Graph_Algo(graph g) {
		this.init(g);
	}

	//empty constructor
	public Graph_Algo() {

	}

	/**
	 * Init this set of algorithms on the parameter - graph.
	 * @param g
	 */
	@Override
	public void init(graph g) {
		_graph =  g;
	}

	/**
	 * Compute a deep copy of this graph.
	 * @return
	 */
	@Override
	public graph copy() {
//		graph copy= new DGraph();
//		Collection<node_data> nodes= _graph.getV();
//		for ( node_data node_data: nodes) {
//			node_data temp= new NodeData(node_data.getKey(), node_data.getWeight(),node_data.getLocation());
//			copy.addNode(temp);
//			Collection<edge_data> edges= _graph.getE(node_data.getKey());
//		}
//
//		for ( node_data node_data: nodes) {
//			node_data temp= new NodeData(node_data.getKey(), node_data.getWeight(),node_data.getLocation());
//			copy.addNode(temp);
//			Collection<edge_data> edges= _graph.getE(node_data.getKey());
//			for (edge_data edge_data : edges) {
//				int src= node_data.getKey();
//				int dest=edge_data.getDest();
//				double weight= edge_data.getWeight();
//				copy.connect(src, dest, weight);
//			}
//		}

		return _graph;
	}

	/**
	 * Init a graph from file
	 * @param file_name
	 */
	@Override
	public void init(String file_name) {
		Graph_Algo temp = null;
		try{
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);
			temp = (Graph_Algo)in.readObject();
			in.close();
			file.close();
		}
		catch(IOException ex) {
			System.out.println("IOException is caught");
			return;
		}
		catch(ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
			return;
		}
		_graph = temp._graph;
	}

	/** Saves the graph to a file.
	 *
	 * @param file_name
	 */
	@Override
	public void save(String file_name) {

		try{
			FileOutputStream file = new FileOutputStream(new File(file_name));
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this);
			out.close();
			file.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return
	 */
	public boolean isConnected() {
		Collection <node_data> nodes= _graph.getV();
		for (node_data node_data : nodes) {
			for (node_data node_data2 : nodes) {
				if(node_data.getKey()== node_data2.getKey());
				else {
					if (!isReachable(node_data.getKey(), node_data2.getKey()))return false;
				}
			}
		}
		return true;
	}

	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(!isReachable(src, dest)) {
			return Double.POSITIVE_INFINITY;
		}
		Collection <node_data> nodes= _graph.getV();
		for (node_data currNode : nodes) {
			currNode.setTag(0);
			currNode.setWeight(Double.POSITIVE_INFINITY);
		}

		node_data srcNode= _graph.getNode(src);
		srcNode.setWeight(0);
		shortestPathDistRe(src);
		double ans= _graph.getNode(dest).getWeight();

		return ans;
	}


	private int smallestWeight() {
		Collection <node_data> nodes= _graph.getV();
		double lowestWeight=Double.POSITIVE_INFINITY;
		int ans=-1;
		for (node_data node_data : nodes) {
			if(node_data.getWeight()<lowestWeight && node_data.getTag()!=1) {
				lowestWeight= node_data.getWeight();
				ans= node_data.getKey();
			}
		}
		return ans;
	}

	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(!isReachable(src, dest))return null;
		Collection <node_data> nodes= _graph.getV();
		for (node_data currNode : nodes) {
			currNode.setTag(0);
			currNode.setWeight(Double.POSITIVE_INFINITY);
			currNode.setInfo("");
		}
		node_data srcNode= _graph.getNode(src);
		srcNode.setWeight(0);
		srcNode.setInfo(""+src);
		shortestPathDistRe(src);
		node_data destNode= _graph.getNode(dest);
		String keysOfNodes= destNode.getInfo();
		List<node_data> ans = new LinkedList<node_data>();
		StringTokenizer tokenizer = new StringTokenizer(keysOfNodes);

		while (tokenizer.hasMoreTokens()) {
			node_data curNod = _graph.getNode(Integer.parseInt(tokenizer.nextToken(",")));
			ans.add(curNod);
		}
		return ans;
	}

	private void shortestPathDistRe(int src) {
		node_data srcNode= _graph.getNode(src);
		if(srcNode.getTag()==1)return;
		if( _graph.getE(src)==null) {
			srcNode.setTag(1);
			return;
		}
		Collection<edge_data> edges= _graph.getE(src);

		srcNode.setTag(1);
		for (edge_data currEdge : edges) {
			double curWeight= srcNode.getWeight()+currEdge.getWeight();
			node_data nextNode= _graph.getNode(currEdge.getDest());

			if(nextNode.getWeight()>curWeight) {
				nextNode.setWeight(curWeight);
				nextNode.setInfo(srcNode.getInfo() + "," + nextNode.getKey());
			}
		}
		int nextKey= smallestWeight();
		if(nextKey==-1) return;
		shortestPathDistRe(nextKey);
	}

	private boolean isReachable(int src, int des) {
		if(_graph.getE(src)==null) {
			return false;
		}

		Collection <node_data> nodes= _graph.getV();
		for (node_data node_data : nodes) {
			node_data.setTag(0);
		}


		Collection<edge_data> edges = _graph.getE(src);
		_graph.getNode(src).setTag(1);
		for (edge_data edge_data : edges) {
			if(isReachableRe(_graph.getNode(edge_data.getDest()),_graph.getNode(des)))return true;
		}
		return false;
	}



	private boolean isReachableRe(node_data src, node_data des) {
		if(src.getKey()==des.getKey()) return true;
		if(src.getTag()==1) return false;

		if(_graph.getE(src.getKey()) == null) {
			src.setTag(1);
			return false;
		}

		Collection<edge_data> edges = _graph.getE(src.getKey());
		src.setTag(1);
		for (edge_data edge_data : edges) {
			if(isReachableRe(_graph.getNode(edge_data.getDest()),des))return true;
		}
		return false;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(!isAllTragetsConnected(targets)) {
			return null;
		}
		//converts targets List from Integer to nodes
		List<node_data> nodesTargets= new LinkedList<>();
		for(int i=0; i<targets.size(); i++) {
			nodesTargets.add(_graph.getNode(targets.get(i)));
		}

		List<node_data> ans= new LinkedList<>();
		node_data curNode=_graph.getNode(targets.get(0));
		double minWeight= Double.POSITIVE_INFINITY;
		while(nodesTargets.size()>1) {
			node_data nextNode= nextNode(nodesTargets, curNode);
			List<node_data> tempList= shortestPath(curNode.getKey(), nextNode.getKey());
			ans.addAll(tempList);
			nodesTargets.remove(curNode);
			curNode=nextNode;
		}
		removeDuplication(ans);
		return ans;
	}
	private void removeDuplication(List<node_data> list) {
		for(int i=0; i<list.size()-1; i++) {
			if(list.get(i)==list.get(i+1)) {
				list.remove(i);
			}
		}
	}


	//this method checking if all targets are connected
	private boolean isAllTragetsConnected(List<Integer> targets) {
		for(int i=0; i<targets.size()-1; i++) {
			for(int j=i+1; j<targets.size();  j++ ) {
				if(! isReachable(targets.get(i), targets.get(j))) {
					return false;
				}
			}
		}
		return true;
	}

	private node_data nextNode(List<node_data> targets, node_data curNode) {
		double minWeight= Double.POSITIVE_INFINITY;
		node_data ans= new NodeData();
		for(int i=0; i<targets.size(); i++) {
			node_data temp= targets.get(i);
			if(temp.getKey()!=curNode.getKey()) {
				double checkWeight= shortestPathDist(curNode.getKey(), temp.getKey());
				if(minWeight>checkWeight) {
					minWeight=checkWeight;
					ans= temp;
				}
			}
		}
		return ans;
	}

	/**
	 * returns the graph of the class
	 * @return graph
	 */
	public graph getGraph() {
		return this._graph;
	}
}