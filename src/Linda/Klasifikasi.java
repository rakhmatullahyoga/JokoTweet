/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Linda;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kevin
 */
public class Klasifikasi {
    private List<String> tokenlist = new ArrayList<>();
    public void start(String testdatapath,String hasiltfidf) throws FileNotFoundException, IOException{
        BufferedReader bufferreader = new BufferedReader(new FileReader(testdatapath));
        String line = null;
        line = bufferreader.readLine();
        tokenlist = new LinkedList<String>(Arrays.asList(line.split(",")));
        tokenlist.remove(tokenlist.size()-1);
        System.out.println(tokenlist.toString());
    }
    public static void main(String[] args) throws IOException{
        Klasifikasi klasifikasi = new Klasifikasi();
        klasifikasi.start("./data/test_data.csv","hasil-TFIDF.csv");
    }
}
