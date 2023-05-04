package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Lamp extends Animal {
    public Lamp(Point2D position, Game game) {
        super(position, Utils.Const.LAMP_STRENGTH, Utils.Const.LAMP_INITIATIVE, game);
    }
    public Lamp(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public Lamp createNewOne(Point2D positionForNew) {
        return new Lamp(positionForNew, this.game);
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        return beingInConflict instanceof Lamp;
    }
    @Override
    public String getOrganismType() {
        return "LAMP";
    }
    @Override
    protected int getStep() {
        return Utils.Const.LAMP_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(28, 36, 85);
    }
}
