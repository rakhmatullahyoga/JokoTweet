/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Linda;

import Kevin.tokenFrekuensiNWeight;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    private List<tokenFrekuensiNWeight> datasTFIDFEkonomi = new ArrayList<tokenFrekuensiNWeight>();
    private List<tokenFrekuensiNWeight> datasTFIDFPendidikan= new ArrayList<tokenFrekuensiNWeight>();
    private List<tokenFrekuensiNWeight> datasTFIDFHukum= new ArrayList<tokenFrekuensiNWeight>();
    private List<tokenFrekuensiNWeight> datasTFIDFOther= new ArrayList<tokenFrekuensiNWeight>();
    private LinkedList<LinkedList<String>> instances = new LinkedList<LinkedList<String>>();;
    public void start(String testdatapath,String hasiltfidf) throws FileNotFoundException, IOException{
        readTFIDF(hasiltfidf);
        BufferedReader bufferreader = new BufferedReader(new FileReader(testdatapath));
        String line = null;
        line = bufferreader.readLine();
        tokenlist = new LinkedList<String>(Arrays.asList(line.split(",")));
        tokenlist.add("Ekonomi");
        tokenlist.add("Pendidikan");
        tokenlist.add("Hukum-Politik");
        tokenlist.add("Other");
        for(line = bufferreader.readLine();line!=null;line = bufferreader.readLine()){
            instances.add(new LinkedList<String>(Arrays.asList(line.split(","))));
        }
        for(int i =0;i<instances.size();i++){
            int tokenid = 0;
            float bobotEkonomi = 0f;
            float bobotPendidikan = 0f;
            float bobotHukum = 0f;
            float bobotOther = 0f;
            for (String tokenfrekuensi : instances.get(i)) {
                //System.out.print("Instance "+i +": "+ tokenlist.get(tokenid)+" : "+tokenfrekuensi);
                for(tokenFrekuensiNWeight dataEkonomi : datasTFIDFEkonomi){
                    if(dataEkonomi.token.equalsIgnoreCase(tokenlist.get(tokenid))){
                        bobotEkonomi += Float.valueOf(tokenfrekuensi)*dataEkonomi.tokenweight;
                    }
                }
                for(tokenFrekuensiNWeight dataPendidikan : datasTFIDFPendidikan){
                    if(dataPendidikan.token.equalsIgnoreCase(tokenlist.get(tokenid))){
                        bobotPendidikan += Float.valueOf(tokenfrekuensi)*dataPendidikan.tokenweight;
                    }
                }
                for(tokenFrekuensiNWeight dataHukum : datasTFIDFHukum){
                    if(dataHukum.token.equalsIgnoreCase(tokenlist.get(tokenid))){
                        bobotHukum += Float.valueOf(tokenfrekuensi)*dataHukum.tokenweight;
                    }
                }
                for(tokenFrekuensiNWeight dataOther : datasTFIDFOther){
                    if(dataOther.token.equalsIgnoreCase(tokenlist.get(tokenid))){
                        bobotOther += Float.valueOf(tokenfrekuensi)*dataOther.tokenweight;
                    }
                }
                tokenid++;
                
            }
            instances.get(i).add(""+bobotEkonomi);
            instances.get(i).add(""+bobotPendidikan);
            instances.get(i).add(""+bobotHukum);
            instances.get(i).add(""+bobotOther);
            float maxweight = Math.max(Math.max(bobotEkonomi,bobotPendidikan),Math.max(bobotHukum, bobotOther));
            if(maxweight == bobotEkonomi){
                instances.get(i).add("Ekonomi");
            }
            else if(maxweight == bobotPendidikan){
                instances.get(i).add("Pendidikan");
            }
            else if(maxweight == bobotHukum){
                instances.get(i).add("Hukum-Politik");
            }
            else if(maxweight == bobotOther){
                instances.get(i).add("Other");
            }
            
        }
        
        
            
    }
    public static void main(String[] args) throws IOException{
        Klasifikasi klasifikasi = new Klasifikasi();
        klasifikasi.start("testing.csv","dummytfidf.csv");
        klasifikasi.Export("hasil-klasifikasi.csv");
    }
    
    public void readTFIDF(String pathhasiltfidf) throws FileNotFoundException, IOException{
        BufferedReader bufferreader = new BufferedReader(new FileReader(pathhasiltfidf));
        String line = bufferreader.readLine();
        for(line = bufferreader.readLine();line!=null;line = bufferreader.readLine()){
            List<String> data = new LinkedList<String>(Arrays.asList(line.split(",")));
            tokenFrekuensiNWeight tfidfdata = new tokenFrekuensiNWeight(data.get(0),data.get(1),Float.valueOf(data.get(2)));
            if(data.get(1).equalsIgnoreCase("Ekonomi")){
                datasTFIDFEkonomi.add(tfidfdata);
            }
            else if(data.get(1).equalsIgnoreCase("Pendidikan")){
                datasTFIDFPendidikan.add(tfidfdata);
            }
            else if(data.get(1).equalsIgnoreCase("Hukum-Politik")){
                datasTFIDFHukum.add(tfidfdata);
            }
            else if(data.get(1).equalsIgnoreCase("Other")){
                datasTFIDFOther.add(tfidfdata);
            }
        }
        /*System.out.println("==========Ekonomi===========");
        for(int i =0;i<datasTFIDFEkonomi.size();i++){
            System.out.println("Data "+i+" : "+datasTFIDFEkonomi.get(i).token+","+datasTFIDFEkonomi.get(i).estimatedclass+","+datasTFIDFEkonomi.get(i).tokenweight);
        }
        System.out.println("==========Pendidikan===========");
        for(int i =0;i<datasTFIDFEkonomi.size();i++){
            System.out.println("Data "+i+" : "+datasTFIDFPendidikan.get(i).token+","+datasTFIDFPendidikan.get(i).estimatedclass+","+datasTFIDFPendidikan.get(i).tokenweight);
        }
        System.out.println("==========Hukum===========");
        for(int i =0;i<datasTFIDFHukum.size();i++){
            System.out.println("Data "+i+" : "+datasTFIDFHukum.get(i).token+","+datasTFIDFHukum.get(i).estimatedclass+","+datasTFIDFHukum.get(i).tokenweight);
        }
        System.out.println("==========Other===========");
        for(int i =0;i<datasTFIDFOther.size();i++){
            System.out.println("Data "+i+" : "+datasTFIDFOther.get(i).token+","+datasTFIDFOther.get(i).estimatedclass+","+datasTFIDFOther.get(i).tokenweight);
        }*/
        
    }
    public void Export(String fileouput) throws IOException{
        FileWriter writer = new FileWriter(fileouput);
        for(int i=0;i<tokenlist.size()-1;i++){
            writer.append(tokenlist.get(i)+",");
        }
        writer.append(tokenlist.get(tokenlist.size()-1));
        writer.append(",Hasil\n");
        for(int i =0;i < instances.size();i++){
            for(int j = 0;j<instances.get(i).size()-1;j++){
                writer.append(instances.get(i).get(j)+",");
            }
            writer.append(instances.get(i).get(instances.get(i).size()-1)+"\n");
        }
        writer.flush();
        writer.close();
    }
}
