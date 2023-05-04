package pl.edu.pg.eti.po.project.worldsimulator.organisms.animals;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.*;
import java.awt.geom.Point2D;

public class Wolf extends Animal {
    public Wolf(Point2D position, Game game) {
        super(position, Utils.Const.WOLF_STRENGTH, Utils.Const.WOLF_INITIATIVE, game);
    }
    public Wolf(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public Wolf createNewOne(Point2D positionForNew) {
        return new Wolf(positionForNew, this.game);
    }
    @Override
    public boolean isTheSameSpecie(Organism beingInConflict) {
        return beingInConflict instanceof Wolf;
    }
    @Override
    public String getOrganismType() {
        return "WOLF";
    }
    @Override
    protected int getStep() {
        return Utils.Const.WOLF_STEP;
    }
    @Override
    public Color getColor() {
        return new Color(214, 4, 75);
    }
}
