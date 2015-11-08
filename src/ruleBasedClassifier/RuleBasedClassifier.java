/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruleBasedClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Hayyu' Luthfi Hanifah
 */
public class RuleBasedClassifier {
    //All the file needed in this class.
    private File inFile;
    private File outFile;
    private File ecoDictFile;
    private File eduDictFile;
    private File lawpolDictFile;
    
    //Priority of the categories, initialized by default value.
    private int ecoPriority = 0;
    private int eduPriority = 1;
    private int lawpolPriority = 2;
    
    //Words dictionaries.
    private Map<String, Integer> ecoDict = new HashMap<String, Integer>();
    private Map<String, Integer> eduDict = new HashMap<String, Integer>();
    private Map<String, Integer> lawpolDict = new HashMap<String, Integer>();
    
    //Words positions.
    private List<Integer> ecoWordsIndices = new ArrayList<Integer>();
    private List<Integer> eduWordsIndices = new ArrayList<Integer>();
    private List<Integer> lawpolWordsIndices = new ArrayList<Integer>();
    
    //Word vector.
    private Map<String, Integer> wordVector = new HashMap<String, Integer>();
    
    //Scores
    private List<Integer> scores = new ArrayList<Integer>();
    
    //Output File
    private FileWriter fileWriter;
    private CSVPrinter csvPrinter;
    
