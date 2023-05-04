package pl.edu.pg.eti.po.project.worldsimulator;

import java.util.ArrayList;
import java.util.stream.Collectors;

import pl.edu.pg.eti.po.project.configurations.Config;
import pl.edu.pg.eti.po.project.worldsimulator.graphics.Graphics;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class Game {
    public ArrayList<Organism> organisms;
    private final Graphics graphics;

    private int width = 0, height = 0;
    // Alzur Shield -> if is 0 it can be use, if is + for this number of turns will be active, if is - for this number of turns can't be use
    private int alzurShield = 0;
    private int turnsNumber = 0;
    private final ArrayList<String> messages = new ArrayList<>();
    private String warnings = "";
    private String input = "";

    public Game() {
        this.graphics = new Graphics(this);
        this.organisms = new ArrayList<>();
    }
    public void loadSave() {
        var config = new Config(this);
        config.loadSave();
        updateBoard();
    }
    public void saveGame() {
        var config = new Config(this);
        try {
            config.doSave();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {
        var config = new Config(this);
        config.loadConfig();
        updateBoard();
    }

    public void nextTurn() {
        this.messages.clear();
        this.warnings="";

        organisms.sort((Organism a, Organism b) -> {if (a.getInitiative() > b.getInitiative()) return -1;
        if (a.getInitiative() < b.getInitiative()) return 1;
        if (a.getAge() > b.getAge()) return -1;
        if (a.getAge() < b.getAge()) return 1;
        return 1;});

        organisms.forEach(organism -> {
            if(!organism.getIsAlive()) return;
            organism.action();
            organism.collision();
        });
        this.organisms = organisms.parallelStream().filter(organism -> {
            if (!organism.getIsAlive()) return false;
            organism.increaseAge();
            return true;
        }).collect(Collectors.toCollection(ArrayList::new));
        if(organisms.size() == 1)  endGame("You stay alone");

        if(this.alzurShield == 1) alzurShield = -Utils.Const.ALZUR_SHIELD_LOCK;
        else if(this.alzurShield < 0) alzurShield++;
        else if(this.alzurShield > 0) alzurShield--;
        this.turnsNumber++;
        updateBoard();
    }

    public boolean isAlzurShieldActive() {
        return alzurShield > 0;
    }
    public boolean activeAzurShield() {
        if(alzurShield != 0) return false;
        this.alzurShield = Utils.Const.ALZUR_SHIELD_DURATION_SET;
        return true;
    }
    public void endGame(String report) {
        graphics.endGame(report);
    }

    public String getInput(){
        return input;
    }
    public void setInput(String input){
        this.input = input;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getAzurShield() {
        return this.alzurShield;
    }
    public void setAzurShield(int value) {
        alzurShield = value;
    }
    public void setBgDimensions(int _width, int _height) {
        this.width = _width;
        this.height = _height;
        graphics.setBoardDimensions(width, height);
    }
    public int getTurnsNumber(){
        return this.turnsNumber;
    }
    public void setTurnsNumber(int _turnsNumber) {
        this.turnsNumber = _turnsNumber;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
    public String getWarnings() {
        return warnings;
    }
    public void addMessage(String _message){
        messages.add(_message);
    }
    public void addWarning(String _warning){
        warnings += _warning;
    }

    private void updateBoard() {
        graphics.clearBoard();
        for(var organism : organisms) {
            graphics.setColor((int)organism.getPosition().getX(), (int)organism.getPosition().getY(), organism.getColor());
        }
        graphics.updateComments();
    }
}
