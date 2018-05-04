package main;



import java.security.*;
import java.util.*;
import java.security.spec.X509EncodedKeySpec;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class FormatConversion {
    public static void main(String[] args) {
        List<Block> blocks = new ArrayList<Block>();
        for (int i = 0; i < 1; i++) {
            Block newBlock = new Block("" + i);
            blocks.add(newBlock);
            System.out.println(newBlock.timeStamp);
        }
        String json = toJSON(blocks.get(0));
        Block block = fromJSON(json);
        System.out.println(block.timeStamp);
    }
    
    public static String toJSON(List<Block> blocks) {
        Gson g = new Gson();
        return g.toJson(blocks);
    }
    
    public static String toJSON(Block block) {
        Gson g = new Gson();
        return g.toJson(block);
    }
    
    public static Block fromJSON(String jsonInString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Block.class);
    }


    public static ArrayList<Block> fromJSONList(String jsonInString){
        Gson gson = new Gson();
        Block[] blocks = gson.fromJson(jsonInString, Block[].class);

        return new ArrayList<Block>(Arrays.asList(blocks));
    }

    
    // Converts a PublicKey object to String.
    public static String fromPublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    
    // Converts a String to PublicKey.
    public static PublicKey toPublicKey(String publicKeyString) throws GeneralSecurityException {
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA"); // must match the first parameter used in KeyPairGenerator.getInstance()
        return keyFactory.generatePublic(keySpec);
    }
    
    // Converts a PrivateKey object to String.
    public static String fromPublicKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    // Converts a String to PublicKey.
    public static PrivateKey toPrivateKey(String privateKeyString) throws GeneralSecurityException {
        byte[] privateBytes = Base64.getDecoder().decode(privateKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA"); // must match the first parameter used in KeyPairGenerator.getInstance()
        return keyFactory.generatePrivate(keySpec);
    }
}