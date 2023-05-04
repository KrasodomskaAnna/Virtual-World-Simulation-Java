package pl.edu.pg.eti.po.project.worldsimulator.organisms;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;


public abstract class Organism {
    protected final Game game;
    protected int strength;
    protected int initiative;
    protected int age = 0;
    protected boolean isAlive = true;
    protected Point2D position;

    public Organism(Point2D position, int strength, int initiative, Game game) {
        this(position, strength, initiative, game, 0);
    }
    public Organism(Point2D position, int strength, int initiative, Game game, int age) {
        this.position = position;
        this.strength = strength;
        this.initiative = initiative;
        this.game = game;
        this.age = age;
    }
    public abstract void action();
    public abstract void collision();
    public abstract void collisionAnswer(Organism beingInConflict);
    public abstract String getOrganismType();
    public abstract Color getColor();
    public abstract Organism createNewOne(Point2D positionForNew);

    public boolean getIsAlive() {
        return this.isAlive;
    }
    public void kill() {
        this.isAlive = false;
    }
    public void setStrength(int _strength) {
        this.strength = _strength;
    }
    public int getStrength() {
        return  this.strength;
    }
    public int getInitiative() {
        return this.initiative;
    }
    public int getAge() {
        return this.age;
    }
    public void increaseAge() {
        this.age++;
    }
    public Point2D getPosition() {
        return this.position;
    }
    public String getPositionNotation() {
        return  "(" + this.getPosition().getX() + ", " + this.getPosition().getY() + ")";
    }

    protected boolean isOnBoard(Point2D _position) {
        return _position.getX() < game.getWidth() && _position.getX() >= Utils.Const.CORD_ORIGIN && _position.getY() < game.getHeight() && _position.getY() >= Utils.Const.CORD_ORIGIN;
    }
    protected Point2D randomNeighbor(boolean moveOnEmptyField, int shift) {
        ArrayList<Point2D> freePositions = new ArrayList<>();
        Point2D.Double point = new Point2D.Double(20,20);
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                if(x == 0 && y == 0) continue;
                Point2D check = new Point2D.Double(x+position.getX(), y+position.getY());
                if(isOnBoard(check)) {
                    if(moveOnEmptyField) {
                        if(isFree(check))
                            freePositions.add(check);
                    }
                    else freePositions.add(check);
                }
            }
        }
        if(freePositions.isEmpty()) return new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR);
        Random generator = new Random();
        int randomChoice = generator.nextInt(freePositions.size());
        for(var freePosition : freePositions) {
            if(randomChoice == 0) return freePosition;
            else randomChoice--;
        }
        return new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR);
    }
    protected boolean isFree(Point2D _position){
        for(var organism : game.organisms) {
            if(_position.equals(organism.getPosition())) {
                return false;
            }
        }
        return true;
    }
    protected Organism collisionWith() {
        for(var organism : game.organisms) {
            if(organism == this) continue;
            if(this.position.equals(organism.getPosition())) {
                if(organism.getIsAlive())
                    return organism;
            }
        }
        return null;
    }
}
