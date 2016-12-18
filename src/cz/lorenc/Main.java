package cz.lorenc;

import cz.lorenc.model.Item;
import cz.lorenc.model.LoadProblem;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static Random rnd = new Random();

    public static void main(String[] args) {

        List<LoadProblem> problems = FileHelper.proceedFileTask("./data/in/knap_40.inst.dat");

        LoadProblem loadProblem = problems.get(48);

        GA ga = new GA(GA.SIZE_OF_POPULATION,loadProblem.getItems(),loadProblem.getCapacity());
        int geneticResult = ga.evolution(GA.NUMBER_OF_STEP_IN_EVOLUTION);

        System.out.println(loadProblem);
        System.out.println(GA.getDescriptionOfParam());
        int dynamicResult = DynamicProgramming.dynamicProgramming(loadProblem);
        System.out.println("Ideální hodnota řešení:" + dynamicResult);
        System.out.println("Genetická hodnota řešení: " + geneticResult);
        System.out.println("Relativní chyba:" + ((dynamicResult - geneticResult) / dynamicResult));

        try(FileWriter fw = new FileWriter("data.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {

            out.println(loadProblem);
            out.println(GA.getDescriptionOfParam());
            out.println("Ideální hodnota řešení:" + dynamicResult);
            out.println("Genetická hodnota řešení: " + geneticResult);
            out.println("Relativní chyba:" + ((dynamicResult - geneticResult) / dynamicResult));
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
