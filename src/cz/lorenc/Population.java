package cz.lorenc;

import cz.lorenc.model.Chromosome;
import cz.lorenc.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr.lorenc on 09.12.16.
 */
public class Population {

    private int sizePopulation;
    private List<Chromosome> population;


    private Population(int sizePopulation){
        this.sizePopulation = sizePopulation;
        population = new ArrayList<>(this.sizePopulation);
    }


    public List<Chromosome> getPopulation() {
        return population;
    }


    public void setPopulation(List<Chromosome> population) {
        this.population = population;
    }

    public List<Chromosome> tournamentSelection(List<Item> items, int capacity, int sizeOfTournament){
        List<Chromosome> newGeneration = new ArrayList<>();
        do {
            List<Chromosome> tournament = new ArrayList<>(sizeOfTournament);
            for (int i = 0; i < sizeOfTournament; i++) {
                int randomSelect = Main.rnd.nextInt(sizeOfTournament);
                tournament.add(getPopulation().get(randomSelect));
            }

            int bestFitness = -1;
            int bestIndex = 0;
            for (int i = 0; i < tournament.size(); i++) {
                int actualFitness = tournament.get(i).getFitness(items, capacity);
                if (actualFitness > bestFitness) {
                    bestFitness = actualFitness;
                    bestIndex = i;
                }
            }
            newGeneration.add(tournament.get(bestIndex));
        }while(newGeneration.size() < getPopulation().size());

        return newGeneration;
    }

    public List<Chromosome> rouleteSelection(List<Item> items, int capacity){

        List<Chromosome> newGeneration = new ArrayList<>();
        List<Chromosome> actualGeneration = getPopulation();
        List<Chromosome> elite = new ArrayList<>();

        int sumOfFitness = 0;
        List<Integer> indexes = new ArrayList<>();


        int maxFitness = 0;
        int minFitness = Integer.MAX_VALUE;
        for (int i = 0; i < actualGeneration.size(); i++) {
            int actualFitness = actualGeneration.get(i).getFitness(items, capacity);
            if(actualFitness > maxFitness){
                maxFitness = actualFitness;
            }
            if(actualFitness < minFitness){
                minFitness = actualFitness;
            }
        }

        for (int i = 0; i < actualGeneration.size(); i++) {
            int actualFitness = actualGeneration.get(i).getFitness(items,capacity);

            if(GA.ELITISMUS && Math.abs(actualFitness - maxFitness) < GA.ELITISMUS_TOLERANCE){
                elite.add(actualGeneration.get(i));
            } else { // dont need to choose again - already in generation
                int actualFitnessScaling = linearScaling(maxFitness, minFitness, actualFitness);
                sumOfFitness += actualFitnessScaling;
                for (int a = 0; a < actualFitnessScaling; a++) {
                    indexes.add(i);
                }
            }
        }


        if(GA.ELITISMUS){
            newGeneration.addAll(elite);
        }

        do{
            newGeneration.add(actualGeneration.get(indexes.get(Main.rnd.nextInt(sumOfFitness))));
        }while (newGeneration.size() < actualGeneration.size());

        return  newGeneration;
    }

    private int linearScaling(int localMax, int localMin, int actual){
        if(localMax == localMin){
            return (int) GA.NEW_MIN;
        }
        int x = (int) ((GA.NEW_MIN+(actual - localMin))*((GA.NEW_MAX - GA.NEW_MIN)/(localMax-localMin)));
        return x;
    }

    public static Population getRandomPopulation(int sizePopulation, int sizeOfChromosome){
        Population population = new Population(sizePopulation);
        for (int i = 0; i < sizePopulation; i++) {
            population.getPopulation().add(Chromosome.getRandomChromosome(sizeOfChromosome));
        }
        return population;
    }

    public static Population getPopulationFromChromosomes(List<Chromosome> chromosomes, int sizePopulation){
        Population population = new Population(sizePopulation);
        population.getPopulation().addAll(chromosomes);
        return population;
    }

}
