import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Class that represents the ants functionality.
 */
public class Ant {

    public Maze maze;
    public Coordinate start;
    public Coordinate end;
    public Coordinate currentPosition;
    public static Random rand;

    /**
     * Constructor for ant taking a Maze and PathSpecification.
     *
     * @param maze Maze the ant will be running in.
     * @param spec The path specification consisting of a start coordinate and an end coordinate.
     */
    public Ant(Maze maze, PathSpecification spec) {
        this.maze = maze;
        this.start = spec.getStart();
        this.end = spec.getEnd();
        this.currentPosition = start;
        if (rand == null) {
            rand = new Random();
        }

    }

    /**
     * Method that performs a single run through the maze by the ant.
     *
     * @return The route the ant found through the maze.
     */
    public Route findRoute(int path_length) {
        Route route = new Route(this.start);
        HashSet<Coordinate> visited_list = new HashSet<>();
        List<Double> deadEnd = List.of(0.0, 0.0, 0.0, 0.0);
        while (!this.currentPosition.equals(this.end)) {

            SurroundingPheromone surroundingPheromone = this.maze.getSurroundingPheromone(currentPosition);
            Coordinate currentPos = this.currentPosition;
            //System.out.println(this.currentPosition);
            visited_list.add(currentPos);

            double eastChance = surroundingPheromone.get(Direction.East);
            double northChance = surroundingPheromone.get(Direction.North);
            double westChance = surroundingPheromone.get(Direction.West);
            double southChance = surroundingPheromone.get(Direction.South);

            if (visited_list.contains(currentPos.add(Direction.East))) {
                eastChance = 0.0;
            }

            if (visited_list.contains(currentPos.add(Direction.North))) {
                northChance = 0.0;
            }

            if (visited_list.contains(currentPos.add(Direction.West))) {
                westChance = 0.0;
            }

            if (visited_list.contains(currentPos.add(Direction.South))) {
                southChance = 0.0;
            }

            if (deadEnd.equals(List.of(eastChance, northChance, westChance,  southChance))) {

                Direction back_direction = route.removeLast();

                this.currentPosition = this.currentPosition.subtract(back_direction);

            } else {
                double totalSurrounding = eastChance + westChance + northChance + southChance;
                eastChance /= totalSurrounding;
                northChance /= totalSurrounding;
                westChance /= totalSurrounding;
                southChance /= totalSurrounding;
                Direction direction = directionRandomizer(List.of(eastChance, northChance, westChance,  southChance));
                this.currentPosition = this.currentPosition.add(direction);
                route.add(direction);
            }
//            if (route.size() > path_length) {
//                return new Route(start);
//            }
        }

        return route;
    }

    private Direction directionRandomizer(List<Double> list) {

        // cumulative problem

        rand = new Random();
        double value = rand.nextDouble();
        double e1 = list.get(0);
        double e2 = e1 + list.get(1);
        double e3 = e2 + list.get(2);
        double e4 = e3 + list.get(3);

        if (value <= e1)
            return Direction.East;
        else if (value <= e2)
            return Direction.North;
        else if (value <= e3)
            return Direction.West;
        else return Direction.South;

    }
}

