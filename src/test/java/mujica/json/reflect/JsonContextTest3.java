package mujica.json.reflect;

import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.Nullable;

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

        @JsonHint(JsonHint.NULLABLE)
        public Integer milesGallon;

        @JsonHint(JsonHint.NULLABLE)
        public Integer CO2Output;

        @JsonHint(JsonHint.NULLABLE)
        public Integer percentElectric;
    }

    @CodeHistory(date = "2022/8/17", project = "Ultramarine")
    @FieldOrder({"horsePower", "doors", "engine", "make", "model", "year"})
    public static class ElectricVehicle {

        @JsonHint(JsonHint.NULLABLE)
        public Integer horsePower;

        @JsonHint(JsonHint.UNSIGNED)
        public Integer doors;

        @Nullable
        @JsonHint(JsonHint.NULLABLE)
        public Engine engine;

        @JsonHint(JsonHint.NULLABLE)
        public String make;

        @JsonHint(JsonHint.NULLABLE)
        public String model;

        @JsonHint(JsonHint.NULLABLE | JsonHint.UNSIGNED)
        public Integer year;
    }

    @JsonHint(JsonHint.NULLABLE)
    public ElectricVehicle vehicle;

    @CodeHistory(date = "2026/5/2")
    public static class StandardColor {

        @Name(value = "L", language = "json")
        public double luminosity;

        @Name(value = "a*", language = "json")
        public double aStar;

        @Name(value = "b*", language = "json")
        public double bStar;
    }

    @Nullable
    @JsonHint(JsonHint.NULLABLE)
    public StandardColor color;

    public transient StandardColor referenceColor;


}
