package optimization_solutions;


import core_algorithms.GeneticAlgorithm;
import optimization_problems.Individual;
import optimization_problems.TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * Implement elements that are problem specific
 *
 *
 *
 */
public class GeneticAlgorithm_TSP extends GeneticAlgorithm<Integer>
{
    private final TSP problem;

    public GeneticAlgorithm_TSP(int maxGen, double mRate, double elitism, TSP problem)
    {
        super(maxGen, mRate, elitism);
        this.problem = problem;
    }

    @Override
    public Individual<Integer> reproduce(Individual<Integer> p1, Individual<Integer> p2)
    {
        List<Integer> childChromosome = new ArrayList<>();

        Random rand = new Random();

        List<Integer> p1Chromosome = p1.getChromosome();
        List<Integer> p2Chromosome = p2.getChromosome();

        int size = p1Chromosome.size();

        // Step 1: Copy a portion of parent 1's chromosome to the child
        int crossoverPoint = rand.nextInt(size);
        for (int i = 0; i < crossoverPoint; i++) {
            childChromosome.add(p1Chromosome.get(i));
        }

        // Step 2: Copy the remaining genes from parent 2 to the child
        for (int i = 0; i < size; i++) {
            if (!childChromosome.contains(p2Chromosome.get(i))) {
                childChromosome.add(p2Chromosome.get(i));
            }
        }

        return new Individual<>(childChromosome, calcFitnessScore(childChromosome));

    }

    @Override
    public Individual<Integer> mutate(Individual<Integer> i)
    {
        List<Integer> chromosome = i.getChromosome();
        int size = chromosome.size();

        Random rand = new Random();
        int randomIndex1 = rand.nextInt(size);
        int randomIndex2 = rand.nextInt(size);

        // Make sure the two random indexes are different
        while (randomIndex1 == randomIndex2) {
            randomIndex2 = rand.nextInt(size);
        }

        // Swap the elements at randomIndex1 and randomIndex2
        int temp = chromosome.get(randomIndex1);
        chromosome.set(randomIndex1, chromosome.get(randomIndex2));
        chromosome.set(randomIndex2, temp);

        return i;
    }

    public double calcFitnessScore(List<Integer> chromosome)
    {
        return 1/problem.cost(chromosome);
    }

    public List<Individual<Integer>> generateInitPopulation(int popSize, int numCities)
    {
        List<Individual<Integer>> population = new ArrayList<>(popSize);

        for(int i=0; i<popSize; i++)
        {
            List<Integer> chromosome = new ArrayList<>(numCities);
            for(int j=0; j<numCities; j++)
            {
                chromosome.add(j);
            }
            Collections.shuffle(chromosome);
            Individual<Integer> indiv = new Individual<>(chromosome, calcFitnessScore(chromosome));
            population.add(indiv);
        }
        return population;
    }

    public static void main(String[] args) {
        int MAX_GEN = 200;
        double MUTATION_RATE = 0.05;
        int POPULATION_SIZE = 1000;
        int NUM_CITIES = 26; //choose 5,6,17,26
        double ELITISM = 0.2;

        TSP problem = new TSP(NUM_CITIES);

        GeneticAlgorithm_TSP agent = new GeneticAlgorithm_TSP(MAX_GEN, MUTATION_RATE, ELITISM, problem);

        Individual<Integer> best = agent.evolve(agent.generateInitPopulation(POPULATION_SIZE, NUM_CITIES));

        System.out.println(best);
        System.out.println(problem.cost(best.getChromosome()));
    }


}
