/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kevin;

/**
 *
 * @author Kevin
 */
public class tokenFrekuensiNWeight{
    public String token;
    public float tokenfrekuensi = 0f;
    public float tokenweight = 0f;
    public float totalDocumentMentioned = 0f;
    public String estimatedclass = null;

    public tokenFrekuensiNWeight(String token){
        this.token = token;
    }
    
    public tokenFrekuensiNWeight(String token,String estString,float weight){
        this.token = token;
        this.estimatedclass = estString;
        this.tokenweight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        tokenFrekuensiNWeight object = (tokenFrekuensiNWeight) obj;
        if(object.token.equalsIgnoreCase(this.token)){
            return true;
        }
        else{
            return false;
        }
    }
}
