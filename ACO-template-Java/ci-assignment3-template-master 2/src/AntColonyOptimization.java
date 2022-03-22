import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the first assignment. Finds shortest path between two points in a maze according to a specific
 * path specification.
 */
public class AntColonyOptimization {
	
	public int antsPerGen;
    public int generations;
    public double Q;
    public double evaporation;
    public Maze maze;
    public int path_length;

    /**
     * Constructs a new optimization object using ants.
     * @param maze the maze .
     * @param antsPerGen the amount of ants per generation.
     * @param generations the amount of generations.
     * @param Q normalization factor for the amount of dropped pheromone
     * @param evaporation the evaporation factor.
     */
    public AntColonyOptimization(Maze maze, int antsPerGen, int generations, double Q, double evaporation, int path_length) {
        this.maze = maze;
        this.antsPerGen = antsPerGen;
        this.generations = generations;
        this.Q = Q;
        this.evaporation = evaporation;
        this.path_length = path_length;
    }

    /**
     * Loop that starts the shortest path process
     * @param spec Spefication of the route we wish to optimize
     * @return ACO optimized route
     */
    public Route findShortestRoute(PathSpecification spec) throws Exception {
        maze.reset();
        Route currentShortest = null;
        for(int i = 0; i < generations; i++){
            List<Route> routeList = new ArrayList<>();
            for(int j = 0; j < antsPerGen; j++){
                Ant ant = new Ant(maze, spec);
                Route current = ant.findRoute(path_length);
                routeList.add(current);
                if(currentShortest == null || current.shorterThan(currentShortest)){
                    currentShortest = current;
                }
            }
            maze.evaporate(evaporation);
            maze.addPheromoneRoutes(routeList, this.Q);
        }
        return currentShortest;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws Exception {
    	//parameters
    	int gen = 3000;
        int noGen = 100;
        double Q = 400;
        double evap = 0.15;
        int path_length = 4800;
        
        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/hard maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/hard coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap, path_length);
        
        //save starting time
        long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/hard_solution.txt");
        
        //print route size
        System.out.println("Route size: " + shortestRoute.size());
    }
}
