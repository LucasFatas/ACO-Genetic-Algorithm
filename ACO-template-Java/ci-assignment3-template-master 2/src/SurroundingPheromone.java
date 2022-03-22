/**
 * Class containing the pheromone information around a certain point in the maze
 */
public class SurroundingPheromone {

    public double north;
    public double south;
    public double east;
    public double west;
    public final double totalSurroundingPheromone;

    /**
     * Creates a surrounding pheromone object.
     * @param north the amount of pheromone in the north.
     * @param east the amount of pheromone in the east.
     * @param south the amount of pheromone in the south.
     * @param west the amount of pheromone in the west.
     */
    public SurroundingPheromone(double north, double east, double south, double west) {
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        this.totalSurroundingPheromone = east + north + south + west;
    }

    /**
     * Get the total amount of surrouning pheromone.
     * @return total surrounding pheromone
     */
    public double getTotalSurroundingPheromone() {
        return this.totalSurroundingPheromone;
    }

    /**
     * Get a specific pheromone level
     * @param dir Direction of pheromone
     * @return Pheromone of dir
     */
    public double get(Direction dir) {
        switch (dir) {
            case North:
                return this.north;
            case East:
                return this.east;
            case West:
                return this.west;
            case South:
                return this.south;
        }
        throw new IllegalArgumentException("Invalid direction");
    }
}
