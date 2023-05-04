package pl.edu.pg.eti.po.project.configurations;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.edu.pg.eti.po.project.worldsimulator.Game;
import pl.edu.pg.eti.po.project.worldsimulator.Utils;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.Organism;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import static java.lang.System.exit;

import pl.edu.pg.eti.po.project.worldsimulator.organisms.animals.*;
import pl.edu.pg.eti.po.project.worldsimulator.organisms.plants.*;

public class Config {
    private static final int NEVER_BEFORE = 0;

    private static final float HUNDRED_PERCENT = 100;
    private static final float FIELDS_OCCUPIED_BY_ORGANISM_PERCENT = 20/HUNDRED_PERCENT;
    private static final int DEFAULT_ORGANISM_TRAIT = -1;

    private final Game game;
    private int allPercent;

    public Config(Game game) {
        this.game = game;
    }

    public final void loadSave() {
        game.organisms.clear();
        try {
            File inputFile = new File("C:\\Users\\Ewa\\Desktop\\semestr 2\\PO\\projekty\\java\\src\\pl\\edu\\pg\\eti\\po\\project\\configurations\\save.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            loadSavedSettings(doc);
            loadSavedOrganisms(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final void doSave() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        saveSettings(doc, rootElement);
        saveOrganisms(doc, rootElement);

        try (FileOutputStream output = new FileOutputStream("C:\\Users\\Ewa\\Desktop\\semestr 2\\PO\\projekty\\java\\src\\pl\\edu\\pg\\eti\\po\\project\\configurations\\save.xml")) {
            writeXml(doc, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public final void loadConfig() {
        allPercent = 0;
        game.organisms.clear();
        try {
            File inputFile = new File("C:\\Users\\Ewa\\Desktop\\semestr 2\\PO\\projekty\\java\\src\\pl\\edu\\pg\\eti\\po\\project\\configurations\\config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            executeConfigSettings(doc);
            executeConfigOrganisms(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void displayError(String msg) { // enable to display errors
        System.out.println("Failed to parse the config file: %s\n" + msg);
        exit(1); // end program if there is an error
    }
    private static void writeXml(Document doc, OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    private void loadSavedOrganisms(Document doc) {
        NodeList nList = doc.getElementsByTagName("organism");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                String x = eElement.getElementsByTagName("x").item(0).getTextContent();
                String y = eElement.getElementsByTagName("y").item(0).getTextContent();
                String strength = eElement.getElementsByTagName("strength").item(0).getTextContent();
                String initiative = eElement.getElementsByTagName("initiative").item(0).getTextContent();
                String age = eElement.getElementsByTagName("age").item(0).getTextContent();

                createOrganism(type, new Point2D.Double(Double.parseDouble(x), Double.parseDouble(y)), Integer.parseInt(strength), Integer.parseInt(initiative), Integer.parseInt(age));
            }
        }
    }
    private void loadSavedSettings(Document doc) {
        NodeList nList = doc.getElementsByTagName("settings");

        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;

            String boardSizeW = eElement.getElementsByTagName("BoardSizeW").item(0).getTextContent();
            String boardSizeH = eElement.getElementsByTagName("BoardSizeH").item(0).getTextContent();
            this.game.setBgDimensions(Integer.parseInt(boardSizeW), Integer.parseInt(boardSizeH));

            String turns = eElement.getElementsByTagName("Turns").item(0).getTextContent();
            this.game.setTurnsNumber(Integer.parseInt(turns));

            String azurS = eElement.getElementsByTagName("AzurS").item(0).getTextContent();
            this.game.setAzurShield(Integer.parseInt(azurS));
        }
    }

    private void executeConfigSettings(Document doc) {
        NodeList nList = doc.getElementsByTagName("settings");

        returnToGameInitialSettings();

        Node nNode = nList.item(0);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;

            String boardSizeW = eElement.getElementsByTagName("BoardSizeW").item(0).getTextContent();
            String boardSizeH = eElement.getElementsByTagName("BoardSizeH").item(0).getTextContent();
            this.game.setBgDimensions(Integer.parseInt(boardSizeW), Integer.parseInt(boardSizeH));
            createOrganism("HUMAN", getRandomFreePosition(), DEFAULT_ORGANISM_TRAIT, DEFAULT_ORGANISM_TRAIT, NEVER_BEFORE);
        }
    }
    private void returnToGameInitialSettings() {
        this.game.setAzurShield(NEVER_BEFORE);
        this.game.setTurnsNumber(NEVER_BEFORE);
    }
    private void executeConfigOrganisms(Document doc) {
        NodeList nList = doc.getElementsByTagName("organism");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                String percent = eElement.getElementsByTagName("percent").item(0).getTextContent();
                createOrganismType(type, Integer.parseInt(percent));

            }
        }
    }
    private void createOrganismType(String type, int percent) {
        allPercent += percent;
        if (allPercent > 100) displayError("too many Organisms! Please, repair config");

        for (int i = 0; i < FIELDS_OCCUPIED_BY_ORGANISM_PERCENT * percent; i++) {
            createOrganism(type, getRandomFreePosition(), DEFAULT_ORGANISM_TRAIT, DEFAULT_ORGANISM_TRAIT, NEVER_BEFORE);
        }
    }

    private void saveSettings(Document doc, Element rootElement) {
        Element posXml = doc.createElement("settings");
        rootElement.appendChild(posXml);

        Element boardSizeW = doc.createElement("BoardSizeW");
        boardSizeW.appendChild(doc.createTextNode(String.valueOf(game.getWidth())));
        posXml.appendChild(boardSizeW);

        Element boardSizeH = doc.createElement("BoardSizeH");
        boardSizeH.appendChild(doc.createTextNode(String.valueOf(game.getHeight())));
        posXml.appendChild(boardSizeH);

        Element turns = doc.createElement("Turns");
        turns.appendChild(doc.createTextNode(String.valueOf(game.getTurnsNumber())));
        posXml.appendChild(turns);

        Element azurS = doc.createElement("AzurS");
        azurS.appendChild(doc.createTextNode(String.valueOf(game.getAzurShield())));
        posXml.appendChild(azurS);
    }
    private void saveOrganisms(Document doc, Element rootElement) {
        if(game.organisms == null) return;
        for(var organism : game.organisms) {
            Element posXml = doc.createElement("organism");
            rootElement.appendChild(posXml);
            saveOrganism(doc, posXml, organism);
        }
    }
    private void saveOrganism(Document doc, Element posXml, Organism org) {
        Element type = doc.createElement("type");
        type.appendChild(doc.createTextNode(org.getOrganismType()));
        posXml.appendChild(type);

        Element x = doc.createElement("x");
        x.appendChild(doc.createTextNode(String.valueOf(org.getPosition().getX())));
        posXml.appendChild(x);

        Element y = doc.createElement("y");
        y.appendChild(doc.createTextNode(String.valueOf(org.getPosition().getY())));
        posXml.appendChild(y);

        Element strength = doc.createElement("strength");
        strength.appendChild(doc.createTextNode(String.valueOf(org.getStrength())));
        posXml.appendChild(strength);

        Element initiative = doc.createElement("initiative");
        initiative.appendChild(doc.createTextNode(String.valueOf(org.getInitiative())));
        posXml.appendChild(initiative);

        Element age = doc.createElement("age");
        age.appendChild(doc.createTextNode(String.valueOf(org.getAge())));
        posXml.appendChild(age);
    }

    private void createOrganism(String type, Point2D position, int strength, int initiative, int age) {
        switch (type) {
            case "ANTELOPE" -> this.game.organisms.add(new Antelope(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.ANTELOPE_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.ANTELOPE_INITIATIVE : initiative, this.game, age));
            case "FOX" -> this.game.organisms.add(new Fox(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.FOX_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.FOX_INITIATIVE : initiative, this.game, age));
            case "HUMAN" -> this.game.organisms.add(new Human(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.HUMAN_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.HUMAN_INITIATIVE : initiative, this.game, age));
            case "LAMP" -> this.game.organisms.add(new Lamp(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.LAMP_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.LAMP_INITIATIVE : initiative, this.game, age));
            case "TURTLE" -> this.game.organisms.add(new Turtle(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.TURTLE_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.TURTLE_INITIATIVE : initiative, this.game, age));
            case "WOLF" -> this.game.organisms.add(new Wolf(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.WOLF_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.WOLF_INITIATIVE : initiative, this.game, age));
            case "DANDELION" -> this.game.organisms.add(new Dandelion(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_STRENGTH : strength, initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_INITIATIVE : initiative, this.game, age));
            case "GRASS" -> this.game.organisms.add(new Grass(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_INITIATIVE : initiative, this.game, age));
            case "GUARANA" -> this.game.organisms.add(new Guarana(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_INITIATIVE : initiative, this.game, age));
            case "NIGHTSHADE" -> this.game.organisms.add(new Nightshade(position, strength == DEFAULT_ORGANISM_TRAIT ? Utils.Const.NIGHTSHADE_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT ? Utils.Const.PLANT_INITIATIVE : initiative, this.game, age));
            case "SOSNOWSKY" -> this.game.organisms.add(new Sosnowsky_hogweed(position, strength == DEFAULT_ORGANISM_TRAIT
                    ? Utils.Const.SOSNOWSKY_STRENGTH : strength,
                    initiative == DEFAULT_ORGANISM_TRAIT
                            ? Utils.Const.PLANT_INITIATIVE : initiative, this.game, age));
        }
    }
    private Point2D getRandomFreePosition() {
        Point2D position = new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR);
        while (position.equals(new Point2D.Double(Utils.Const.ERROR, Utils.Const.ERROR))) {
            Point2D randomPos = Utils.randomPosition(game.getWidth(), game.getHeight());
            for (var organism: game.organisms) {
                if (organism.getPosition().equals(randomPos)) break;
            }
            position = randomPos;
        }
        return position;
    }
}
