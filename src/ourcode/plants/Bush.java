package ourcode.plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.simulator.Actor;

import java.awt.*;

public class Bush implements DynamicDisplayInformationProvider, Actor {

    protected boolean hasBerries;
    protected int berryCount;
    protected int berryCountMax;
    protected int growCount;

    public Bush() {
        this.hasBerries = true;
        this.berryCount = 10;
        this.berryCountMax = 10;
        this.growCount = 0;
    }


    @Override
    public DisplayInformation getInformation() {
        if(this.hasBerries) return new DisplayInformation(Color.RED, "bush-berries");
        return new DisplayInformation(Color.GREEN, "bush");
    }

    public void act(World w) {
        if(!this.hasBerries) {
            growCount++;
            if(growCount >= 8) {
                berryCount = berryCountMax;
                growCount = 0;
                this.hasBerries = true;
            }
        }
    }
    
   public void eatBerry() {
       if(this.berryCount > 0) {
           this.berryCount--;
           if(this.berryCount == 0) {
               this.hasBerries = false;
           }
       }
   }
}
