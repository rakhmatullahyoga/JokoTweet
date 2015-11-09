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
    private List<tokenFrekuensiNWeight> tokensOther;
    private List<tokenFrekuensiNWeight> hasilPolitik;
    private List<tokenFrekuensiNWeight> hasilPendidikan;
    private List<tokenFrekuensiNWeight> hasilEkonomi;
    private List<tokenFrekuensiNWeight> hasilOther;
    private float totalWordOther = 0f;
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
        tokensOther = new ArrayList<>();
        hasilPolitik = new ArrayList<>();
        hasilPendidikan = new ArrayList<>();
        hasilEkonomi = new ArrayList<>();
        hasilOther = new ArrayList<>();
        
        line = bufferreader.readLine();
        tokenlist = new LinkedList<String>(Arrays.asList(line.split(",")));
        //tokenlist.remove(0);
        tokenlist.remove(tokenlist.size()-1);
        for(String token : tokenlist){
            tokensHukumNPolitik.add(new tokenFrekuensiNWeight(token));
            tokensEkonomi.add(new tokenFrekuensiNWeight(token));
            tokensPendidikan.add(new tokenFrekuensiNWeight(token));
            tokensOther.add(new tokenFrekuensiNWeight(token));
        }
        for(line = bufferreader.readLine();line!=null;line = bufferreader.readLine()){
            List<String> instances = new LinkedList<String>(Arrays.asList(line.split(",")));
            //instances.remove(0);
            String classtype = instances.get(instances.size()-1);
            switch(classtype){
                case "Ekonomi":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensEkonomi.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                case "Pendidikan":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensPendidikan.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                case "Hukum-Politik":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensHukumNPolitik.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
                    }
                    break;
                case "Other":
                    for(int i = 0;i<instances.size()-1;i++){
                        tokensOther.get(i).tokenfrekuensi += Integer.valueOf(instances.get(i));
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
        float tokenOtherFrekuensi = 0f;
        
        for(int i =0;i<tokenlist.size();i++){
            tokenHukumNPolitikFrekuensi = tokensHukumNPolitik.get(i).tokenfrekuensi;
            tokenEkonomiFrekuensi = tokensEkonomi.get(i).tokenfrekuensi;
            tokenPendidikanFrekuensi = tokensPendidikan.get(i).tokenfrekuensi;
            tokenOtherFrekuensi = tokensOther.get(i).tokenfrekuensi;
            if(tokenHukumNPolitikFrekuensi != 0){
                totalWordHukumNPolitik+= tokenHukumNPolitikFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
                tokensOther.get(i).totalDocumentMentioned++;
            }
            if(tokenEkonomiFrekuensi != 0){
                totalWordEkonomi+= tokenEkonomiFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
                tokensOther.get(i).totalDocumentMentioned++;
            }
            if(tokenPendidikanFrekuensi != 0){
                totalWordPendidikan+= tokenPendidikanFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
                tokensOther.get(i).totalDocumentMentioned++;
            }
            if(tokenOtherFrekuensi != 0){
                totalWordOther+= tokenOtherFrekuensi;
                tokensHukumNPolitik.get(i).totalDocumentMentioned++;
                tokensEkonomi.get(i).totalDocumentMentioned++;
                tokensPendidikan.get(i).totalDocumentMentioned++;
                tokensOther.get(i).totalDocumentMentioned++;
            }
        }
        for(int i = 0;i<tokenlist.size();i++){
            tokenHukumNPolitikFrekuensi = tokensHukumNPolitik.get(i).tokenfrekuensi;
            tokenEkonomiFrekuensi = tokensEkonomi.get(i).tokenfrekuensi;
            tokenPendidikanFrekuensi = tokensPendidikan.get(i).tokenfrekuensi;
            tokenOtherFrekuensi = tokensOther.get(i).tokenfrekuensi;
            if(totalWordHukumNPolitik != 0){
                tokensHukumNPolitik.get(i).tokenweight = tokenHukumNPolitikFrekuensi/totalWordHukumNPolitik * (float)Math.log10(4/tokensHukumNPolitik.get(i).totalDocumentMentioned);
            }
            if(totalWordEkonomi != 0){
                tokensEkonomi.get(i).tokenweight = tokenEkonomiFrekuensi/totalWordEkonomi * (float)Math.log10(4/tokensEkonomi.get(i).totalDocumentMentioned);;
            }
            if(totalWordPendidikan != 0){
                tokensPendidikan.get(i).tokenweight = tokenPendidikanFrekuensi/totalWordPendidikan * (float)Math.log10(4/tokensPendidikan.get(i).totalDocumentMentioned);
            }
            if(totalWordOther != 0){
                tokensOther.get(i).tokenweight = tokenOtherFrekuensi/totalWordOther * (float)Math.log10(4/tokensOther.get(i).totalDocumentMentioned);
            }
            /*if(tokensHukumNPolitik.get(i).tokenweight >= tokensEkonomi.get(i).tokenweight &&
                    tokensHukumNPolitik.get(i).tokenweight >= tokensPendidikan.get(i).tokenweight &&
                    tokensHukumNPolitik.get(i).tokenweight >= tokensOther.get(i).tokenweight){
                hasilPolitik.add(tokensHukumNPolitik.get(i));
            }
            if(tokensEkonomi.get(i).tokenweight >= tokensHukumNPolitik.get(i).tokenweight &&
                    tokensEkonomi.get(i).tokenweight >= tokensPendidikan.get(i).tokenweight &&
                    tokensEkonomi.get(i).tokenweight >= tokensOther.get(i).tokenweight){
                hasilEkonomi.add(tokensEkonomi.get(i));
            }
            if(tokensPendidikan.get(i).tokenweight >= tokensHukumNPolitik.get(i).tokenweight &&
                    tokensPendidikan.get(i).tokenweight >= tokensEkonomi.get(i).tokenweight &&
                    tokensPendidikan.get(i).tokenweight >= tokensOther.get(i).tokenweight){
                hasilPendidikan.add(tokensPendidikan.get(i));
            }
            if(tokensOther.get(i).tokenweight >= tokensHukumNPolitik.get(i).tokenweight &&
                    tokensOther.get(i).tokenweight >= tokensEkonomi.get(i).tokenweight &&
                    tokensOther.get(i).tokenweight >= tokensPendidikan.get(i).tokenweight){
                hasilOther.add(tokensOther.get(i));
            }*/
        }
        Print();
        Export("hasil-TFIDF.csv");
    }
    
    void Print(){
        int counter = 0;
        System.out.println("==============Ekonomi===============");
        for(tokenFrekuensiNWeight data : tokensEkonomi){
            counter++;
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
        System.out.println("==============Other===============");
        counter = 0;
        for(tokenFrekuensiNWeight data : tokensOther){
            counter++;
            System.out.println("Instance : "+counter);
            System.out.println("Token : "+data.token);
            System.out.println("Frekuensi : "+data.tokenfrekuensi);
            System.out.println("Weight : "+data.tokenweight+"\n");
        }
        System.out.println("Total Words in Ekonomi : "+totalWordEkonomi);
        System.out.println("Total Words in Pendidikan : "+totalWordPendidikan);
        System.out.println("Total Words in HukumNPolitik : "+totalWordHukumNPolitik);
        System.out.println("Total Words in Other : "+totalWordOther);
        
    }
    void Export(String filepath) throws IOException{
        FileWriter writer = new FileWriter(filepath);
        writer.append("token,kelas,bobot");
        writer.append("\n");
        /*for(int i=0;i<hasilEkonomi.size()-1;i++){
            //HukumNPolitik
            writer.append(hasilEkonomi.get(i).token);
            writer.append(",Ekonomi,"+tokensEkonomi.get(i).tokenweight+"\n");
        }
        for(int i=0;i<hasilPendidikan.size()-1;i++){
            //HukumNPolitik
            writer.append(hasilPendidikan.get(i).token);
            writer.append(",Pendidikan,"+hasilPendidikan.get(i).tokenweight+"\n");
        }
        for(int i=0;i<hasilPolitik.size()-1;i++){
            //HukumNPolitik
            writer.append(hasilPolitik.get(i).token);
            writer.append(",Hukum-Politik,"+hasilPolitik.get(i).tokenweight+"\n");
        }
        for(int i=0;i<hasilOther.size()-1;i++){
            //HukumNPolitik
            writer.append(hasilOther.get(i).token);
            writer.append(",Other,"+hasilOther.get(i).tokenweight+"\n");
        }*/
        for(tokenFrekuensiNWeight token : tokensEkonomi){
            writer.append(token.token);
            writer.append(",Ekonomi,"+token.tokenweight+"\n");
        }
        for(tokenFrekuensiNWeight token : tokensPendidikan){
            writer.append(token.token);
            writer.append(",Pendidikan,"+token.tokenweight+"\n");
        }
        for(tokenFrekuensiNWeight token : tokensHukumNPolitik){
            writer.append(token.token);
            writer.append(",Hukum-Politik,"+token.tokenweight+"\n");
        }
        for(tokenFrekuensiNWeight token : tokensOther){
            writer.append(token.token);
            writer.append(",Other,"+token.tokenweight+"\n");
        }
        writer.flush();
        writer.close();
    }
    
    public static void main(String[] args) throws IOException{
        TFIDF tfidf = new TFIDF();
        tfidf.start("klas_dict.csv");
    }
    
}

