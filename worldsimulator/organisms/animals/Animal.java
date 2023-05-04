package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.geom.Point2D;

public abstract class Animal extends Organism {
    protected Point2D previousPosition;
    public Animal(Point2D position, int strength, int initiative, Game game) {
        super(position, strength, initiative, game);
        this.previousPosition = position;
    }
    public Animal(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }

    public void action() {
        move();
    }
    public void collision() {
        Organism beingInConflict = collisionWith();
        if(beingInConflict == null) return;
        if(isTheSameSpecie(beingInConflict)) {
            this.position = this.previousPosition;
            reproduce((Animal) beingInConflict);
            return;
        }
        if(beingInConflict.getStrength() <= this.strength)
            beingInConflict.collisionAnswer(this);
        else {
            if(handleDeath(beingInConflict))
                game.addMessage(this.getOrganismType() + " went to position " + this.getPositionNotation() + " so is kill by " + beingInConflict.getOrganismType() + "\n");
        }
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        if(handleDeath(beingInConflict))
            game.addMessage(this.getOrganismType() + " from position " + this.getPositionNotation() + " is kill by " + beingInConflict.getOrganismType() + " which change his position with the aim of kill" + "\n");
    }
    public void comeBack() {
        this.position = previousPosition;
    }
    public void runAway() {
        action();
        collision();
    }
    public abstract Organism createNewOne(Point2D positionForNew);
    public abstract boolean isTheSameSpecie(Organism beingInConflict);

    protected void reproduce(Animal animalReproducingWith) {
        Point2D positionForNew = randomNeighbor(true, Utils.Const.NEIGHBOUR_SHIFT);
        if(positionForNew.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) {
            positionForNew = animalReproducingWith.randomNeighbor(true, Utils.Const.NEIGHBOUR_SHIFT);
            if(positionForNew.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) {
                game.addWarning("Can't reproduce " + this.getOrganismType() + " on position " + this.getPositionNotation() + "\n");
                return;
            }
        }
        game.addMessage(this.getOrganismType() + " from position " + this.getPositionNotation() + " change position and met " + animalReproducingWith.getOrganismType() + " on position" + animalReproducingWith.getPositionNotation() + " so they reproduce" + "\n");
        game.organisms.add(this.createNewOne(positionForNew));
    }
    protected void move() {
        Point2D newPosition = positionMove();
        if(newPosition.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) return;
        swapPosition(newPosition);
        game.addMessage(this.getOrganismType() + " move on a new position " + this.getPositionNotation() + "\n");
    }
    protected boolean handleDeath(Organism beingInConflict) {
        this.kill();
        return true;
    }
    protected Point2D positionMove(){
        return randomNeighbor(false, this.getStep());
    }
    protected void swapPosition(Point2D newPosition) {
        this.previousPosition = this.position;
        this.position = newPosition;
    }
    protected abstract int getStep();
}
