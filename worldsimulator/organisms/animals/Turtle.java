package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Turtle extends Animal {
    public Turtle(Point2D position, Game game) {
        super(position, Utils.Const.TURTLE_STRENGTH, Utils.Const.TURTLE_INITIATIVE, game);
    }
    public Turtle(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void action() {
        if(!Utils.randomChoiceProbability(Utils.Const.PROBABILITY_TURTLE_MOVE)) return;
        move();
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        if(beingInConflict.getStrength() < Utils.Const.STRENGTH_REPEL_ATTACK_TURTLE) {
            if(!(beingInConflict instanceof Animal)) return;
            ((Animal) beingInConflict).comeBack();
            game.addMessage(beingInConflict.getOrganismType() + " attack is repel by " + this.getOrganismType() + " and must back off to the previous position" + "\n");
        }
        else {
            kill();
            game.addMessage(this.getOrganismType() + " is kill by " + beingInConflict.getOrganismType() + "\n");
        }
    }
    @Override
    public Turtle createNewOne(Point2D positionForNew) {
        return new Turtle(positionForNew, this.game);
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        return beingInConflict instanceof Turtle;
    }
    @Override
    public String getOrganismType() {
        return "TURTLE";
    }
    @Override
    protected int getStep() {
        return Utils.Const.TURTLE_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(145, 15, 85);
    }
}
