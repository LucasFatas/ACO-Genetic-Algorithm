import os, sys

sys.path.append(os.path.dirname(os.path.dirname(os.path.realpath(__file__))))

import random
import numpy as np
from src.TSPData import TSPData


# TSP problem solver using genetic algorithms.
class GeneticAlgorithm:

    # Constructs a new 'genetic algorithm' object.
    # @param generations the amount of generations.
    # @param popSize the population size.
    def __init__(self, generations, pop_size, p_crossover, p_mutation):
        self.generations = generations
        self.pop_size = pop_size
        self.p_crossover = p_crossover
        self.p_mutation = p_mutation
        self.chromosomes = None
        self.chromosome_size = None
        self.tsp_data = None

    # Knuth-Yates shuffle, reordering a array randomly
    # @param chromosome array to shuffle.
    def shuffle(self, chromosome):
        n = len(chromosome)
        for i in range(n):
            r = i + int(random.uniform(0, 1) * (n - i))
            swap = chromosome[r]
            chromosome[r] = chromosome[i]
            chromosome[i] = swap
        return chromosome

    # This method should solve the TSP.
    # @param pd the TSP data.
    # @return the optimized product sequence.
    def solve_tsp(self, tsp_data):
        self.tsp_data = tsp_data
        self.chromosome_size = tsp_data[0]
        self.create_population()

        for i in range(generations):
            self.new_population()

        fitness = self.population_fitness()

        optimal_path = self.chromosomes[fitness.index(fitness.max())]
        return optimal_path

    # This method creates the initial population
    def create_population(self):
        self.chromosomes = []
        for i in range(self.pop_size):
            chromosome = np.arrange(0, self.chromosome_size)
            self.chromosomes.append(self.shuffle(chromosome))

    # This method calculates the total distance of a route in a chromosome
    # @returns the total distance
    def calculate_distance(self, chromosome):
        distance = self.tsp_data.start_to_product[chromosome[0]]

        for i in range(self.chromosome_size - 1):
            distance += self.tsp_data.product_to_product[chromosome[i], chromosome[i + 1]]

        distance += self.tsp_data.product_to_end[chromosome[self.chromosome_size - 1]]

        return distance

    # This method calculates the fitness of the whole population
    # Other fitness functions should be considered
    # @returns array with the fitness of each chromosome
    def population_fitness(self):
        fitness = []

        for c in self.chromosomes:
            fitness.append(1 / self.calculate_distance(c))  # take inverse since trying to find the smallest distance

        fitness_sum = fitness.sum()

        unit_vector = fitness / fitness_sum

        return unit_vector

    # This method selects the chromosomes for reproduction
    def select_chromosomes(self, fitness):
        selected_chromosomes = []

        for i in range(self.population_size):
            chromosome_index = np.random.choice(self.chromosome_size, self.chromosome_size, fitness)
            selected_chromosomes.append(self.chromosomes[chromosome_index])

        return selected_chromosomes

    # This method implements Non-Wrapping Ordered Crossover (NWOX), chose based on the study below
    # https://arxiv.org/pdf/1203.3097.pdf
    def crossover_operation(self, parent1, parent2):
        child1 = []
        child2 = []

        crossover_points = np.random.randint(1, self.chromosome_size, size=2)

        a = crossover_points.min()
        b = crossover_points.max()

        child1.append(parent1[0:a])
        child2.append(parent2[0:a])

        child1.append(parent2[a:b + 1])
        child2.append(parent1[a:b + 1])

        child1.append(parent1[b + 1:self.chromosome_size])
        child2.append(parent2[b + 1:self.chromosome_size])

        return [child1, child2]

    # This method implements Reverse Sequence Mutation (RSM), chose based on this study below
    # https://arxiv.org/pdf/1203.3099.pdf
    def mutation_operation(self, chromosome):
        swap_points = np.random.choice(self.chromosome_size, 2, replace=False)

        temp = chromosome[swap_points[0]]
        chromosome[swap_points[0]] = chromosome[swap_points[1]]
        chromosome[swap_points[1]] = temp

    # This method handles the process of creating a new generation
    def new_population(self):
        fitness = self.population_fitness()
        selected_chromosomes = self.select_chromosomes(fitness)

        # ASK TA ABOUT ODD NUMBER OF PARENTS
        # crossover
        for i in range(self.pop_size-1):
            if np.random.random < self.p_crossover:
                new_chromosomes = self.crossover_operation(selected_chromosomes[i], selected_chromosomes[i+1])

                self.chromosomes[i] = new_chromosomes[0]
                self.chromosomes[i+1] = new_chromosomes[1]

        # mutation
        for i in range(self.pop_size):
            if np.random.random < self.p_mutation:
                self.mutation_operation(selected_chromosomes[i])

        self.chromosomes = selected_chromosomes


# Assignment 2.b
if __name__ == "__main__":
    # parameters
    population_size = 20
    generations = 20
    persistFile = "./../tmp/productMatrixDist"

    # setup optimization
    tsp_data = TSPData.read_from_file(persistFile)
    ga = GeneticAlgorithm(generations, population_size)

    # run optimzation and write to file
    solution = ga.solve_tsp(tsp_data)
    tsp_data.write_action_file(solution, "./../data/TSP solution.txt")
