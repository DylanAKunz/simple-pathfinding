import java.util.Random;
import java.util.Vector;

public class pathfinding {
	public static final int dimension = 10;
	public static final int obstacles = 5;
	public static final int[] startTile = {dimension/2, 0};
	public static final int[] endTile = {dimension/2, dimension - 1};
	
	//generate a series of obstacles for the pathfinding
	public static boolean[][] obstacleGenerate(int number){
		Random rand = new Random();
		boolean[][] passableTiles = new boolean[dimension][dimension];
		int x, y;
		//Iterate through the multidimensional array and set all values to true, for simplicity, I.E. true = passable
		for(int i = 0; i < dimension;i++) {
			for(int z = 0; z < dimension; z++) {
				passableTiles[i][z] = true;
			}
		}
		//Iterate through creating a random x and y coordinate for the number of obstacles set, assign the boolean from passable tiles that matches to false, and print a statement stating its been obstructed.
		for(int i = 0; i < number; i++) {
			x = rand.nextInt(dimension);
			y = rand.nextInt(dimension);
			passableTiles[x][y] = false;
			System.out.println(x + " " + y + " is obstructed.");
		}
		return passableTiles;
	}
	
	
	//Travel along the path towards the destination
	public static void pathfinding(boolean[][] passableTiles, int[] startPosition, int[] targetTile) {
		int[][] tiles = new int[dimension][dimension];
		//Error code variable, and counter.  0 for no end, 1 for ended successfully and 2 for ran out of viable tiles to attempt to move to.  reused for second loop.
		int reachedEnd = 0, counter = 1;
		boolean changed = false;
		int[] currentTile = new int[2];
		//initializes the current tile to the end tile for the second loop.
		currentTile[0] = targetTile[0];
		currentTile[1] = targetTile[1];
		//Contains the currently active valid tiles to attempt to move from.
		Vector<Integer> activeTiles = new Vector<Integer>();
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
				for(int i = -1; i < 2; i += 2) {
					//Tests for the x axis
					//Checks if within bounds.
					if(activeTiles.get(0) + i >= 0 && activeTiles.get(0) + i < dimension) {
						//Checks if already set move counter on the tile.
						if(tiles[activeTiles.get(0) + i][activeTiles.get(1)] == 0) {
							tiles[activeTiles.get(0) + i][activeTiles.get(1)] = counter;
							//Adds the tile to the activeTiles for the next move
							activeTilesTemp.add(activeTiles.get(0) + i);
							activeTilesTemp.add(activeTiles.get(1));
						}
					}
					//Tests for the y axis
					//Checks if within bounds.
					if(activeTiles.get(1) + i >= 0 && activeTiles.get(1) + i < dimension) {
						//Checks if already set move counter on the tile.
						if(tiles[activeTiles.get(0)][activeTiles.get(1) + i] == 0) {
							tiles[activeTiles.get(0)][activeTiles.get(1) + i] = counter;
							//Adds the tile to the activeTiles for the next move
							activeTilesTemp.add(activeTiles.get(0));
							activeTilesTemp.add(activeTiles.get(1) + i);
						}
					}
				}
				//Remove the tiles from the active vector
				activeTiles.remove(0);
				activeTiles.remove(0);
			}
			//If no tiles were added to the activeTilesTemp, this indicates the program found no valid moves, so set the reachedEnd to 2, indicating it was unable to reach the destination.
			if(activeTilesTemp.size() == 0) {
				reachedEnd = 2;
			}else {
				//Else add the activeTilesTemp to the activeTiles and purging activeTilesTemp, readying it for the next turn.
				for(int x = 0; x < activeTilesTemp.size(); x++) {
					activeTiles.add(activeTilesTemp.get(0));
					activeTiles.add(activeTilesTemp.get(1));
					activeTilesTemp.remove(0);
					activeTilesTemp.remove(0);
				}
			}
			//If reached the target tile, set reachedEnd to 1, indicating a successfull path.
			if(activeTiles.get(0) == targetTile[0] && activeTiles.get(1) == targetTile[1]) {
				reachedEnd = 1;
			}
		}
		System.out.println("Reached end code: " + reachedEnd);
		//If finding a path was successfull, iterate backwards through the path starting at the target tile.
		if(reachedEnd == 1) {
			reachedEnd = 0;
			//Iterate through until reaching the beginning tile.
			while(reachedEnd != 1) {
				System.out.println("Back tracing path, Current tile: " + currentTile[0] + " " + currentTile[1]);
				//Iterate through the four directly adjacent directions, ceasing if a valid move was found.
				for(int i = -1; i < 2; i += 2) {
					if(currentTile[0] + i >= 0 && currentTile[0] + i < dimension) {
						if(tiles[currentTile[0] + i][currentTile[1]] < counter) {
							currentTile[0] = currentTile[0] + i;
							changed = true;
							counter = tiles[currentTile[0]][currentTile[1]];
						}
					}
					if(currentTile[1] + i >= 0 && currentTile[1] + i < dimension - 1) {
						if(tiles[currentTile[0]][currentTile[1] + i] < counter) {
							currentTile[1] = currentTile[1] + i;
							changed = true;
							counter = tiles[currentTile[0]][currentTile[1]];
						}
					}
				}
				if(currentTile[0] == startPosition[0] && currentTile[1] == startPosition[1]) {
					reachedEnd = 1;
					System.out.println("Back tracing path, Current tile: " + currentTile[0] + " " + currentTile[1]);
				}
				changed = false;
			}
		}
	}
	
	
	//Initialize obstacles, call functions for pathfinding, display path.
	public static void main(String[] args) {
		boolean[][] passableTiles = new boolean[dimension][dimension];
		passableTiles = obstacleGenerate(obstacles);
		pathfinding(passableTiles, startTile, endTile);
	}

}
