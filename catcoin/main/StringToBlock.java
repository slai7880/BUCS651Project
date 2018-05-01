package main;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import  java.security.GeneralSecurityException;
public class StringToBlock {
    public static Block abc(String string) throws GeneralSecurityException {
        String[] strs= string.split("fuckfuckfuck");



        PublicKey sender = loadPublicKey(strs[3]);

        PublicKey reciepient= loadPublicKey(strs[4]);
        byte[] signature;

        try{
            signature = strs[7].getBytes("ISO-8859-1");
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException();
        }
        float value  = Float.parseFloat(strs[5]);
        Transaction transaction = new Transaction(sender, reciepient,value, strs[6]);
        transaction.signature= signature;
        Block block = new Block(strs[1]);
        block.hash= strs[0];
        block.merkleRoot= strs[2];
        block.transactions=transaction;
        block.timeStamp= Long.parseLong(strs[8]);
        block.nonce = Integer.parseInt(strs[9]);
        return  block;

    }
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode((stored));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("ECDSA","BC");
        return fact.generatePublic(spec);
    }

    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("ECDSA","BC");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString((spec.getEncoded()));
    }
}
