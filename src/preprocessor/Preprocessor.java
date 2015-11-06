package preprocessor;

import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianStemmer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class Preprocessor {

    private StringToWordVector wordVector = new StringToWordVector();
    private IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
    private IndonesianStemmer stemmer = new IndonesianStemmer();

    /**
     * Stores the result of the stemming
     */
    private List<String> result = new LinkedList<>();

    /**
     * Stores the instances of the tweets
     */
    private File tweetInstancesFile;

    /**
     * Stores
     */
    private StopWordFilter stopWordFilter;

    public Preprocessor(String filterFile, String tweetFile) throws IOException {
        tweetInstancesFile = new File(tweetFile);
        if (!tweetInstancesFile.exists()) {
            throw new IOException("Tweet File does not exists!");
        }

        stopWordFilter = new StopWordFilter(filterFile);
    }

    public void process() {
        // TODO: fetch a string from the file
    }
}
