package main;


import main.Transaction;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public ArrayList<Block> blockchain = new ArrayList<Block>();
    public int difficulty = 4;
    public HashMap<String, String>petList=new HashMap<>();
    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public float getBalance(PublicKey publicKey) {
    	float total = 0;
		for (int i =0; i < blockchain.size(); i++) {
			Block currentBlock = blockchain.get(i);
				Transaction currentTransaction = currentBlock.transactions;
				if (currentTransaction.recipient.equals(FormatConversion.fromPublicKey(publicKey)) && currentTransaction.value >= 0) {
					total += currentTransaction.value;
				}
				if (currentTransaction.sender.equals(FormatConversion.fromPublicKey(publicKey)) && currentTransaction.value >= 0) {
                    total -= currentTransaction.value;
				}
		}
		return total;
    }
    public Transaction sendFunds(PublicKey _recipient,float value ) {
        if(getBalance(this.publicKey) < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value,"");
        newTransaction.generateSignature(privateKey);
        return newTransaction;
    }


    public Transaction sendPet(PublicKey _recipient,String pethash ) throws  GeneralSecurityException{
        if(getowner(pethash)!=this.publicKey) {
            System.out.println("#Not pet owner to send transaction. Transaction Discarded.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient ,0, pethash);
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }

	public PublicKey getowner(String pethash) throws GeneralSecurityException {
		ArrayList<Block> blockchain = this.blockchain;
		for (int i = blockchain.size()-1; i >= 0; i--) {
			Block currentBlock = blockchain.get(i);
				Transaction currentTransaction = currentBlock.transactions;
				if (currentTransaction.pethash == pethash) {
					return  FormatConversion.toPublicKey(currentTransaction.recipient);
			}
		}
		return null;
	}
	public boolean ifowner(String pethash) throws GeneralSecurityException {
		return (this.publicKey.equals(getowner(pethash)));
	}
    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
//            TransactionOutput tempOutput;
                Transaction currentTransaction = currentBlock.transactions;
                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction is Invalid");
                    return false;
//              if( currentTransaction.reciepient != currentTransaction.reciepient) {
//              System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
//              return false;
//          }
//                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
//                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
//                    return false;
//                }

//                for(TransactionInput input: currentTransaction.inputs) {
//                    tempOutput = tempUTXOs.get(input.transactionOutputId);
//
//                    if(tempOutput == null) {
//                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
//                        return false;
//                    }
//
//                    if(input.UTXO.value != tempOutput.value) {
//                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
//                        return false;
//                    }
//
//                    tempUTXOs.remove(input.transactionOutputId);
//                }
//
//                for(TransactionOutput output: currentTransaction.outputs) {
//                    tempUTXOs.put(output.id, output);
//                }
//

//                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
//                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
//                    return false;
//                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode((stored));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("ECDSA");
        return fact.generatePublic(spec);
    }
    
    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
	    KeyFactory fact = KeyFactory.getInstance("ECDSA");
	    X509EncodedKeySpec spec = fact.getKeySpec(publ,
	            X509EncodedKeySpec.class);
	    return Base64.getEncoder().encodeToString((spec.getEncoded()));
	}
	
    public Block newblock(ArrayList<Transaction> transactions){
        Block bk  = new Block( blockchain.get(blockchain.size()-1).hash);
        for(Transaction t:transactions)
            bk.addTransaction(t);
        return bk;
    }
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
    public void replacechain(ArrayList<Block> chain){
        blockchain = chain;
    }
    public void longestchain(ArrayList<Block> chain){
        Integer newlength = chain.size();
        if (newlength> blockchain.size()){
            replacechain(chain);
        }
    }
    public void savePet(String pethash,String petcount){
    	petList.put(pethash, petcount);
    }
    public String getPets() throws GeneralSecurityException{
    	ArrayList<String> pets=new ArrayList<>();
    	for (int i=0;i< blockchain.size();i++) {
			Block currentBlock = blockchain.get(i);
				Transaction currentTransaction = currentBlock.transactions;
				if (currentTransaction.pethash !=null && FormatConversion.toPublicKey(currentTransaction.recipient).equals(publicKey) ) {
					pets.add(currentTransaction.pethash);
				
			}
				if (currentTransaction.pethash !=null && FormatConversion.toPublicKey(currentTransaction.sender).equals(publicKey)) {
					pets.remove(currentTransaction.pethash);
				
			}

		}
		String result = "";
		if (pets.size() == 0) {
			result = "0";
		} else {
			for (Map.Entry<String, String> entry : petList.entrySet()) {
				if (pets.contains(entry.getKey())) {
					result += entry.getValue();
				}
			}
		}
		return result;
    }
    public void addChain(ArrayList<Block> partchain){
        for(Block block:partchain){
            addBlock(block);
        }
    }
}
