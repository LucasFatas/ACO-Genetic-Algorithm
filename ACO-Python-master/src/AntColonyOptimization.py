import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

import time
from Maze import Maze
from PathSpecification import PathSpecification
import Ant

# Class representing the first assignment. Finds shortest path between two points in a maze according to a specific
# path specification.
class AntColonyOptimization:

    # Constructs a new optimization object using ants.
    # @param maze the maze .
    # @param antsPerGen the amount of ants per generation.
    # @param generations the amount of generations.
    # @param Q normalization factor for the amount of dropped pheromone
    # @param path_length amount of steps an ant does before giving up
    # @param evaporation the evaporation factor.
    def __init__(self, maze, ants_per_gen, generations, q, evaporation, path_length):
        self.maze = maze
        self.ants_per_gen = ants_per_gen
        self.generations = generations
        self.q = q
        self.evaporation = evaporation
        self.path_length = path_length

     # Loop that starts the shortest path process
     # @param spec Spefication of the route we wish to optimize
     # @return ACO optimized route
    def find_shortest_route(self, path_specification):
        self.maze.reset()
        current_shortest_route = None
        for gen in range(self.generations):
            routes = []
            for ants in range(self.ants_per_gen):
                ant = Ant.Ant(self.maze, path_specification)
                current = ant.find_route(self.path_length)
                routes.append(current)
                if current_shortest_route is None or current.shorter_than(current_shortest_route):
                    current_shortest_route = current
            self.maze.evaporate(evap)
            self.maze.add_pheromone_routes(routes, self.q)
        return current_shortest_route

# Driver function for Assignment 1
if __name__ == "__main__":
    #parameters
    ants_per_gen = 10
    no_gen = 10
    q = 400
    evap = 0.1
    path_length = 1000

    #construct the optimization objects
    maze = Maze.create_maze("./../data/medium maze.txt")
    spec = PathSpecification.read_coordinates("./../data/medium coordinates.txt")
    aco = AntColonyOptimization(maze, ants_per_gen, no_gen, q, evap, path_length)

    #save starting time
    start_time = int(round(time.time() * 1000))

    #run optimization
    shortest_route = aco.find_shortest_route(spec)

    #print time taken
    print("Time taken: " + str((int(round(time.time() * 1000)) - start_time) / 1000.0))

    #save solution
    shortest_route.write_to_file("./../data/medium_solution.txt")

    #print route size
    print("Route size: " + str(shortest_route.size()))