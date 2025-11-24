package itumulator.simulator.themeOne;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;

public class Rabbit implements Actor {

    int energy = 100;
    boolean isBurrowed = false;
    int totalEnergy = 100;
    int age = 1;
    int dayCount = 0;
    int IntercourseDelayTimer = 0;
    boolean aboutToGoDown = false;
    ArrayList<Location> PriorityMoves = new ArrayList<>();

    private Location burrowLocation = null;



    public Location getBurrowLocation() {
        return burrowLocation;
    }


    @Override
    public void act(World w) {
        Random r = new Random();
        if(!isBurrowed) {
            //aging logic
            dayCount++;
            if(dayCount >= 5) {
                dayCount = 0;
                age++;
                totalEnergy -= 10;
                if(age >= 10) {
                    w.delete(this);
                    return;
                }
            }

            //the rabbit will reproduce if another rabbit is nearby
            Set<Location> neighbours = w.getSurroundingTiles(w.getLocation(this));
            for(Location l : neighbours) {
                if(w.getTile(l) != null && w.getTile(l) instanceof Rabbit) {
                    Rabbit tempRabbit = (Rabbit)w.getTile(l);
                    if(tempRabbit.age >= 5 && this.age >= 5 && tempRabbit.IntercourseDelayTimer <= 1 && this.IntercourseDelayTimer <= 1) {
                        tempRabbit.IntercourseDelayTimer = 5;
                        this.IntercourseDelayTimer = 5;
                        Set<Location> tempNeighbours = w.getEmptySurroundingTiles(w.getLocation(this));
                        if(!tempNeighbours.isEmpty()) {
                            ArrayList<Location> tempNeighboursList = new ArrayList<>(tempNeighbours);
                            w.setTile(tempNeighboursList.get(r.nextInt(tempNeighboursList.size())), new Rabbit());
                        }
                    }
                }
            }
            if(IntercourseDelayTimer > 0) IntercourseDelayTimer--;


            Location currentLocation = w.getLocation(this);

            // Check if there's grass at current location before trying to get it
            if (w.containsNonBlocking(currentLocation)) {
                Object nonBlockingObject = w.getNonBlocking(currentLocation);

                if (nonBlockingObject instanceof Grass) {
                    energy += 30;
                    if(energy > totalEnergy) energy = totalEnergy;
                    w.delete(nonBlockingObject);
                }
            }

            //Digging Burrow logic

            if(this.burrowLocation == null && w.getNonBlocking(w.getLocation(this)) == null) {

                if(r.nextDouble() < 0.15 && !w.containsNonBlocking(currentLocation)) {
                    Burrow burrow = new Burrow();
                    w.setTile(currentLocation, burrow);
                    //Set Rabbit to the Burrow it made
                    this.burrowLocation = w.getLocation(this);
                    burrow.addRabbit(this);
                }
            }

            //Rabbit gets Burrow at night if it doesn't have one
            if(this.burrowLocation == null && w.isNight()){
                //Checks for Burrows around Rabbit
                Set<Location> surroundingBurrows =  w.getSurroundingTiles(w.getLocation(this), w.getSize());
                for(Location l : surroundingBurrows) {
                    Object Tile = w.getTile(l);

                    if(Tile instanceof Burrow) {
                        Burrow nearBurrow = (Burrow)Tile;

                        this.burrowLocation = w.getLocation(nearBurrow);
                        nearBurrow.addRabbit(this);
                    }
                }
            }

            Location nextLocation = null;

            //adding logic for moving rabbits to their burrows
            if(this.burrowLocation != null && w.isNight()) {
                //Get empty surrounding tiles
                Set<Location> emptyNeighbours = w.getEmptySurroundingTiles(currentLocation);

                if(!emptyNeighbours.isEmpty()) {
                    //find the tile closest to the burrow
                    Location bestMove = null;
                    double shortestDistance = Double.MAX_VALUE;

                    for(Location candidate : emptyNeighbours) {
                        // alculate distance to burrow (this is done using euclidance
                        int dx = candidate.getX() - burrowLocation.getX();
                        int dy = candidate.getY() - burrowLocation.getY();
                        double distance = Math.sqrt(dx * dx + dy * dy);

                        if(distance < shortestDistance) {
                            shortestDistance = distance;
                            bestMove = candidate;
                        }
                    }

                    // Add the best move to PriorityMoves if we found one
                    if(bestMove != null) {
                        PriorityMoves.add(bestMove);
                    }
                }
            }

            //move if the rabbit has the energy
            if (energy >= 10) {

                if(!PriorityMoves.isEmpty()) {
                    w.move(this, PriorityMoves.get(0));
                    PriorityMoves.remove(0);
                } else {
                    Set<Location> neighboursto = w.getEmptySurroundingTiles(currentLocation);
                    if (neighboursto.isEmpty()) return;

                    ArrayList<Location> neighboursList = new ArrayList<>(neighboursto);
                    Location newLocation = neighboursList.get(r.nextInt(neighboursList.size()));
                    w.move(this, newLocation);
                }

                energy -= 10;
            } else {
                w.delete(this);
            }

            //go down the rabbithole (tihi)
            if(burrowLocation != null && burrowLocation.equals(w.getLocation(this)) && w.isNight()) {
                isBurrowed = true;
                aboutToGoDown = true;
            }
        }

        if(aboutToGoDown) {
            w.remove(this);
            aboutToGoDown = false;
        }

        if(isBurrowed && w.isDay()) {
            if(burrowLocation != null && w.isTileEmpty(burrowLocation)) {
                w.setTile(burrowLocation, this);
                isBurrowed = false;
            }
        }






    }
}
