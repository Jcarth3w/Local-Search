package core_algorithms;

import optimization_problems.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * implement elements that are independent of any specific problem
 *
 *
 *
 */
public abstract class GeneticAlgorithm<G>
{
    private final int MAX_GEN;

    private final double MUTATION_RATE;

    private final double ELITISM;

    public GeneticAlgorithm(int maxGen, double mRate, double elitism)
    {
        this.MAX_GEN = maxGen;
        this.MUTATION_RATE = mRate;
        this.ELITISM = elitism;
    }

    public Individual<G> evolve(List<Individual<G>> initPop)
    {
        List<Individual<G>> population = initPop;
        for(int generation = 1; generation <= MAX_GEN; generation++)
        {
            List<Individual<G>> offSpring = new ArrayList<>();
            for(int i = 0; i < population.size(); i++)
            {
                Individual<G> p1 = selectParent(population);
                Individual<G> p2 = selectParent(population, p1);
                Individual<G> child = reproduce(p1, p2);
                if(new Random().nextDouble() <= MUTATION_RATE)
                {
                    child = mutate(child);
                }
                offSpring.add(child);
            }
            Collections.sort(population);
            Collections.sort(offSpring);
            List<Individual<G>> newPop = new ArrayList<>();
            int e = (int) (ELITISM * population.size());

            for(int i=0; i<e; i++)
            {
                newPop.add(population.get(i));
            }

            for(int i=0; i< population.size()-e; i++)
            {
                newPop.add(offSpring.get(i));
            }

            population = newPop;
        }

        Collections.sort(population);
        return population.get(0);
    }

    public abstract Individual<G> reproduce(Individual<G> p1, Individual<G> p2);

    public abstract Individual<G> mutate(Individual<G> i);

    public abstract double calcFitnessScore(List<G> chromosome);

    public Individual<G> selectParent(List<Individual<G>> population)
    {
        Random rand = new Random();
        int tournamentSize = population.size();

        Individual<G> bestParent = null;
        double bestFitness = -999;

        for (int i = 0; i < tournamentSize; i++) {
            Individual<G> randomIndividual = population.get(rand.nextInt(population.size()));
            double randomFitness = randomIndividual.getFitnessScore();
            if (randomFitness > bestFitness) {
                bestFitness = randomFitness;
                bestParent = randomIndividual;
            }
        }

        return bestParent;

    }

    //select parent that's not p
    public Individual<G> selectParent(List<Individual<G>> population, Individual<G> p)
    {
        Random rand = new Random();
        int tournamentSize = population.size();

        Individual<G> bestParent = null;
        double bestFitness = -999;

        for (int i = 0; i < tournamentSize; i++) {
            Individual<G> randomIndividual = population.get(rand.nextInt(population.size()));
            if (!randomIndividual.equals(p)) {
                double randomFitness = randomIndividual.getFitnessScore();
                if (randomFitness > bestFitness) {
                    bestFitness = randomFitness;
                    bestParent = randomIndividual;
                }
            }
        }
        return bestParent;
    }

}
