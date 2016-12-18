package cz.lorenc;

import cz.lorenc.model.Item;
import cz.lorenc.model.LoadProblem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static Random rnd = new Random();

    public static void main(String[] args) {

        List<LoadProblem> problems = FileHelper.proceedFileTask("./data/in/knap_40.inst.dat");

        LoadProblem loadProblem = problems.get(48);

        GA ga = new GA(50,loadProblem.getItems(),loadProblem.getCapacity());
        int geneticResult = ga.evolution(500);

        System.out.println(loadProblem);
        System.out.println(ga);
        int dynamicResult = DynamicProgramming.dynamicProgramming(loadProblem);
        System.out.println("Ideální hodnota řešení:" + dynamicResult);
        System.out.println("Genetická hodnota řešení: " + geneticResult);
        System.out.println("Relativní chyba:" + ((dynamicResult - geneticResult) / dynamicResult));
    }
}
