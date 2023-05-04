package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.animals.Animal;

import java.awt.*;
import java.awt.geom.Point2D;

public class Guarana extends Plant {
    public Guarana(Point2D position, Game game) {
        super(position, Utils.Const.PLANT_STRENGTH, game);
    }
    public Guarana(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        if(beingInConflict instanceof Animal) return;
        beingInConflict.setStrength(beingInConflict.getStrength() + Utils.Const.GUARANA_INCREASE_STRENGTH_WHO_EAT);
        game.addMessage(this.getOrganismType() + " increased strength of " + beingInConflict.getOrganismType() + " so it's now " +
                beingInConflict.getStrength() + "\n");
        kill();
    }
    @Override
    public Guarana createNewOne(Point2D positionForNew) {
        return new Guarana(positionForNew, this.game);
    }
    @Override
    public String getOrganismType() {
        return "GUARANA";
    }
    @Override
    public Color getColor() {
        return new Color(225, 11, 15);
    }
}
