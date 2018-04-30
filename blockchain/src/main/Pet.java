package main;

import java.util.Random;

public class Pet {
    private String hash;
    private int hp;
    Random random=new Random();
    private int hpmax=10;
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void changeHp() {
        this.hp=random.nextInt(hpmax);
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public Pet(){
        this.hp=random.nextInt(hpmax);
    }
}
