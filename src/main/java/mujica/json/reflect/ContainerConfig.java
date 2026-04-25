package mujica.json.reflect;

import mujica.json.entity.JsonArray;
import mujica.json.entity.JsonObject;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@CodeHistory(date = "2026/4/13")
public class ContainerConfig {

    @NotNull
    Supplier<JsonArray> jsonArraySupplier = JsonArray::newArrayList;

    @NotNull
    public Supplier<JsonArray> getJsonArraySupplier() {
        return jsonArraySupplier;
    }

    public void setJsonArraySupplier(@NotNull Supplier<JsonArray> jsonArraySupplier) {
        this.jsonArraySupplier = jsonArraySupplier;
    }

    @CodeHistory(date = "2026/4/13")
    public enum ArrayAction {

        NEW, CLEAR, APPEND, COVER;

        private static final long serialVersionUID = 0xFB1D8004BA4F5337L;
    }

    @NotNull
    ArrayAction arrayAction = ArrayAction.NEW;

    @NotNull
    public ArrayAction getArrayAction() {
        return arrayAction;
    }

    public void setArrayAction(@NotNull ArrayAction arrayAction) {
        this.arrayAction = arrayAction;
    }

    @NotNull
    Supplier<JsonObject> jsonObjectSupplier = JsonObject::newHashMap;

    @NotNull
    public Supplier<JsonObject> getJsonObjectSupplier() {
        return jsonObjectSupplier;
    }

    public void setJsonObjectSupplier(@NotNull Supplier<JsonObject> jsonObjectSupplier) {
        this.jsonObjectSupplier = jsonObjectSupplier;
    }

    @CodeHistory(date = "2026/4/13")
    public enum ObjectAction {

        PUT, PUT_IF_ABSENT, PUT_IF_PRESENT, PUT_IF_NULL, PUT_IF_NOT_NULL;

        private static final long serialVersionUID = 0xC1238CA96BB9D7E1L;
    }

    @NotNull
    ObjectAction objectAction = ObjectAction.PUT;

    @NotNull
    public ObjectAction getObjectAction() {
        return objectAction;
    }

    public void setObjectAction(@NotNull ObjectAction objectAction) {
        this.objectAction = objectAction;
    }

    public ContainerConfig() {
        super();
    }
}
