package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;

import java.awt.*;
import java.awt.geom.Point2D;

public class Dandelion extends Plant {
    public Dandelion(Point2D position, Game game) {
        super(position, Utils.Const.PLANT_STRENGTH, game);
    }
    public Dandelion(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void action() {
        for(int i = 0; i < Utils.Const.TRY_DANDELION_TO_SOW_NUMBER; i++)
            this.sow();
    }
    @Override
    public Dandelion createNewOne(Point2D positionForNew) {
        return new Dandelion(positionForNew, this.game);
    }
    @Override
    public String getOrganismType() {
        return "DANDELION";
    }
    @Override
    public Color getColor() {
        return new Color(45, 98, 95);
    }
}
