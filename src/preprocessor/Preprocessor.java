package preprocessor;

import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianStemmer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class Preprocessor {
    private static final Logger LOGGER = Logger.getLogger(Preprocessor.class.getName());

    private StringToWordVector wordVector = new StringToWordVector();
    private IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
    private IndonesianStemmer stemmer = new IndonesianStemmer();

    /**
     * Stores the result of the stemming
     */
    private List<String> originalTweets = new LinkedList<>();
    private List<String> result = new LinkedList<>();

    /**
     * Stores the instances of the tweets
     */
    private File tweetInstancesFile;

    public Preprocessor(String stopWordFile, String tweetFile) throws IOException {
        tweetInstancesFile = new File(tweetFile);
        if (!tweetInstancesFile.exists()) {
            throw new IOException("Tweet File does not exists!");
        }

        loadTweets();

        // initialize the formalizer
        formalizer.initStopword();
    }

    /**
     * Loads the tweets from the file
     *
     * @throws IOException
     */
    private void loadTweets() throws IOException {
        originalTweets.clear();

        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(tweetInstancesFile));

        System.out.println("Headers:");
        for (CSVRecord record: csvParser) {
            originalTweets.add(record.get("content"));
        }

        csvParser.close();
    }

    public void process() {
        result.clear();

        for (String tweet: originalTweets) {
            // 1. formalize
            String formalSentence = formalizer.formalizeSentence(tweet);

            // 2. stem
            String processedSentence = stemmer.stemSentence(formalSentence);

            // 3. toWordVector

            result.add(processedSentence);
        }
    }

    public void exportToCsv(String targetCsv) throws IOException {
        File output = new File(targetCsv);

        CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .withHeader("no", "original_tweet", "filtered")
                .print(new OutputStreamWriter(new FileOutputStream(targetCsv)));

        csvPrinter.close();
    }
}
