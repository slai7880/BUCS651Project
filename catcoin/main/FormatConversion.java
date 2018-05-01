package main;



import java.security.Security;
import java.util.*;

import com.google.gson.Gson;

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
}