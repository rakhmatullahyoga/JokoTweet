package preprocessor;

import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianStemmer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.tokenizers.Tokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class Preprocessor {
    private static final Logger LOGGER = Logger.getLogger(Preprocessor.class.getName());

    private IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
    private IndonesianStemmer stemmer = new IndonesianStemmer();

    /**
     * Stores the result of the stemming
     */
    private List<String> originalTweets = new LinkedList<>();

    /**
     * Stores the bag of words
     */
    private Instances result;

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

        for (CSVRecord record: csvParser) {
            originalTweets.add(record.get("content"));
        }

        csvParser.close();
    }

    public void process() {
        // prep the attributes first
        FastVector attributes = new FastVector(1);
        attributes.addElement(new Attribute("tweet", (FastVector) null));

        Instances instances = new Instances("filtered_tweets", attributes, 0);

        for (String tweet: originalTweets) {
            // 1. formalize and delete stop words
            String formalSentence = formalizer.deleteStopword(formalizer.formalizeSentence(tweet));

            // 2. stem
            String processedSentence = stemmer.stemSentence(formalSentence);

            // 3. add to instances
            double[] newInstanceValue = new double[1];
            newInstanceValue[0] = instances.attribute("tweet").addStringValue(processedSentence);
            instances.add(new Instance(1.0, newInstanceValue));
        }
        LOGGER.log(Level.INFO, String.format("Read %d tweets\n", instances.numInstances()));

        // create and setup the StringToWordVector
        StringToWordVector wordVector = new StringToWordVector();
        wordVector.setTokenizer(new WordTokenizer());

        try {
            wordVector.setInputFormat(instances);

            LOGGER.log(Level.INFO, "Creating bag of words...");
            result = Filter.useFilter(instances, wordVector);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportToCsv(String targetCsv) throws IOException {
        File output = new File(targetCsv);

        CSVSaver saver = new CSVSaver();
        saver.setFile(output);
        saver.setInstances(result);
        saver.writeBatch();
    }
}
