package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2026/4/14")
@ReferencePage(title = "Algorithms Jeff Erickson", href = "http://jeffe.cs.illinois.edu/teaching/algorithms/")
@ReferencePage(title = "Algorithms Jeff Erickson", href = "http://algorithms.wtf")
public class JsonContextTest3 {

    @Name(value = "beer", language = "json")
    public int nBottlesOfBeerOnTheWall;

    @Name(value = "take", language = "json")
    public int takeOneDownPassItAround;

    @Name(value = "Christmas", language = "en")
    @Name(value = "love", language = "json")
    public long onTheDayOfChristmasMyTrueLoveGaveToMe;

    @Name(value = "barely-mow", language = "en")
    @Name(value = "health", language = "json")
    public long healthToTheBarelyMowMyBraveBoys;

    @Name(value = "journal", language = "json")
    public String internationalJournalOfComputationalGeometryAndApplications;

    @Name(value = "symposium", language = "json")
    public String symposiumOnTheoreticalAspectsOfComputerScience;

    private int idea;

    @Name(value = "dream", language = "json")
    public int beAMillionaireAndNeverPayTaxes() {
        return idea;
    }

    @Name(value = "dream", language = "json")
    public void teachOtherPeopleHowAndWhyYourAlgorithmWork(int newIdea) {
        idea = newIdea;
    }

    @CodeHistory(date = "2022/8/17", project = "Ultramarine")
    @ReferencePage(title = "Jakarta commons cookbook", href = "#")
    @FieldOrder({"milesGallon", "CO2Output", "percentElectric"})
    public static class Engine {

        public Integer milesGallon;

        public Integer CO2Output;

        public Integer percentElectric;
    }

    @CodeHistory(date = "2022/8/17", project = "Ultramarine")
    @FieldOrder({"horsePower", "doors", "engine", "make", "model", "year"})
    public static class ElectricVehicle {

        public Integer horsePower;

        public Integer doors;

        public Engine engine;

        public String make;

        public String model;

        public Integer year;
    }
}
