package project.inherits;

import itumulator.simulator.*;

import itumulator.world.World;
import project.animals.Wolf;

import java.awt.event.ActionListener;
import java.util.*;


public class Pack {
    World w;
    private static Pack instance;

    HashMap<Integer, ArrayList<Wolf>> packs;

    public Pack() {
        this.packs = new HashMap<>();
    }

    // adds a wolf to the pack and sets its hasPack to true
    public void addToPack(Wolf w) {
        int id = w.getPackID();

        if (!w.isRemoved) {
            // if id allready exists add it to the pack list
            if (packs.containsKey(id)) {
                packs.get(id).add(w);

            } else { // else make a new pack and add it to the set
                ArrayList<Wolf> list = new ArrayList<>();
                list.add(w);
                packs.put(id, list);
            }
        }
    }

    public void removeFromPack(Wolf w) {
        packs.get(w.getPackID()).remove(w);
    }


    public ArrayList<Wolf> getPack(int ID) {
        return packs.get(ID);
    }

    // this method does so only one Pack class is instanciatede at a time
    // when called as a static(it can be called upon wherever), it checks on weather this class exists
    // if it dosnt it makes one, if it does it just returns the class
    public static Pack getInstance() {
        if (instance == null) {
            instance = new Pack();
        }
        return instance;
    }

}