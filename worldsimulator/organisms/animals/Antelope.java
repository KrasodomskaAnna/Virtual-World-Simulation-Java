package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Antelope extends Animal {
    public Antelope(Point2D position, Game game) {
        super(position, Utils.Const.ANTELOPE_STRENGTH, Utils.Const.ANTELOPE_INITIATIVE, game);
    }
    public Antelope(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public Antelope createNewOne(Point2D positionForNew) {
        return new Antelope(positionForNew, this.game);
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        return beingInConflict instanceof Antelope;
    }
    @Override
    public String getOrganismType() {
        return "ANTELOPE";
    }
    @Override
    protected int getStep() {
        return Utils.Const.ANTELOPE_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(33, 58, 85);
    }

    @Override
    public boolean handleDeath(Organism beingInConflict) {
        if(!(beingInConflict instanceof Animal)) {
            if(escape()) return false;
        }
        this.kill();
        return true;
    }

    private boolean escape() {
        if(!Utils.randomChoiceProbability(Utils.Const.PROBABILITY_ANTELOPE_ESCAPE)) return false;
        Point2D newPosition = randomNeighbor(true, Utils.Const.NEIGHBOUR_SHIFT);
        if(newPosition.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) return false;
        this.previousPosition = this.position;
        this.position = newPosition;
        game.addMessage(this.getOrganismType() + " runAway from attack" + "\n");
        return true;
    }
}
