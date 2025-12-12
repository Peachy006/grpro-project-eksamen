package project.structures;

import itumulator.executable.*;
import itumulator.simulator.*;
import itumulator.world.*;
import project.animals.Wolf;
import project.inherits.Pack;

import java.awt.*;
import java.util.*;


public class WolfsDen implements DynamicDisplayInformationProvider, NonBlocking, Actor {
    Random r = new Random();

    int denID;
    Location denLocation;
    int matingChance;

    Pack packs;
    ArrayList<Wolf> pack;
    ArrayList<Wolf> denList;
    ArrayList<Wolf> leavingDen;


    public WolfsDen(Wolf w, Location denLocation) {
        this.matingChance = 10;

        // get wolf pack controller
        this.packs = Pack.getInstance();

        //get's dens wolf pack
        this.pack = packs.getPack(w.getPackID());

        this.denID = w.getPackID();

        this.denList = new ArrayList<>();
        this.leavingDen = new ArrayList<>();
        this.denLocation = denLocation;

        // give every wolf in the pack a den.
        for (Wolf wolf : pack) {
            wolf.receiveDen(this, denLocation);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.BLACK, "hole", true);
    }

    @Override
    public void act(World w) {
        reporduce(w);

        System.out.println("list: " + leavingDen.size());

        // wolfe's who has reproduced leaves the den
        if (w.isDay() && !leavingDen.isEmpty() && w.isTileEmpty(denLocation)) {

            // get a random wolf that is leaving
            int randList = r.nextInt(leavingDen.size());
            Wolf tempWolf = leavingDen.get(randList);

            // remove it form the den
            leavingDen.remove(tempWolf);
            packs.addToPack(tempWolf);
            w.setTile(denLocation, tempWolf);
            tempWolf.isRemoved = false;
        }
    }

    // adds wolf to the list that has the same
    public void addToDen(Wolf wolf) {
        if (wolf.getEnergy() >= 100) {
            denList.add(wolf);
            packs.removeFromPack(wolf);
        }
    }

    // takes 2 wolf's and makes them reproduce
    public void reporduce(World w) {

        // if there are more then 2 wolf's in the den
        if (denList.size() >= 2) {

            // pick 2 random wolfs
            int rand = r.nextInt(denList.size());
            Wolf w1 = denList.get(rand);

            rand = r.nextInt(denList.size());
            Wolf w2 = denList.get(rand);

            // roll for mating chance
            int matingRoll = r.nextInt(matingChance);

            // if mating roll = 0 and both wolf's has the energy mate
            if (matingRoll == 0 && w1.getEnergy() >= 100 && w2.getEnergy() >= 100) {

                // make new baby wolf, set its pack
                Wolf babyWolf = new Wolf(denID);
                babyWolf.receiveDen(this, denLocation);

                // set cooldown
                w1.setCooldown(20);
                w2.setCooldown(20);

                // add them to the leaving list
                leavingDen.add(w1);
                leavingDen.add(w2);
                leavingDen.add(babyWolf);

                // remove wolfs from den
                denList.remove(w1);
                denList.remove(w2);
            }
        }
    }

    public boolean isInsideDen(Wolf w) {
        return denList.contains(w);
    }
}