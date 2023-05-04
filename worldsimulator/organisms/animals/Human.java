package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Human extends Animal {
    public Human(Point2D position, Game game) {
        super(position, Utils.Const.HUMAN_STRENGTH, Utils.Const.HUMAN_INITIATIVE, game);
    }
    public Human(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void action() {
        Point2D newPos = switch (game.getInput()) {
            case "UP" -> new Point2D.Double(this.position.getX() + 0, this.position.getY() - Utils.Const.HUMAN_STEP);
            case "DOWN" -> new Point2D.Double(this.position.getX() + 0, this.position.getY() + Utils.Const.HUMAN_STEP);
            case "RIGHT" -> new Point2D.Double(this.position.getX() + Utils.Const.HUMAN_STEP, this.position.getY() + 0);
            case "LEFT" -> new Point2D.Double(this.position.getX() - Utils.Const.HUMAN_STEP, this.position.getY() + 0);
            default -> null;
        };
        if(newPos == null) return;
        if(this.isOnBoard(newPos))
            swapPosition(newPos);
    }
    @Override
    public void collision() {
        Organism beingInConflict = collisionWith();
        if(beingInConflict == null) return;
        Animal collisionAnimal = (Animal) beingInConflict;
        if(!game.isAlzurShieldActive() || collisionAnimal == null) {
            if(beingInConflict.getStrength() <= this.strength)
                beingInConflict.collisionAnswer(this);
            else this.kill();
        }
        else {
            collisionAnimal.runAway();
            game.addMessage(this.getOrganismType() + " from position " + this.getPositionNotation() + " deter " + collisionAnimal.getOrganismType() + " which go away to " + collisionAnimal.getPositionNotation() + "\n");
        }
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        if(!game.isAlzurShieldActive()) {
            this.kill();
            return;
        }
        var collisionAnimal = (Animal) beingInConflict;
        if(collisionAnimal != null)
            collisionAnimal.runAway();
    }
    @Override
    public Human createNewOne(Point2D positionForNew) {
        // human can not reproduce
        return null;
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        // there is only one human in the world
        return false;
    }
    @Override
    public String getOrganismType() {
        return "HUMAN";
    }
    @Override
    public void kill() {
        this.isAlive = false;
        game.endGame("You died");
    }
    @Override
    protected int getStep() {
        return Utils.Const.HUMAN_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(203, 34, 65);
    }
}
