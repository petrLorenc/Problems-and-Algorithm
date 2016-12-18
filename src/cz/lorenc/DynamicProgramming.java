package cz.lorenc;

import cz.lorenc.model.Item;
import cz.lorenc.model.LoadProblem;

/**
 * Created by petr.lorenc on 14.12.16.
 */
public class DynamicProgramming {

    public static int dynamicProgramming(LoadProblem problem) {
        int maxPrice = (int) problem.getItems().stream().mapToInt(Item::getPrice).sum();
        int[][] arr = new int[problem.getNumberOfItem() + 1][maxPrice + 1]; // + 1 because of 0 fields

        for (int i = 0; i < arr.length; i++) {
            for (int j = 1; j < arr[i].length; j++) {
                arr[i][j] = Integer.MAX_VALUE;
            }
        }

        // calculation
        // on posion i=0 is no items in bag
        for (int i = 1; i < arr.length; i++) {
            for (int c = 0; c < arr[i].length; c++) {
                arr[i][c] = getFunctionValue(problem, arr, i, c);
            }
        }

        int bestSolution = 0;
        for (int j = arr[arr.length - 1].length - 1; j >= 0; j--) {
            if (arr[arr.length - 1][j] <= problem.getCapacity()) {
                bestSolution = j;
                break;
            }
        }

        boolean[] variation = getVariationOfItem(problem, arr, problem.getNumberOfItem(), bestSolution);

        bestSolution = 0;
        for (int i = 0; i < variation.length; i++) {
            if (variation[i]) {
                bestSolution += problem.getItems().get(i).getPrice();
            }
        }

        return bestSolution;
    }

    private static int getFunctionValue(LoadProblem problem, int[][] arr, int ithThing, int price) {
        Item item = problem.getItems().get(ithThing - 1);
        if (price - item.getPrice() < 0 || arr[ithThing - 1][price - item.getPrice()] == Integer.MAX_VALUE) {
            return arr[ithThing - 1][price];
        }
        return Math.min(arr[ithThing - 1][price], arr[ithThing - 1][price - item.getPrice()] + item.getWeight());
    }

    private static boolean[] getVariationOfItem(LoadProblem problem, int[][] arr, int itn, int price) {
        boolean[] variation = new boolean[problem.getNumberOfItem()];

        for (int i = variation.length - 1; i >= 0; i--) {
            variation[i] = isThingInABag(problem, arr, itn, price);
            if (variation[i]) {
                price = price - problem.getItems().get(itn - 1).getPrice();
            }
            itn--;
        }

        return variation;
    }

    private static boolean isThingInABag(LoadProblem problem, int[][] arr, int itn, int price) {
        return !(arr[itn - 1][price] == arr[itn][price]);
    }
}
