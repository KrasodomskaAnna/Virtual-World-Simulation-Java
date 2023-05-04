package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Fox extends Animal {
    public Fox(Point2D position, Game game) {
        super(position, Utils.Const.FOX_STRENGTH, Utils.Const.FOX_INITIATIVE, game);
    }
    public Fox(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public Fox createNewOne(Point2D positionForNew) {
        return new Fox(positionForNew, this.game);
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        return beingInConflict instanceof Fox;
    }
    @Override
    public String getOrganismType() {
        return "FOX";
    }
    @Override
    protected int getStep() {
        return Utils.Const.FOX_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(28, 81, 85);
    }
    @Override
    protected boolean isFree(Point2D _position) {
        for(var organism : game.organisms) {
            if(_position.equals(organism.getPosition())) {
                if(organism.getStrength() > this.strength) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    protected Point2D positionMove() {
        return randomNeighbor(true, this.getStep());
    }
}
