package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;

import java.awt.*;
import java.awt.geom.Point2D;

public class Grass extends Plant {
    public Grass(Point2D position, Game game) {
        super(position, Utils.Const.PLANT_STRENGTH, game);
    }
    public Grass(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public Grass createNewOne(Point2D positionForNew) {
        return new Grass(positionForNew, this.game);
    }
    @Override
    public String getOrganismType() {
        return "GRASS";
    }
    @Override
    public Color getColor() {
        return new Color(146, 33, 85);
    }
}
