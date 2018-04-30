package main;


import main.Transaction;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.security.Security;
/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public ArrayList<Block> blockchain = new ArrayList<Block>();
    public int difficulty = 4;


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

    public float getBalance() {
    	float total = 0;
		for (int i =0; i < blockchain.size(); i++) {
			Block currentBlock = blockchain.get(i);
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if (currentTransaction.reciepient == publicKey && currentTransaction.value > 0) {
					total += currentTransaction.value;
				}
				if (currentTransaction.sender == publicKey && currentTransaction.value > 0) {
					total -= currentTransaction.value;
				}
			}
		}
		return total;
    }
    public Transaction sendFunds(PublicKey _recipient,float value ) {
        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value,"");
        newTransaction.generateSignature(privateKey);
        return newTransaction;
    }


    public Transaction sendPet(PublicKey _recipient,String pethash ) {
        if(getowner(pethash)!=this.publicKey) {
            System.out.println("#Not pet owner to send transaction. Transaction Discarded.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient ,0, pethash);
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }

	public PublicKey getowner(String pethash) {
		ArrayList<Block> blockchain = this.blockchain;
		for (int i = blockchain.size()-1; i >= 0; i--) {
			Block currentBlock = blockchain.get(i);
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				if (currentTransaction.pethash == pethash) {
					return currentTransaction.reciepient;
				}
			}
		}
		return null;
	}
	public boolean havePet(){
		ArrayList<String>pets=new ArrayList<>();
		
		for (int i = blockchain.size()-1; i >= 0; i--) {
			Block currentBlock = blockchain.get(i);
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				if (currentTransaction.pethash !=null&&currentTransaction.reciepient==this.publicKey) {
					pets.add(currentTransaction.pethash);
				}
				if (pets.contains(currentTransaction.pethash) &&currentTransaction.sender==this.publicKey) {
					pets.remove(currentTransaction.pethash);
				}
			}
		}
		return pets.size()>0;
	}
	public boolean ifowner(String pethash){
		return (this.publicKey==getowner(pethash));
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
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
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

    public Block newblock(ArrayList<Transaction> transactions){
        Block bk  = new Block( blockchain.get(blockchain.size()-1).hash);
        for(Transaction t:transactions)
            bk.addTransaction(t);
        return bk;
    }
    public int getChainSize(){
    	return blockchain.size();
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
}
