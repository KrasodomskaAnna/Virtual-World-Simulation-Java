package pl.edu.pg.eti.po.project.worldsimulator.organisms.plants;

import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.animals.Animal;

import java.awt.geom.Point2D;

public abstract class Plant extends Organism {
    public Plant(Point2D position, int strength, Game game) {
        super(position, strength, Utils.Const.PLANT_INITIATIVE, game);
    }
    public Plant(Point2D position, int strength, int initiative, Game game, int age) {
        super(position, strength, initiative, game, age);
    }

    @Override
    public void collisionAnswer(Organism beingInConflict) {
        if(beingInConflict == null) return;
        kill();
        if(beingInConflict instanceof Animal)
            game.addMessage(beingInConflict.getOrganismType() + " change position to " + this.getPositionNotation() + " so eat " + this.getOrganismType() + "\n");
    }

    @Override
    public void action() {
        sow();
    }

    @Override
    public void collision() {
        collisionAnswer(collisionWith());
    }

    public void sow() {
        if(!Utils.randomChoiceProbability(Utils.Const.PROBABILITY_SOW)) return;
        Point2D positionForNew = randomNeighbor(true, Utils.Const.NEIGHBOUR_SHIFT);
        if(positionForNew.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) return;
        game.addMessage(this.getOrganismType() + " sow a new plant!" + "\n");
        game.organisms.add(this.createNewOne(positionForNew));
    }
    public abstract Organism createNewOne(Point2D positionForNew);
}
