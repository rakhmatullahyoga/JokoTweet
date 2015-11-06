package preprocessor;

import java.io.IOException;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class Main {
    public static void main(String args[]) {
        try {
            Preprocessor preprocessor = new Preprocessor("./data/stopword.txt", "./data/jokowi_sort_uniq.csv");
            preprocessor.process();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
