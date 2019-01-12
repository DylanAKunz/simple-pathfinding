import java.util.Random;

public class pathfinding {
	public static final int dimension = 10;
	public static final int obstacles = 5;
	
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
	
	/*
	//Travel along the path towards the destination
	public static int[] pathfinding(int[] previousPosition, int direction) {}
	*/
	
	//Initialize obstacles, call functions for pathfinding, display path.
	public static void main(String[] args) {
		boolean[][] passableTiles = new boolean[dimension][dimension];
		passableTiles = obstacleGenerate(obstacles);
	}

}