    /**
     * Constructor with default category priority
     * @param inFile
     * @param ecoDictFile
     * @param eduDictFile
     * @param lawpolDictFile 
     */
    public RuleBasedClassifier(String inFile, String ecoDictFile,
            String eduDictFile, String lawpolDictFile) throws FileNotFoundException, IOException {
        this.inFile = new File(inFile);
        this.ecoDictFile = new File(ecoDictFile);
        this.eduDictFile = new File(eduDictFile);
        this.lawpolDictFile = new File(lawpolDictFile);
        
        // load files content to memory
        CSVParser ecoDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.ecoDictFile));
        this.ecoDict.putAll(ecoDictParser.getHeaderMap());
        CSVParser eduDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.eduDictFile));
        this.eduDict.putAll(eduDictParser.getHeaderMap());
        CSVParser lawpolDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.lawpolDictFile));
        this.lawpolDict.putAll(lawpolDictParser.getHeaderMap());
        CSVParser wordVectorParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.inFile));
        this.wordVector.putAll(wordVectorParser.getHeaderMap());
        
        // initialize ecoWordsIndices, eduWordsIndices, lawpolWordsIndices
        this.setWordsIndicesLists(this.wordVector);
        
        // initialize scores
        this.scores.add(this.ecoPriority, Integer.valueOf(0));
        this.scores.add(this.eduPriority, Integer.valueOf(0));
        this.scores.add(this.lawpolPriority, Integer.valueOf(0));
        
        // create file output
        this.fileWriter = new FileWriter("./data/klas_dict.csv");
        this.csvPrinter = new CSVPrinter(this.fileWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
        this.wordVector.put("kelas", Integer.valueOf(99999));
        this.csvPrinter.printRecord(this.wordVector.keySet());
    }
    
    /**
     * Constructor with customized category priority
     * @param inFile
     * @param ecoDictFile
     * @param eduDictFile
     * @param lawpolDictFile
     * @param ecoPriority
     * @param eduPriority
     * @param lawpolPriority 
     */
    public RuleBasedClassifier(String inFile, String ecoDictFile,
            String eduDictFile, String lawpolDictFile, int ecoPriority,
            int eduPriority, int lawpolPriority) throws FileNotFoundException, IOException {
        this.inFile = new File(inFile);
        this.ecoDictFile = new File(ecoDictFile);
        this.eduDictFile = new File(eduDictFile);
        this.lawpolDictFile = new File(lawpolDictFile);
        this.setEcoPriority(ecoPriority);
        this.setEduPriority(eduPriority);
        this.setLawpolPriority(lawpolPriority);
        
        // load files content to memory
//        CSVParser ecoDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.ecoDictFile));
        this.ecoDict = CSVParser.parse(this.ecoDictFile, null, CSVFormat.DEFAULT).getHeaderMap();
//        CSVParser eduDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.eduDictFile));
        this.eduDict = CSVParser.parse(this.eduDictFile, null, CSVFormat.DEFAULT).getHeaderMap();
//        CSVParser lawpolDictParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.lawpolDictFile));
        this.lawpolDict = CSVParser.parse(this.lawpolDictFile, null, CSVFormat.DEFAULT).getHeaderMap();
//        CSVParser wordVectorParser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(this.inFile));
        this.wordVector = CSVParser.parse(this.inFile, null, CSVFormat.DEFAULT).getHeaderMap();
        
        // initialize ecoWordsIndices, eduWordsIndices, lawpolWordsIndices
//        this.setWordsIndicesLists(this.wordVector);
        
        // initialize scores
//        this.scores.add(this.ecoPriority, Integer.valueOf(0));
//        this.scores.add(this.eduPriority, Integer.valueOf(0));
//        this.scores.add(this.lawpolPriority, Integer.valueOf(0));
        
        // create file output
        this.fileWriter = new FileWriter("./data/klas_dict.csv");
        this.csvPrinter = new CSVPrinter(this.fileWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
        this.wordVector.put("kelas", Integer.valueOf(99999));
        this.csvPrinter.printRecord(this.wordVector.keySet());
    }
    
    public void setWordsIndicesLists(Map<String, Integer> inMap) {
        for(Map.Entry<String, Integer> entry: inMap.entrySet()) {
            if (this.ecoDict.containsKey(entry.getKey())) {
                this.ecoWordsIndices.add(entry.getValue());
            } else if (this.eduDict.containsKey(entry.getKey())) {
                this.eduWordsIndices.add(entry.getValue());
            } else if (this.lawpolDict.containsKey(entry.getKey())) {
                this.lawpolWordsIndices.add(entry.getValue());
            }
        }
    }
    
    // TODO loop for each tweet, count scores, write the result to output file
    // use priority if there are more than one high score
    // label tweet as "other" if scores [0,0,0]
    
    public void labelling() throws FileNotFoundException, IOException {
        CSVParser tweets = CSVFormat.DEFAULT.withSkipHeaderRecord().parse(new FileReader(new File("./data/preprocessor-test-woheader.csv")));
        
        for(CSVRecord tweet : tweets) {
            int tmpEcoScore = 0;
            int tmpEduScore = 0;
            int tmpLawpolScore = 0;
            for(Integer ecoIdx : this.ecoWordsIndices) {
                tmpEcoScore += Integer.parseInt(tweet.get(ecoIdx.intValue()));
            }
            for(Integer eduIdx : this.eduWordsIndices) {
                tmpEduScore += Integer.parseInt(tweet.get(eduIdx.intValue()));
            }
            for(Integer lawpolIdx : this.lawpolWordsIndices) {
                tmpLawpolScore += Integer.parseInt(tweet.get(lawpolIdx.intValue()));
            }
            this.scores.set(this.ecoPriority, Integer.valueOf(tmpEcoScore));
            this.scores.set(this.eduPriority, Integer.valueOf(tmpEduScore));
            this.scores.set(this.lawpolPriority, Integer.valueOf(tmpLawpolScore));
            
            Map<String, String> tmpMap = tweet.toMap();
            
            // TODO classify based on scores and print it to output file
            if (Collections.frequency(this.scores, Integer.valueOf(0)) == 3) {
                // TODO label the tweet as "Other"
                System.out.println("Other");
                tmpMap.put("kelas", "Other");
            } else {
                // TODO depend on getMaxScoreIndex
                int maxScoreCategory = getMaxScoreIndex((ArrayList<Integer>) this.scores);
                if (maxScoreCategory == this.ecoPriority) {
                    tmpMap.put("kelas", "Ekonomi");
                    System.out.println("Ekonomi");
                } else if (maxScoreCategory == this.eduPriority) {
                    tmpMap.put("kelas", "Pendidikan");
                    System.out.println("Pendidikan");
                } else {
                    tmpMap.put("kelas", "Hukum & Politik");
                    System.out.println("Hukum-Politik");
                }
                
            }
            this.csvPrinter.printRecord(tmpMap.values());    
        }
    }
    
    public int getMaxScoreIndex(ArrayList<Integer> arrInt) {
        Integer maxScore = Collections.max(arrInt);
        int maxOccurence = Collections.frequency(arrInt, maxScore);
        if (maxOccurence == 1) {
            return this.scores.indexOf(maxScore);
        } else {
            if (this.scores.get(0).equals(maxScore))
                return 0;
            return 1;
        }
    }
    
    public void setEcoPriority(int i) {
        this.ecoPriority = i;
    }
    
    public void setEduPriority(int i) {
        this.eduPriority = i;
    }
    
    public void setLawpolPriority(int i) {
        this.lawpolPriority = i;
    }
    
    public static void main(String args[]) throws IOException {
        RuleBasedClassifier rbc = new RuleBasedClassifier("./data/preproc.csv",
            "./data/ecodict.csv", "./data/edudict.csv", "./data/lawpoldict.csv");
        rbc.labelling();
    }
}
