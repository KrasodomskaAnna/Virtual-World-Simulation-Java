package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.animals.Animal;

import java.awt.*;
import java.awt.geom.Point2D;

public class Sosnowsky_hogweed extends Plant {
    public Sosnowsky_hogweed(Point2D position, Game game) {
        super(position, Utils.Const.SOSNOWSKY_STRENGTH, game);
    }
    public Sosnowsky_hogweed(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }
    @Override
    public void action() {
        for(var organism : game.organisms) {
            for(int x = -1; x <= 1; x++) {
                for(int y = -1; y <= 1; y++) {
                    if(x == 0 && y == 0) continue;
                    if(organism.getPosition().equals(new Point2D.Double(x + position.getX(), y+ position.getY())))
                        canKillAndExecute(organism);
                }
            }
        }
        this.sow();
    }
    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        beingInConflict.kill();
        game.addMessage(beingInConflict.getOrganismType() + " is kill by " + this.getOrganismType() + "\n");
    }
    @Override
    public Sosnowsky_hogweed createNewOne(Point2D positionForNew) {
        return new Sosnowsky_hogweed(positionForNew, this.game);
    }
    @Override
    public String getOrganismType() {
        return "SOSNOWSKY";
    }
    @Override
    public Color getColor() {
        return new Color(60, 1, 95);
    }
    private void canKillAndExecute(Organism beingInConflict) {
        if(!(beingInConflict instanceof Animal)) return;
        beingInConflict.kill();
        game.addMessage(beingInConflict.getOrganismType() + " is kill by " + this.getOrganismType() + "\n");
    }
}
