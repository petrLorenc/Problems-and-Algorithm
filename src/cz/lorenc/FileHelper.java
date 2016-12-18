package cz.lorenc;

import cz.lorenc.model.Item;
import cz.lorenc.model.LoadProblem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by petr.lorenc on 11.12.16.
 */
public class FileHelper {

    public static List<LoadProblem> proceedFileTask(String path) {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<LoadProblem> problems =
                    stream.map(w -> w.split("\\s+"))
                            .flatMap(FileHelper::mapStringToProblem)
                            .collect(Collectors.toList());
            return problems;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static Stream<LoadProblem> mapStringToProblem(String[] strings) {
        //ID	n	M	v?ha	cena	v?ha	cena	?	?
        LoadProblem problem = new LoadProblem(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        for (int i = 3; i < strings.length; i++, i++) {
            problem.addItem(new Item(Integer.parseInt(strings[i]), Integer.parseInt(strings[i + 1])));
        }
        return Stream.of(problem);
    }

}
