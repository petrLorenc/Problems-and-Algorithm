package cz.lorenc;

import cz.lorenc.model.Chromosome;
import cz.lorenc.model.Item;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Algorithm class
 * Created by petr.lorenc on 09.12.16.
 */
public class GA {
    // PARAMETRY K UPRAVOVANI------------------------------------------------------------------
    public static final int SIZE_OF_POPULATION = 50;
    public static final int NUMBER_OF_STEP_IN_EVOLUTION = 500;
    public static final boolean ROULETE_SELECTION = true;
    public static final int SIZE_OF_TOURNAMENT = 3;


    public static final float PROBABILITY_CROSSOVER = 0.85f;
    public static final float PROBABILITY_MUTATION = 0.01f;

    // for lineat scaling -> pri rizeni selekciho tlaku
    public static final float NEW_MAX = 1000;
    public static final float NEW_MIN = 100;

    public static final boolean ELITISMUS = false;
    public static final int ELITISMUS_LIMIT = 10; // what is considered to be elite - not taken only best one
    //-----------------------------------------------------------------------------------------

    Population population;
    int sizeOfPopulation;
    List<Item> items;
    int numberOfItem;
    int capacity;

    public GA(int sizeOfPopulation, List<Item> items, int capacity) {
        this.sizeOfPopulation = sizeOfPopulation;
        this.items = items;
        this.numberOfItem = items.size();
        this.capacity = capacity;
    }

    public int evolution(int numberOfGeneration){
        int bestFitnessOverAll = 0;
        Chromosome bestChromozomeOverAll = null;

        List<Integer> bestScores = new ArrayList<>();
        List<Integer> worstScores = new ArrayList<>();
        List<Integer> averageScores = new ArrayList<>();


        this.population = Population.getRandomPopulation(this.sizeOfPopulation,this.numberOfItem);

        for (int i = 0; i < numberOfGeneration; i++) {

            //Samotna evoluce
            this.population = oneIteration(this.population);



            //Pocitani pro graf


            int averageFitness = 0;
            Chromosome bestChromozomeInGeneration = this.population.getPopulation().get(0);
            Chromosome worstChromozomeInGeneration = this.population.getPopulation().get(0);
            for (Chromosome chromosome : this.population.getPopulation()) {
                int currentFitness = chromosome.getFitness(this.items,capacity);
                if(currentFitness > bestChromozomeInGeneration.getFitness(this.items,capacity)){
                    bestChromozomeInGeneration = chromosome;
                }
                if(currentFitness < worstChromozomeInGeneration.getFitness(this.items,capacity)){
                    worstChromozomeInGeneration = chromosome;
                }
                averageFitness += currentFitness;
            }


            int currentBestFitness = bestChromozomeInGeneration.getFitness(items,capacity);
            int currentWorstFitness = worstChromozomeInGeneration.getFitness(items,capacity);
            int currentAverageFitness = averageFitness / sizeOfPopulation;
            bestScores.add(currentBestFitness);
            worstScores.add(currentWorstFitness);
            averageScores.add(currentAverageFitness);
//            System.out.println("Best in generation :" + currentBestFitness);
//            System.out.println("Worst in generation :" + currentWorstFitness);
//            System.out.println("Average in generation :" + currentAverageFitness);
//            System.out.println("--");

            if(currentBestFitness > bestFitnessOverAll){
                bestFitnessOverAll = currentBestFitness;
                bestChromozomeOverAll = bestChromozomeInGeneration;
            }

        }
//        for (int i = 0; i < bestChromozomeOverAll.getVariationOfSolution().length; i++) {
//            System.out.println(items.get(i) + "\t=\t"+bestChromozomeOverAll.getVariationOfSolution()[i]);
//        }

        //have to reasign because of lambda expression
        int finalBestFitnessOverAll = bestFitnessOverAll;
        SwingUtilities.invokeLater(() -> Graph.createAndShowGui(bestScores,averageScores,worstScores, finalBestFitnessOverAll));
        return bestFitnessOverAll;
    }

    private Population oneIteration(Population orinignal){
        //selection
        List<Chromosome> selectedChromozomes = null;
        if(ROULETE_SELECTION) {
            selectedChromozomes = orinignal.rouleteSelection(items, capacity);
        }else{
            selectedChromozomes = orinignal.tournamentSelection(items, capacity, SIZE_OF_TOURNAMENT);
        }
        List<Chromosome> crossOverPopulation = new ArrayList<>();

        //krizeni
        while(crossOverPopulation.size() != selectedChromozomes.size()){
            int firstChoice = Main.rnd.nextInt(sizeOfPopulation / 2); // fist half of population
            int secondChoice = Main.rnd.nextInt(sizeOfPopulation / 2) + sizeOfPopulation / 2; // second half of population

            // pokud se hodnota pod PROBABILITY_CROSSOVER bude vybrane krizit
            if(Main.rnd.nextFloat() < PROBABILITY_CROSSOVER) {
                crossOverPopulation.addAll(Chromosome
                        .crossover(selectedChromozomes.get(firstChoice),
                                selectedChromozomes.get(secondChoice),
                                numberOfItem));
            } else {
                // pokud je hodnota vetsi nez PROBABILITY_CROSSOVER tak pridam originalni
                crossOverPopulation.add(selectedChromozomes.get(firstChoice));
                crossOverPopulation.add(selectedChromozomes.get(secondChoice));
            }

            // osetreni pokud bych nahodou presahl
            while (crossOverPopulation.size() > selectedChromozomes.size()){
                crossOverPopulation.remove(0);
            }
        }

        //mutace
        for (int i = 0; i < sizeOfPopulation; i++) {
            if(Main.rnd.nextFloat() < PROBABILITY_MUTATION){
                crossOverPopulation.get(i).mutate(numberOfItem);
            }
        }

        // vrati novou populaci vytvorenou z upravenych chromozomu o velikosti ktera je globalni pro vsechny
        return Population.getPopulationFromChromosomes(crossOverPopulation,sizeOfPopulation);
    }

    /**
     * Projde vsechny jedince v populaci a najde toho nejlepsiho podle fitness
     * @return
     */
    private Chromosome getBestFitnessInPopulation(){
        Chromosome bestFitness = this.population.getPopulation().get(0);
        for (Chromosome chromosome : this.population.getPopulation()) {
            if(chromosome.getFitness(this.items,capacity) > bestFitness.getFitness(this.items,capacity)){
                bestFitness = chromosome;
            }
        }
        return bestFitness;
    }

    public static String getDescriptionOfParam(){
        StringBuilder builder = new StringBuilder();
        builder.append("Velikost populace: " + SIZE_OF_POPULATION + "\n");
        builder.append("Počet kroků evoluce: " + NUMBER_OF_STEP_IN_EVOLUTION + "\n");
        builder.append("Pravděpodobnost křížení: " + PROBABILITY_CROSSOVER + "\n");
        builder.append("Pravděpodobnost mutace: " + PROBABILITY_MUTATION + "\n");
        builder.append("Elitismus: " + ELITISMUS + "\n");
        builder.append("Selekční algoritmus: " + (ROULETE_SELECTION ? "RULETA": "TURNAJ" ) + "\n");
        builder.append("Velikost turnaje (pokud vybrána turnajová selekce): " + SIZE_OF_TOURNAMENT + "\n");
        return builder.toString();
    }

}
