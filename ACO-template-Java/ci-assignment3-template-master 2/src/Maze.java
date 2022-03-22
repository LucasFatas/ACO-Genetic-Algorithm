import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class that holds all the maze data. This means the pheromones, the open and blocked tiles in the system as
 * well as the starting and end coordinates.
 */
public class Maze {
    public int width;
    public int length;
    public double[][] walls;
    public double[][] pheromones;
    public Coordinate start;
    public Coordinate end;

    /**
     * Constructor of a maze
     * @param walls int array of tiles accessible (1) and non-accessible (0)
     * @param width width of Maze (horizontal)
     * @param length length of Maze (vertical)
     */
    public Maze(int[][] wallsw, int width, int length) {


        this.length = length;
        this.width = width;
        this.walls = new double[width][length];
        this.pheromones = new double[width][length];
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < length; column++) {
                this.walls[row][column] = (double) wallsw[row][column];
            }
        }
        initializePheromones();
    }

    /**
     * Initialize pheromones to a start value.
     */
    private void initializePheromones() {
        for (int row = 0; row < pheromones.length; row++) {
            for (int column = 0; column < pheromones[0].length; column++) {
                this.pheromones[row][column] = this.walls[row][column];
            }
        }
    }

    /**
     * Reset the maze for a new shortest path problem.
     */
    public void reset() {
        initializePheromones();
    }

    /**
     * Update the pheromones along a certain route according to a certain Q
     * @param r The route of the ants
     * @param Q Normalization factor for amount of dropped pheromone
     */
    public void addPheromoneRoute(Route r, double Q) throws Exception {
        Coordinate curPos = r.start;
        int routeSize = r.size();
        for (Direction dir : r.route) {
            curPos.add(dir);
            if (routeSize == 0)
                throw new Exception("sad");
            this.pheromones[curPos.getX()][curPos.getY()] += Q * (1.0 / routeSize);

        }
    }

    /**
     * Update pheromones for a list of routes
     * @param routes A list of routes
     * @param Q Normalization factor for amount of dropped pheromone
     */
    public void addPheromoneRoutes(List<Route> routes, double Q) throws Exception {
        for (Route r : routes) {
            addPheromoneRoute(r, Q);
        }
    }

    /**
     * Evaporate pheromone
     * @param rho evaporation factor
     */
    public void evaporate(double rho) {
        for (int row = 0; row < pheromones.length; row++) {
            for (int column = 0; column < pheromones[0].length; column++) {
                this.pheromones[row][column] = this.pheromones[row][column] * (1-rho);
            }
        }
    }

    /**
     * Width getter
     * @return width of the maze
     */
    public int getWidth() {
        return width;
    }

    /**
     * Length getter
     * @return length of the maze
     */
    public int getLength() {
        return length;
    }


    /**
     * Returns a the amount of pheromones on the neighbouring positions (N/S/E/W).
     * @param position The position to check the neighbours of.
     * @return the pheromones of the neighbouring positions.
     */
    public SurroundingPheromone getSurroundingPheromone(Coordinate position) {
        double east = 0,west= 0,north= 0,south = 0;

        Coordinate dir = position.add(Direction.East);
        if(inBounds(dir)){
            east = pheromones[dir.getX()][dir.getY()];
        }

        dir = position.add(Direction.West);
        if(inBounds(dir)){
            west = pheromones[dir.getX()][dir.getY()];
        }
        dir = position.add(Direction.North);
        if(inBounds(dir)){
            north = pheromones[dir.getX()][dir.getY()];
        }
        dir = position.add(Direction.South);
        if(inBounds(dir)){
            south = pheromones[dir.getX()][dir.getY()];
        }
        return new SurroundingPheromone(north,east,south,west);
    }


    /**
     * Check whether a coordinate lies in the current maze.
     * @param position The position to be checked
     * @return Whether the position is in the current maze
     */
    private boolean inBounds(Coordinate position) {
        return position.xBetween(0, width) && position.yBetween(0, length);
    }

    /**
     * Representation of Maze as defined by the input file format.
     * @return String representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(' ');
        sb.append(length);
        sb.append(" \n");
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++ ) {
                sb.append(walls[x][y]);
                sb.append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method that builds a mze from a file
     * @param filePath Path to the file
     * @return A maze object with pheromones initialized to 0's inaccessible and 1's accessible.
     */
    public static Maze createMaze(String filePath) throws FileNotFoundException {
        Scanner scan = new Scanner(new FileReader(filePath));
        int width = scan.nextInt();
        int length = scan.nextInt();
        int[][] mazeLayout = new int[width][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                mazeLayout[x][y] = scan.nextInt();
            }
        }
        scan.close();
        return new Maze(mazeLayout, width, length);
    }
}
