import java.util.Random;
import java.util.Vector;
import java.util.List;

public class pathfinding {
	public static final int dimension = 10;
	public static final int obstacles = 15;
	public static final int[] startTile = {dimension/2, 0};
	public static final int[] endTile = {dimension/2, dimension - 1};
	
	/**
	 * Creates a number of obstacles based on provided parameters, returning the coordinates 
	 * @param number the number of obstacles to return
	 * @param dimensionX the desired number of x coordinates
	 * @param dimensionY the desired number of y coordinates.
	 * @return a boolean array with obstructed tiles = false
	 */
	public static boolean[][] obstacleGenerate(int number, int dimensionX, int dimensionY){
		Random rand = new Random();
		boolean[][] passableTiles = new boolean[dimensionX][dimensionY];
		int x, y;
		//Iterate through the multidimensional array and set all values to true, for simplicity, I.E. true = passable
		for(int i = 0; i < dimensionX;i++) {
			for(int z = 0; z < dimensionY; z++) {
				passableTiles[i][z] = true;
			}
		}
		//Iterate through creating a random x and y coordinate for the number of obstacles set, assign the boolean from passable tiles that matches to false, and print a statement stating its been obstructed.
		for(int i = 0; i < number; i++) {
			x = rand.nextInt(dimensionX);
			y = rand.nextInt(dimensionY);
			passableTiles[x][y] = false;
			System.out.println(x + " " + y + " is obstructed.");
		}
		return passableTiles;
	}
	
	
	/**
	 * Travels along the path of provided tiles from the provided start position, avoiding obstructed tiles until reaching the provided end coordinates. 
	 * @param passableTiles A two dimensional boolean array of obstructed/unobstructed tiles with passables tiles being equal to true.
	 * @param startPosition a one dimensional integer array containing the starting coordinates, [0] being the x and [1] being the y coordinates.
	 * @param dimensionX the provided number of x coordinates, should be equal to the x dimension of passableTiles
	 * @param dimensionY the provided number of y coordinates, should be equal to the y dimension of passableTiles
	 * @param targetTile a one dimensional integer array containing the starting coordinates, [0] being the x and [1] being the y coordinates.
	 */
	public static void pathfinding(boolean[][] passableTiles, int[] startPosition, int[] targetTile, int dimensionX, int dimensionY) {
		int[][] tiles = new int[dimensionX][dimensionY];
		//Error code variable, and counter.  0 for no end, 1 for ended successfuly and 2 for ran out of viable tiles to attempt to move to.  reused for second loop.
		int reachedEnd = 0, counter = 1;
		int[] currentTile = new int[2];
		boolean moved;
		//initializes the current tile to the end tile for the second loop.
		currentTile[0] = targetTile[0];
		currentTile[1] = targetTile[1];
		passableTiles[targetTile[0]][targetTile[1]] = true;
		//Contains the currently active valid tiles to attempt to move from.
		List<Integer> activeTiles = new Vector<Integer>();
		//Contains the tiles that have been successfully moved into.
		Vector<Integer> activeTilesTemp = new Vector<Integer>();
		//Add the starting tile to the initial list of active tiles, and sets the first move to be 1.
		activeTiles.add(startPosition[0]);
		activeTiles.add(startPosition[1]);
		tiles[startPosition[0]][startPosition[1]] = 1;
		//Loop through until no valid moves are available or have reached the end.
		while(reachedEnd == 0) {
			counter++;
			//Loops through until the active moves have been exhausted, allowing the counter to increase and the new tiles to be loaded into the active vector.
			while(activeTiles.size() != 0) {
				//Loops through each possible directly adjacent tile, checking if it is A. Within the array of tiles. B. currently without a move counter on it. if it is sets it to be the current counter.  does not need to test if tile is lower, as tile will either be less or the same number of moves to reach.
				for(int i = -1; i <= 1; i += 2) {
					//Tests for the x axis
					//Checks if within bounds.
					if(activeTiles.get(0) + i >= 0 && activeTiles.get(0) + i < dimensionX) {
						//Checks if already set move counter on the tile.
						if(tiles[activeTiles.get(0) + i][activeTiles.get(1)] == 0 && passableTiles[activeTiles.get(0) + i][activeTiles.get(1)] == true) {
							tiles[activeTiles.get(0) + i][activeTiles.get(1)] = counter;
							//Adds the tile to the activeTiles for the next move
							activeTilesTemp.add(activeTiles.get(0) + i);
							activeTilesTemp.add(activeTiles.get(1));
						}
					}
					//Tests for the y axis
					//Checks if within bounds.
					if(activeTiles.get(1) + i >= 0 && activeTiles.get(1) + i < dimensionY) {
						//Checks if already set move counter on the tile.
						if(tiles[activeTiles.get(0)][activeTiles.get(1) + i] == 0 && passableTiles[activeTiles.get(0)][activeTiles.get(1) + i] == true) {
							tiles[activeTiles.get(0)][activeTiles.get(1) + i] = counter;
							//Adds the tile to the activeTiles for the next move
							activeTilesTemp.add(activeTiles.get(0));
							activeTilesTemp.add(activeTiles.get(1) + i);
						}
					}
				}
				//Remove the tile from the active vector
				activeTiles.remove(0);
				activeTiles.remove(0);
			}
			//If no tiles were added to the activeTilesTemp, this indicates the program found no valid moves, so set the reachedEnd to 2, indicating it was unable to reach the destination.
			if(activeTilesTemp.size() <= 1) {
				reachedEnd = 2;
			}else {
				//Else add the activeTilesTemp to the activeTiles and purging activeTilesTemp, readying it for the next turn.
				for(int x = 0; x < activeTilesTemp.size(); x++) {
					if(activeTilesTemp.get(0) == targetTile[0] && activeTilesTemp.get(1) == targetTile[1]) {
						reachedEnd = 1;
					}
					activeTiles.add(activeTilesTemp.get(0));
					activeTilesTemp.remove(0);
					activeTiles.add(activeTilesTemp.get(0));
					activeTilesTemp.remove(0);
					//If reached the target tile, set reachedEnd to 1, indicating a successful path.
				}
			}
		}
		System.out.println("Reached end code: " + reachedEnd);
		//If finding a path was successful, iterate backwards through the path starting at the target tile.
		if(reachedEnd == 1) {
			reachedEnd = 0;
			//Iterate through until reaching the beginning tile.
			while(reachedEnd != 1) {
				moved = false;
				System.out.println("Back tracing path, Current tile: " + currentTile[0] + " " + currentTile[1]);
				//Iterate through the four directly adjacent directions, checking for which of the four has the shortest path back to the start.
				for(int i = -1; i < 2; i += 2) {
					//Checking if in bounds, then the x axis
					if(currentTile[0] + i >= 0 && currentTile[0] + i < dimensionX - 1 && moved == false) {
						if(tiles[currentTile[0] + i][currentTile[1]] < counter && tiles[currentTile[0] + i][currentTile[1]] > 0) {
							currentTile[0] = currentTile[0] + i;
							counter = tiles[currentTile[0]][currentTile[1]];
							moved = true;
						}
					}
					//Checking if in bounds, then the y axis
					if(currentTile[1] + i >= 0 && currentTile[1] + i < dimensionY - 1 && moved == false) {
						if(tiles[currentTile[0]][currentTile[1] + i] < counter && tiles[currentTile[0]][currentTile[1] + i] > 0) {
							currentTile[1] = currentTile[1] + i;
							counter = tiles[currentTile[0]][currentTile[1]];
							moved = true;
						}
					}
				}
				//sets reachedEnd to 1 if back at the starting position, allowing the loop to end.  Only break point for the loop, as at reaching this point should have guaranteed path to start.
				if(currentTile[0] == startPosition[0] && currentTile[1] == startPosition[1]) {
					reachedEnd = 1;
					//Print one last log, to show user they are at their starting point.
					System.out.println("Back tracing path, Current tile: " + currentTile[0] + " " + currentTile[1]);
				}
			}
		}
	}
	
	
	/** 
	 * Initialize obstacles, call functions for pathfinding, display path.
	 * @param args
	 */
	public static void main(String[] args) {
		boolean[][] passableTiles = new boolean[dimension][dimension];
		passableTiles = obstacleGenerate(obstacles, dimension, dimension);
		pathfinding(passableTiles, startTile, endTile, dimension, dimension);
	}

}
