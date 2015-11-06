/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kevin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kevin
 */
public class TFIDF {
    private List<String> tokenlist = new ArrayList<>();
    private List<tokenFrekuensiNWeight> tokensHukumNPolitik;
    private List<tokenFrekuensiNWeight> tokensPendidikan;
    private List<tokenFrekuensiNWeight> tokensEkonomi;
    private float totalWordHukumNPolitik = 0f;
    private float totalWordEkonomi = 0f;
    private float totalWordPendidikan = 0f;
    public void start(String filepath) throws FileNotFoundException, IOException{
        BufferedReader bufferreader = new BufferedReader(new FileReader(filepath));
        int counter = 0;
        String line = null;
        tokensHukumNPolitik = new ArrayList<>();
        tokensPendidikan = new ArrayList<>();
        tokensEkonomi = new ArrayList<>();
        
        line = bufferreader.readLine();
        tokenlist = new LinkedList<String>(Arrays.asList(line.split(",")));
        tokenlist.remove(0);
        tokenlist.remove(tokenlist.size()-1);
        for(String token : tokenlist){
            tokensHukumNPolitik.add(new tokenFrekuensiNWeight(token));
            tokensEkonomi.add(new tokenFrekuensiNWeight(token));
            tokensPendidikan.add(new tokenFrekuensiNWeight(token));
        }
        for(line = bufferreader.readLine();line!=null;line = bufferreader.readLine()){
            List<String> instances = new LinkedList<String>(Arrays.asList(line.split(",")));
            instances.remove(0);
            String classtype = instances.get(instances.size()-1);
            switch(classtype){
                case "k1":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensEkonomi.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                case "k2":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensPendidikan.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                case "k3":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensHukumNPolitik.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                default:
                    System.err.println("Class unidentified!");
                    break;
            }
        }
        float tokenHukumNPolitikFrekuensi = 0f;
        float tokenEkonomiFrekuensi = 0f;
        float tokenPendidikanFrekuensi = 0f;
        
        for(int i =0;i<tokenlist.size();i++){
            tokenHukumNPolitikFrekuensi = tokensHukumNPolitik.get(i).tokenfrekuensi;
            tokenEkonomiFrekuensi = tokensEkonomi.get(i).tokenfrekuensi;
            tokenPendidikanFrekuensi = tokensPendidikan.get(i).tokenfrekuensi;
            if(tokenHukumNPolitikFrekuensi != 0){
                totalWordHukumNPolitik+= tokenHukumNPolitikFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
            }
            if(tokenEkonomiFrekuensi != 0){
                totalWordEkonomi+= tokenEkonomiFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
            }
            if(tokenPendidikanFrekuensi != 0){
                totalWordPendidikan+= tokenPendidikanFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
            }
        }
        for(int i = 0;i<tokenlist.size();i++){
            tokenHukumNPolitikFrekuensi = tokensHukumNPolitik.get(i).tokenfrekuensi;
            tokenEkonomiFrekuensi = tokensEkonomi.get(i).tokenfrekuensi;
            tokenPendidikanFrekuensi = tokensPendidikan.get(i).tokenfrekuensi;
            tokensHukumNPolitik.get(i).tokenweight = tokenHukumNPolitikFrekuensi/totalWordHukumNPolitik * (float)Math.log10(3/tokensHukumNPolitik.get(i).totalDocumentMentioned);
            tokensEkonomi.get(i).tokenweight = tokenEkonomiFrekuensi/totalWordEkonomi * (float)Math.log10(3/tokensEkonomi.get(i).totalDocumentMentioned);;
            tokensPendidikan.get(i).tokenweight = tokenPendidikanFrekuensi/totalWordPendidikan * (float)Math.log10(3/tokensPendidikan.get(i).totalDocumentMentioned);
        }
        Print();
        Export("hasil-TFIDF.txt");
    }
    
    void Print(){
        int counter = 0;
        System.out.println("==============Ekonomi===============");
        for(tokenFrekuensiNWeight data : tokensEkonomi){
            System.out.println("Instance : "+counter);
            System.out.println("Token : "+data.token);
            System.out.println("Frekuensi : "+data.tokenfrekuensi);
            System.out.println("Weight : "+data.tokenweight+"\n");
        }
        System.out.println("==============Pendidikan===============");
        counter = 0;
        for(tokenFrekuensiNWeight data : tokensPendidikan){
            counter++;
            System.out.println("Instance : "+counter);
            System.out.println("Token : "+data.token);
            System.out.println("Frekuensi : "+data.tokenfrekuensi);
            System.out.println("Weight : "+data.tokenweight+"\n");
        }
        System.out.println("==============HukumNPolitik===============");
        counter = 0;
        for(tokenFrekuensiNWeight data : tokensHukumNPolitik){
            counter++;
            System.out.println("Instance : "+counter);
            System.out.println("Token : "+data.token);
            System.out.println("Frekuensi : "+data.tokenfrekuensi);
            System.out.println("Weight : "+data.tokenweight+"\n");
        }
        System.out.println("Total Words in Ekonomi : "+totalWordEkonomi);
        System.out.println("Total Words in Pendidikan : "+totalWordPendidikan);
        System.out.println("Total Words in HukumNPolitik : "+totalWordHukumNPolitik);
        
    }
    void Export(String filepath) throws IOException{
        FileWriter writer = new FileWriter(filepath);
        writer.append("token,kelas,bobot");
        writer.append("\n");
        for(int i=0;i<tokenlist.size()-1;i++){
            //HukumNPolitik
            writer.append(tokenlist.get(i));
            writer.append(",k1,"+tokensEkonomi.get(i).tokenweight+"\n");
            writer.append(tokenlist.get(i));
            writer.append(",k2,"+tokensPendidikan.get(i).tokenweight+"\n");
            writer.append(tokenlist.get(i));
            writer.append(",k3,"+tokensHukumNPolitik.get(i).tokenweight+"\n");
        }
        writer.flush();
        writer.close();
    }
    
    public static void main(String[] args) throws IOException{
        TFIDF tfidf = new TFIDF();
        tfidf.start("klas_dict.txt");
    }
    
}

