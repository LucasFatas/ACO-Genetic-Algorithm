import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

import random
from Route import Route
from Direction import Direction

#Class that represents the ants functionality.
class Ant:

    # Constructor for ant taking a Maze and PathSpecification.
    # @param maze Maze the ant will be running in.
    # @param spec The path specification consisting of a start coordinate and an end coordinate.
    def __init__(self, maze, path_specification):
        self.maze = maze
        self.start = path_specification.get_start()
        self.end = path_specification.get_end()
        self.current_position = self.start
        self.rand = random

    # Method that performs a single run through the maze by the ant.
    # @return The route the ant found through the maze.
    def find_route(self, path_length):
        route = Route(self.start)
        visited_list = []
        while self.current_position != self.end and route.size() < path_length:
            sur_pheromones = self.maze.get_surrounding_pheromone(self.current_position)
            current_pos = self.current_position
            visited_list.append(self.current_position)
            east_chance = sur_pheromones.get(Direction.east)
            north_chance = sur_pheromones.get(Direction.north)
            west_chance = sur_pheromones.get(Direction.west)
            south_chance = sur_pheromones.get(Direction.south)
            dead_end = [0,0,0,0]
            if current_pos.add_direction(Direction.east) in visited_list:
                east_chance = 0
            if current_pos.add_direction(Direction.north) in visited_list:
                north_chance = 0
            if current_pos.add_direction(Direction.west) in visited_list:
                west_chance = 0
            if current_pos.add_direction(Direction.south) in visited_list:
                south_chance = 0

            if dead_end.__eq__([east_chance, north_chance, west_chance, south_chance]):
                back_direction = route.remove_last()
                self.current_position = self.current_position.subtract(back_direction)
            else:
                directions = [Direction.east, Direction.north, Direction.west, Direction.south]
                direction = self.rand.choices(directions, weights=(east_chance, north_chance, west_chance, south_chance), k=1)[0]
                self.current_position = self.current_position.add_direction(direction)
                route.add(direction)

                if route.size() > path_length:
                    return Route(self.start);

        return route
