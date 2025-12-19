package project.plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.simulator.Actor;

import java.awt.*;

public class Bush implements DynamicDisplayInformationProvider, Actor {

    /*
    This is a berry bush, they get spawned in by saying "berry", it functions simple, it has a set amount of berrys,
    once these berryes are eaten they will start to regrow.
     */

    protected boolean hasBerries;
    protected int berryCount;
    protected int berryCountMax;
    protected int growCount;

    public Bush() {
        this.hasBerries = true;
        this.berryCount = 10; //amount of berrys before it regrows
        this.berryCountMax = 10; // what it gets reset to after regrowing, most cases you can set berryCount = berryCountMax
        this.growCount = 0;
    }

    //DisplayInformation, displays berries if there are any.

    @Override
    public DisplayInformation getInformation() {
        if(this.hasBerries) return new DisplayInformation(Color.RED, "bush-berries");
        return new DisplayInformation(Color.GREEN, "bush");
    }

    public void act(World w) {

        //growing logic
        if(!this.hasBerries) {
            growCount++;
            if(growCount >= 8) { // 8 ticks before it regrows
                berryCount = berryCountMax;
                growCount = 0;
                this.hasBerries = true;
            }
        }
    }

    //function called by animals eating the berries, e.g. a bear
    
   public void eatBerry() {
       if(this.berryCount > 0) {
           this.berryCount--;
           if(this.berryCount == 0) {
               this.hasBerries = false;
           }
       }
   }
}
