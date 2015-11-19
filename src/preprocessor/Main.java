package preprocessor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class Main {
    public static void main(String args[]) {
        try {
            Preprocessor preprocessor = new Preprocessor("./data/stopword.txt", "./data/jokowi_sort_uniq2.csv");
            preprocessor.process();

            preprocessor.exportToCsv("./data/preprocessor-test.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
