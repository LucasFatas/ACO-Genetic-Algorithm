# ci-assignment3-template
Template assignment 3 for the course Computational Intelligence TI2736-A

**Note**: all the information listed in this README can also be found in the appendix of Assignment 3.

You are free to use any IDE you want, however this template is setup for the Eclipse IDE.

## How to setup project in Eclipse?
1. Start Eclipse
2. Go to `File->Import` and select under `General` the `Existing Projects into Workspace` option.
3. Click `Browse` to select the root directory and pick the template folder. Eclipse will automatically find the `CIassignment3` project.
4. You should now have a project with a src and data folder, where the src folder contains
a number of Java classes.
5. You will need to add three run configurations. This allows you to select different classes in the same project to be run. This can be edited in `Run Configurations` under Run. Select `Java Application`.
6. Double click `Java Application` to generate a new configuration.
7. Rename your new configuration by entering a different value under the `Name` field.
Then select the main class which is `AntColonyOptimization` for the first assignment.
8. Repeat this and create new configurations for the other entry points
for the later parts of the assignment (`TSPData` and `GeneticAlgorithm`).
9. The last highlighted run configurations will be run when you press the run button in
the `Run Configuration` menu or the `Play` button in the main window in Eclipse.

## How is this template organized?
The template consists of a number of Java classes. There are a number of stubs/non-implemented functions that you will need to implement to solve the assignment. You can create extra functions/modify existing functions as you see fit.

### Part 1
We will start with an overview of the classes you will need for part 1.

The entry point for part 1 is `AntColonyOptimization`. All of the I/O has been implemented
and you should only have to worry about the algorithmic side of the problem.

Some information per class:
- **Ant** represents an ant finding a path through the maze. You will need to implement logic how an ant finds a route through the maze.
- **AntColonyOptimzation** driver function that finds an optimal route between two points.
Allows you to tune the parameters of the algorithm. You will need to implement the
creation of a maze, the ants to allow them to run through the maze.
- **Coordinate** represents a coordinate. All functions are implemented.
- **Direction** represents the directions an Ant can take. All functions are implemented.
- **Maze** represents a maze. You will need to implement the initialization, addition and reset of the pheromones. Besides this you will need to implement a function that returns SurroundingPheromone for a given coordinate.
- **PathSpecification** A class representing the pair of coordinates that indicate the start and end points of a route. Used as initialization for shortest paths/ant colony optimization.
- **Route** represents a route. All functions are implemented.
- **SurroundingPheromone** represents the surrounding pheromone for a given coordinate in
the maze. All functions are implemented.

### Part 2
For part 2 you will need to first generate a class containing all the shortest routes between all product combinations. Since this can take a while to generate it is a good idea to persist this to a file.


This file will function as the starting point for your genetic algorithm which will optimize the routes between the products.

Some information per class:
- **GeneticAlgorithm** driver function for the Genetic Algorithm. Takes a file that contains a 2D-matrix containing the routes you found and attempts to optimize those. You will need to implement the genetic algorithm itself.
- **TSPData** builds and stores the data for all the shortest paths between the products. Uses AntColonyOptimzation to fill the matrix. You will only have to tune the parameters.
Can write the final action file given a solution to the TSP problem.

