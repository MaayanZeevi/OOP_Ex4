<h1>MyGame Project</h1>
<h3>About the project..</h3>
This project is repesenting a Packman game, 
robots travel on the graph on purpose to eat all the fruits in the shortest time as possible and gain points, 
by eating the fruits the robot’s speed is getting higher.

The user can choose mode of game: automatic or manual. 

Automatic game – the player watches a game that running on our algorithm for getting the best score.

Manual game -the user choosing the robots location and move them on the game board for eating as much fruits at least time

<h3>The Project Includes:</h3><h3>Package dataStructure:</h3><hr><h3>DGraph class:</h3>This class is designed to build a graph that is both directed and weighted, by adding nodes and edges. 
<h3>Edge_Data class:</h3>This class represents a directional edge(src,dest) in the graph and set of operations applicable on a it.
<h3>Node_Data class:</h3>This class represents a vertex in the graph and set of operations applicable on it.
<h3>Package algorithms:</h3><hr><h3>Graph_Algo class:</h3>This class contains algorithms that can be run on a graph , etc: shortestPathDist, isConnected, TSP, copy and init.
<h3>Package gameClient:</h3><hr><h3>MyGameGUI class:</h3> This class includes methods in order to build the game of the project.
<h3>KML_Logger class:</h3> This class allows you to export the game graph to a KML file. By open the file, you will see the game moves on a map in Google Earth.
<h3>Package gameCUtils:</h3><hr>
<h3>Fruit class:</h3>Variables of the class: location, image, value(grade of this fruit) and type of the fruit(apple or banana).
Methods of the class: init fruit object by json,  getLocation and getImage.
<h3>Robot class:</h3>Variables of the class: location, image, id, dest(destenetion vertix), src and points.
Methods of the class: gets method of class variables and constructor from json.








