package cz.lorenc.model;

import cz.lorenc.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr.lorenc on 09.12.16.
 */
public class Chromosome {
    public static final float TREASHOLD_GENERATING = 0.5f;

    boolean[] variationOfSolution;

    public Chromosome(int sizeGen) {
        this.variationOfSolution = new boolean[sizeGen]; // all values are set to false by default

    }

    public boolean[] getVariationOfSolution() {
        return variationOfSolution;
    }

    public void setVariationOfSolution(boolean[] variationOfSolution) {
        this.variationOfSolution = variationOfSolution;
    }

    /***
     * Modify original Chromosome, if the fitness function says that this chromozome has bad fitness
     * @return
     */
    public int getFitness(List<Item> items, int capacity){
        int fullness = 0;
        int price = 0;
        for (int i = 0; i < variationOfSolution.length; i++) {
            fullness += variationOfSolution[i] ? items.get(i).getWeight() : 0;
            price += variationOfSolution[i] ? items.get(i).getPrice() : 0;

        }
        if(fullness > capacity){
//            do{
//               int randomPosition = Main.rnd.nextInt(variationOfSolution.length);
//                if(variationOfSolution[randomPosition]){
//                    variationOfSolution[randomPosition] = false;
//                    fullness -= items.get(randomPosition).getWeight();
//                    price -= items.get(randomPosition).getPrice();
//                }
//            }while (fullness > capacity);
            return 0;
        }

        return price;
    }

    /**
     * Get fitness without modifiyng original chromosome
     * @param items
     * @return
     */
    public int getFitness(List<Item> items){
        int price = 0;
        for (int i = 0; i < variationOfSolution.length; i++) {
            price += variationOfSolution[i] ? items.get(i).getPrice() : 0;
        }
        return price;
    }

    public static List<Chromosome> crossover(Chromosome firstParent, Chromosome secondParent, int sizeChromosome){
        Chromosome child1 = new Chromosome(sizeChromosome);
        Chromosome child2 = new Chromosome(sizeChromosome);
        int crossPoint = Main.rnd.nextInt(sizeChromosome);
        for (int i = 0; i < crossPoint; i++) {
            child1.variationOfSolution[i] = firstParent.variationOfSolution[i];
            child2.variationOfSolution[i] = secondParent.variationOfSolution[i];
        }
        for (int i = crossPoint; i < sizeChromosome; i++) {
            child1.variationOfSolution[i] = secondParent.variationOfSolution[i];
            child2.variationOfSolution[i] = firstParent.variationOfSolution[i];
        }
        List<Chromosome> returnValue = new ArrayList<>();
        returnValue.add(child1);
        returnValue.add(child2);
        return returnValue;
    }
    
    public Chromosome mutate(int sizeChromozome){
        Chromosome newChromosome = new Chromosome(sizeChromozome);
        newChromosome.setVariationOfSolution(this.variationOfSolution);
        int mutatePoint = Main.rnd.nextInt(sizeChromozome);
        newChromosome.variationOfSolution[mutatePoint] = !newChromosome.variationOfSolution[mutatePoint];
        return newChromosome;
    }
    
    public static Chromosome getRandomChromosome(int sizeGen){
        Chromosome chromosome = new Chromosome(sizeGen);
        int numberOfAddedItems = Main.rnd.nextInt(sizeGen);
        for (int i = 0; i < numberOfAddedItems; i++) {
            int position = Main.rnd.nextInt(sizeGen);
            if(Main.rnd.nextFloat() > TREASHOLD_GENERATING){
                chromosome.variationOfSolution[position] = true;
            }
        }
        return chromosome;
    }
}
