package nlp.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import Linda.Klasifikasi;
import preprocessor.Preprocessor;

public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Preprocess Tweet
		try {
            Preprocessor preprocessor = new Preprocessor("./data/stopword.txt", "./data/testing.csv");
            preprocessor.process();

            preprocessor.exportToCsv("./data/testing-preprocessed.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// Klasifikasi
		Klasifikasi klasifikasi = new Klasifikasi();
        System.out.println("start");
        klasifikasi.start("./data/testing-preprocessed.csv","./data/hasil-TFIDF.csv");
        System.out.println("export");
        klasifikasi.Export("./data/testing-classified.csv");

	}

}
