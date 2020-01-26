package gameClient;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.google.gson.Gson;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameUtils.Fruit;
import gameUtils.robot;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI implements Runnable {
	game_service game;
	DGraph graph;
	int robots;
	GameServer GameServer;
	int level;
	int moves = 0;
	Graph_Algo graph_algo;
	public String type;
	double maxX = Double.NEGATIVE_INFINITY;
	double maxY = Double.NEGATIVE_INFINITY;
	double minX = Double.POSITIVE_INFINITY;
	double minY = Double.POSITIVE_INFINITY;

	/*
	 * constractore
	 */

	public MyGameGUI() {
		StdDraw.setCanvasSize(1000, 700);
		this.level = -1;
	}



	/*
     * This method draws the graph.
     */

	public void DrawGraph() {
		rangeX(graph.getV());
		rangeY(graph.getV());
		double limx=(maxX-minX)*0.10;
		double limy=(maxY-minY)*0.10;
		StdDraw.setXscale(minX-limx, maxX+limx);
		StdDraw.setYscale(minY-limy, maxY+limy);
		StdDraw.setPenColor(Color.PINK);
		Collection<node_data> Nodes = this.graph.getV();
		for (node_data n : Nodes) {
			Collection<edge_data> Edges = this.graph.getE(n.getKey());
			double x = n.getLocation().x();
			double y = n.getLocation().y();

			for (edge_data e : Edges) {
				double x1 = this.graph.getNode(e.getDest()).getLocation().x();
				double y1 = this.graph.getNode(e.getDest()).getLocation().y();
				StdDraw.line(x, y, x1, y1);
			}
		}
		StdDraw.setPenColor(Color.MAGENTA);
		for (node_data n : Nodes) {
			StdDraw.setPenRadius();
			Point3D location = n.getLocation();
			StdDraw.filledCircle(location.x(), location.y(), 0.0001);

			StdDraw.text(location.x() + 0.0002, location.y() + 0.0002, "" + n.getKey());
		}
	}

	/**
	 * Creates a range of the x values of the nodes.
	 *
	 *
	 */

	//DOO
	private void rangeX (Collection<node_data> collection) {
		for (node_data n: collection) {
			if(n.getLocation().x()<minX) minX=n.getLocation().x();
			if(n.getLocation().x()>maxX) maxX=n.getLocation().x();
		}
	}
	/**
	 * Creates a range of the y values of the nodes.
	 * @param collection represents a list with all the nodes in the graph.
	 */
	//DOO
	private void rangeY (Collection<node_data> collection) {
		for (node_data n: collection)  {
			if(n.getLocation().y()<minY) minY=n.getLocation().y();
			if(n.getLocation().y()>maxY) maxY=n.getLocation().y();
		}

	}
	/*
     * This method gets each Fruit  and draws it on the graph by getting its location and its image.
     */


	//DOO
	private void DrawFruit() {
		List<String> Fruits = this.game.getFruits();
		for (String f : Fruits) {

			Fruit fruit = new Fruit(f);
			StdDraw.picture(fruit.getLocation().x(), fruit.getLocation().y(), fruit.getImage(), 0.0006, 0.0006);
		}
	}

	/*
     * This method gets each Robot  and draws it on the graph by getting its location and its image.
     */

	private void DrawRobots() {
		List<String> Robots = this.game.getRobots();
		for (String r : Robots) {
			robot robo = new robot(r);
			StdDraw.picture(robo.getLocation().x(), robo.getLocation().y(), robo.getImage(), 0.0006, 0.0006);
		}
	}

	/*
     * This method make a visualization of the time when the game is running.
     */

	//DOO

	public void DrawTime() {
		long Time = this.game.timeToEnd();
		StdDraw.setPenColor(Color.RED);
		if (this.game.isRunning())
			StdDraw.textRight(StdDraw.xmax - 0.0010, StdDraw.ymax - 0.0010, "" + Time / 1000);
		else StdDraw.textRight(StdDraw.xmax - 0.0005, StdDraw.ymax - 0.0005, "game over");
	}




	private class GameServer {
		int robots;
	}

	private void loginToServer(){
		int id = 0;
		String idS = JOptionPane.showInputDialog(new JFrame(),"enter your id", null);
		try {
			id = Integer.parseInt(idS);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "the action was canceled");
			return;
		}
		Game_Server.login(id);
	}


	/*
     * This method enables the user to choose play with mode Manual
     */
	public void Manual() {
		this.GameServer = new GameServer();
		this.graph = new DGraph();
		this.level = getLevel()-1;
		this.game = Game_Server.getServer(level);

		Gson gson = new Gson();
		MyGameGUI temp = gson.fromJson(game.toString(), MyGameGUI.class);
		this.GameServer = temp.GameServer;
		this.robots = this.GameServer.robots;


		this.graph.init(this.game.getGraph());
		this.graph_algo = new Graph_Algo(this.graph);
		this.DrawGraph();
		this.addRobotsManual();
		this.DrawFruit();
		loginToServer();
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		while (this.game.isRunning()) {
			List<String> robots =  this.game.move();
			moves++;
			for (String i : robots) {
				robot temp1 = new robot(i);
				if (temp1.getDest() == -1) {
					String robotDes = JOptionPane.showInputDialog(new JFrame(), "select a node to go to\nrobot id: " , null);
					int des = -1;
					try {
						des = Integer.parseInt(robotDes);
					} catch (Exception e) {
					}
					this.game.chooseNextEdge(temp1.getId(), des);
				}
			}
			this.game.move();
			StdDraw.clear();
			DrawGraph();
			DrawFruit();
			DrawRobots();
			DrawTime();
			StdDraw.show();

		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		end();

	}


	/*
     * This method enables the user to choose to play with mode Automatic
     */
	public void Automatic() {

		this.GameServer = new GameServer();
		this.level = getLevel();
		this.game = Game_Server.getServer(level );

		Gson gson = new Gson();
		MyGameGUI temp = gson.fromJson(game.toString(), MyGameGUI.class); //puts game server data into MyGameGUI instance
		this.GameServer = temp.GameServer;

		this.robots = this.GameServer.robots;
		game = Game_Server.getServer(this.level );
		this.graph = new DGraph();
		this.graph.init(this.game.getGraph());
		this.graph_algo = new Graph_Algo(this.graph);
		this.DrawGraph();
		this.DrawFruit();
		this.AutomaticRobot();
		this.DrawRobots();

		KML_Logger.openFile(this.level + ".kml");
		loginToServer();
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		Runnable gameShow = new Runnable() {
			@Override
			public void run() {
				movesCounter();

			}
		};
		Thread thread1 = new Thread(gameShow);
		int id = 0;
		String idS = JOptionPane.showInputDialog(new JFrame(),"enter your id", null);
		try {
			id = Integer.parseInt(idS);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "the action was canceled");
			return;
		}
		Game_Server.login(id);
		thread1.start();
		while (this.game.isRunning()) {
			List<String> robots =this.game.move();
			for (String i : robots) {
				robot temp1 = new robot(i);
				if (temp1.getDest() == -1) {
					this.game.chooseNextEdge(temp1.getId(), NearestNode(temp1, 0.0));
				}
			}
			this.game.move();
			StdDraw.clear();
			DrawGraph();
			DrawFruit();
			DrawRobots();
			DrawTime();
			StdDraw.show();
			List<String> fruitsList = this.game.getFruits();
			for (String fruit : fruitsList) {
				KML_Logger.addFruit(this.level + ".kml", fruit);
			}
			List<String> robotsList = this.game.getRobots();
			for (String robot : robotsList) {
				KML_Logger.addRobot(this.level + ".kml", robot);
			}
		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		try {
			Thread.sleep(4500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		KML_Logger.closeFile(this.level + ".kml");
	}


	//this method counts how much moves the player did in the game
	public void movesCounter() {
		while (this.game.isRunning()){
			this.game.move();
			this.moves++;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void automaticWithKml(){
		this.GameServer = new GameServer();
		this.level = getLevel();
		this.game = Game_Server.getServer(level );

		Gson gson = new Gson();
		MyGameGUI temp = gson.fromJson(game.toString(), MyGameGUI.class);
		this.GameServer = temp.GameServer;

		this.robots = this.GameServer.robots;
		game = Game_Server.getServer(this.level );
		this.graph = new DGraph();
		this.graph.init(this.game.getGraph());
		this.graph_algo = new Graph_Algo(this.graph);
		this.DrawGraph();
		this.DrawFruit();
		this.AutomaticRobot();
		this.DrawRobots();
		KML_Logger.openFile((level) + ".kml");
		loginToServer();
		this.game.startGame();
		StdDraw.enableDoubleBuffering();
		Runnable gameShow = new Runnable() {
			@Override
			public void run() {
				movesCounter();
			}
		};
		Thread thread1 = new Thread(gameShow);
		thread1.start();

		while (this.game.isRunning()) {
			List<String> robots =this.game.move();
			for (String i : robots) {
				robot temp1 = new robot(i);
				if (temp1.getDest() == -1) {
					this.game.chooseNextEdge(temp1.getId(), NearestNode(temp1, 0.0));
				}
			}
			this.game.move();
			StdDraw.clear();
			DrawGraph();
			DrawFruit();
			DrawRobots();
			DrawTime();
			StdDraw.show();
			List<String> fruitsList = this.game.getFruits();
			for (String fruit : fruitsList) {
				KML_Logger.addFruit(this.level + ".kml", fruit);
			}
			List<String> robotsList = this.game.getRobots();
			for (String robot : robotsList) {
				KML_Logger.addRobot(this.level + ".kml", robot);
			}

		}
		StdDraw.disableDoubleBuffering();
		StdDraw.clear();
		try {
			Thread.sleep(4500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end();
		KML_Logger.closeFile(level + ".kml");
		System.out.println(this.moves);
		game.sendKML(KML_Logger.toString(level + ".kml"));
	}

	/*
     * this method enables the user to add Robots manually.
     *
     */
	public boolean addRobotsManual() {

		for (int i = 0; i < this.robots; i++) {
			final JFrame jframe = new JFrame();
			int PlaceInt = 0;
			String Place = JOptionPane.showInputDialog(jframe, "enter a vertex key to add robot to\nrobot key: " + i, null);
			if (Place == null) {
				JOptionPane.showMessageDialog(null, "Error");
				return false;
			}
			try {
				PlaceInt = Integer.parseInt(Place);
			} catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(null, "you not  entered a integer!\ngame  cancel");
				return false;
			}
			if (this.graph.getNode(PlaceInt) == null) {
				JOptionPane.showMessageDialog(null, "the key  doesnt exist\ngame   cancel");
				return false;
			}
			this.game.addRobot(PlaceInt);
			this.DrawRobots();
		}
		return true;
	}


	/*
	 * This method adds Robots automatically to the graph.
	 */
	public void AutomaticRobot() {

		double epsilon = 0.0000000001;
		List<String> fruitString = this.game.getFruits();

		LinkedList<Fruit> Fruits = new LinkedList<Fruit>();

		for (String s : fruitString) {

			Fruits.add(new Fruit(s));
		}


		for (int i = 0; i < this.robots; i++) {
			Fruit fruitMax = null;

			for (Fruit f : Fruits) {

				if (fruitMax == null) fruitMax = f;
				else if (fruitMax.value < f.value) ;
				fruitMax = f;
			}
			Collection<node_data> Nodes = this.graph.getV();
			edge_data fruitEdge = null;

			for (node_data n : Nodes) {

				for (node_data j : Nodes) {
					if (n.getKey() == j.getKey()) continue;
					boolean isBetween = isBtween(n, j, fruitMax);
					if (isBetween == true) {

						fruitEdge = this.graph.getEdge(n.getKey(), j.getKey());
						if (fruitEdge != null) break;
					}

				}
				if (fruitEdge != null) break;
			}
			double Ysrc = this.graph.getNode(fruitEdge.getSrc()).getLocation().y();

			double Ydest = this.graph.getNode(fruitEdge.getDest()).getLocation().y();

			if (fruitMax.type == 1) {
				if (Ysrc < Ydest) this.game.addRobot(fruitEdge.getSrc());
				if (Ysrc > Ydest) this.game.addRobot(fruitEdge.getDest());

			} else {
				if (Ysrc > Ydest) this.game.addRobot(fruitEdge.getSrc());
				else this.game.addRobot(fruitEdge.getDest());

			}
			Fruits.remove(fruitMax);
		}
	}

	/*
     * Returns the level that the user chose
     */

	//DOO
	private int getLevel() {
		String level = JOptionPane.showInputDialog("select a level Between 0 and 23", null);
		int NumbrLevel = -1;

		try {
			NumbrLevel = Integer.parseInt(level);
		}
		catch (Exception e1) {
			NumbrLevel = (int) (Math.random() * 25);
		}

		if (NumbrLevel < 0 || NumbrLevel > 23) {
			NumbrLevel = (int) (Math.random() * 25);
		}

		return NumbrLevel;
	}


	private double yValue(node_data n1,node_data n2,Fruit fruit){
		double m = (n1.getLocation().y() - n2.getLocation().y()) / (n1.getLocation().x() - n2.getLocation().x());//M=Y1-Y2/(X1-X2}
		double y = (m * (fruit.location.x() - n1.getLocation().x())) + n1.getLocation().y();//Y=m*x+n
		return y;
	}

	/*
     * This method determine to which node each robot do must go .
     */


	private int NearestNode(robot rob, double epsilon) {
		double distance = Double.POSITIVE_INFINITY;
		LinkedList<Fruit> fruits = new LinkedList<Fruit>();
		List<String> fruitString = this.game.getFruits();
		Collection<node_data> Vertices = this.graph.getV();
		for (String s : fruitString) {
			fruits.add(new Fruit(s));
		}
		edge_data edgeFruit = null;
		int destNode = -1;
		for (Fruit F : fruits) {

			for (node_data m : Vertices) {

				for (node_data n : Vertices) {

					if (m.getKey() != n.getKey()) {
						double ans=yValue(m,n,F);
						if (ans == F.location.y() - epsilon || ans == F.location.y() + epsilon) {
							edgeFruit = this.graph.getEdge(m.getKey(), n.getKey());
						}
					}
				}
			}
			if (edgeFruit != null) {
				if (F.type == 1) {
					double srcY = this.graph.getNode(edgeFruit.getSrc()).getLocation().y();

					double destY = this.graph.getNode(edgeFruit.getDest()).getLocation().y();
					if (srcY >destY) {
						double tempDistance = this.graph_algo.shortestPathDist(rob.src, edgeFruit.getDest());
						if (tempDistance < distance) {
							distance = tempDistance;
							destNode = edgeFruit.getDest();
							if (destNode == rob.src) {
								destNode = edgeFruit.getSrc();
							}
						}
					}
					else {
						double tempDistance = this.graph_algo.shortestPathDist(rob.src, edgeFruit.getSrc());
						if (tempDistance < distance) {
							distance = tempDistance;
							destNode = edgeFruit.getSrc();
							if (destNode == rob.src) {
								destNode = edgeFruit.getDest();
							}
						}
					}
				}
				else {
					double srcY = this.graph.getNode(edgeFruit.getSrc()).getLocation().y();
					double destY = this.graph.getNode(edgeFruit.getDest()).getLocation().y();
					if (srcY > destY) {
						double tempDistance = this.graph_algo.shortestPathDist(rob.src, edgeFruit.getSrc());
						if (tempDistance < distance) {
							distance = tempDistance;
							destNode = edgeFruit.getSrc();
							if (destNode == rob.src) {
								destNode = edgeFruit.getDest();
							}
						}
					}
					else {
						double tempDistance = this.graph_algo.shortestPathDist(rob.src, edgeFruit.getDest());
						if (tempDistance < distance) {
							distance = tempDistance;
							destNode = edgeFruit.getDest();
							if (destNode == rob.src) {
								destNode = edgeFruit.getSrc();
							}
						}
					}
				}
			}
		}
		if (destNode == -1){
			return NearestNode(rob, epsilon + 0.000000000000001);
		}
		List<node_data> ans =  this.graph_algo.shortestPath(rob.src, destNode);

		return ans.get(1).getKey();
	}







	public boolean isBtween (node_data n1,node_data n2,Fruit fruitmax){


		double epsilon=0.0000000001;
		if ((n1.getLocation().x() - epsilon < fruitmax.getLocation().x() && n2.getLocation().x() + epsilon > fruitmax.getLocation().x()) ||
				(n1.getLocation().x() + epsilon > fruitmax.getLocation().x() && n2.getLocation().x() - epsilon < fruitmax.getLocation().x())) {
			if ((n1.getLocation().y() - epsilon < fruitmax.getLocation().y() && n2.getLocation().y() + epsilon > fruitmax.getLocation().y()) ||
					(n1.getLocation().y() + epsilon > fruitmax.getLocation().y() && n2.getLocation().y() - epsilon < fruitmax.getLocation().y())) {
				return true;
			}
		}

		return false;

	}
	/*
     * If the selected mode was "manual",  the game run in manual mode.
     * If the selected mode was automatic, the game run in automatic mode.
     */
	@Override
	public void run() {
		if (this.type.equals("Manual")) Manual();
		if (this.type.equals("Automatic")) Automatic();
		if (this.type.equals("Automatic with KML")) automaticWithKml();
		if (this.type.equals("Your Grade")) yourScore();
//		if (this.type.equals("Global Grade")) globalScore();

	}

	/*
     *  print finsh game and score
     */
	public void end() {
		int score = 0;
		for (String i : this.game.getRobots()) {

			robot rob = new robot(i);

			score = score + rob.getPoints();
		}
		StdDraw.setPenColor(Color.green);
		StdDraw.textRight(((StdDraw.xmax + StdDraw.xmin) / 2), ((StdDraw.ymax + StdDraw.ymin) / 2) + 0.002,"game end and your score is" + score);
	}

	public void yourScore() {
		StdDraw.clear();
		StdDraw.setFont(new Font("arial", Font.PLAIN, 15));
		StdDraw.setPenColor(Color.red);
		int id = 0;
		String idStr = JOptionPane.showInputDialog(new JFrame(),"Please enter your ID", null);
		try {
			id = Integer.parseInt(idStr);
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Eror, unvalide input!");
		}

		int []Stages = grade.CurrentStage(id);
		for (int i = 0; i < Stages.length; i++) {
			if (Stages[i] == 0) {

				if (i == 0) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 0");


					break;
				}
				else if (i == 1) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 1");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					break;
				}
				else if (i == 2) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 3");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is   " + Stages[1]);
					break;
				}
				else if (i == 3) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 5");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					break;
				}
				else if (i == 4) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 9");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);

					break;
				}
				else if (i == 5) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 11");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);

					break;
				}
				else if (i == 6) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 13");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					break;
				}
				else if (i == 7) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 16");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.35 , "Your higher grade at level 13 is  " + Stages[6]);
					break;
				}
				else if (i == 8) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 19");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.35 , "Your higher grade at level 13 is  " + Stages[6]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.40 , "Your higher grade at level 16 is  " + Stages[7]);
					break;
				}
				else if (i == 9) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 20");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.35 , "Your higher grade at level 13 is  " + Stages[6]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.40 , "Your higher grade at level 16 is  " + Stages[7]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.45 , "Your higher grade at level 19 is  " + Stages[8]);
					break;
				}
				else if (i == 10) {
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have played " + grade.frequenceOfGame(id) +" times"+" \n  and your level is 23");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.35 , "Your higher grade at level 13 is  " + Stages[6]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.40 , "Your higher grade at level 16 is  " + Stages[7]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.45 , "Your higher grade at level 19 is  " + Stages[8]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.50 , "Your higher grade at level 20 is  " + Stages[9]);

					break;
				}
				else{ // i=11
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 , "You have finished the game");
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.05 , "Your higher grade at level 0 is   " + Stages[0]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.1 , "Your higher grade at level 1 is  " + Stages[1]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.15 , "Your higher grade at level 3 is  " + Stages[2]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.20 , "Your higher grade at level 5 is  " + Stages[3]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.25 , "Your higher grade at level 9 is  " + Stages[4]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.30 , "Your higher grade at level 11 is  " + Stages[5]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.35 , "Your higher grade at level 13 is  " + Stages[6]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.40 , "Your higher grade at level 16 is  " + Stages[7]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.45 , "Your higher grade at level 19 is  " + Stages[8]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.50 , "Your higher grade at level 20 is  " + Stages[9]);
					StdDraw.text(((StdDraw.xmax + StdDraw.xmin) / 5), ((StdDraw.ymax + StdDraw.ymin) / 5) * 4 -0.55 , "Your higher grade at level 23 is  " + Stages[10]);



					break;
				}
			}
		}

	}


}