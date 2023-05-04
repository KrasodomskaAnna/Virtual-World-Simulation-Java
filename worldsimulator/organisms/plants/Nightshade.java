package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Nightshade extends Plant {
    public Nightshade(Point2D position, Game game) {
        super(position, Utils.Const.NIGHTSHADE_STRENGTH, game);
    }
    public Nightshade(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        beingInConflict.kill();
        game.addMessage(beingInConflict.getOrganismType() + " is kill by " + this.getOrganismType() + "\n");
    }
    @Override
    public Nightshade createNewOne(Point2D positionForNew) {
        return new Nightshade(positionForNew, this.game);
    }
    @Override
    public String getOrganismType() {
        return "NIGHTSHADE";
    }
    @Override
    public Color getColor() {
        return new Color(287, 29, 95);
    }
}
