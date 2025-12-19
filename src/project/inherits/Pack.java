package project.inherits;

import itumulator.world.World;
import project.animals.Wolf;

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

        // if id allready exists add it to the pack list
        if (packs.containsKey(id)) {
            packs.get(id).add(w);

        } else { // else make a new pack and add it to the set
            ArrayList<Wolf> list = new ArrayList<>();
            list.add(w);
            packs.put(id, list);
        }
    }

    public void removeFromPack(Wolf w) {
        if(packs.get(w.getPackID()).contains(w)) {
            packs.get(w.getPackID()).remove(w);
        } else {
            System.out.println("Pack is trying to remove a wolf which is not in the list");
        }
    }


    public ArrayList<Wolf> getPack(Wolf w) {
        return packs.get(w.getPackID());
    }

    // this ensures only one "Packs" is created at a time
    // when called as a static(it can be called upon wherever), it checks on whether this class already exists
    // if it dosnt it makes one, if it does it just returns the class
    public static Pack getInstance() {
        if (instance == null) {
            instance = new Pack();
        }
        return instance;
    }


}
