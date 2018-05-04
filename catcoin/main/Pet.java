package main;

import utils.StringUtil;

import java.util.Date;
import java.util.Random;

public class Pet {
    public static Integer count = 0;
    public String hash;
    public int hp;

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
        this.hash = StringUtil.applySha256(Long.toString(new Date().getTime()) +
                        Integer.toString(count) +Integer.toString(hp)
        );
        count++;
    }
}
