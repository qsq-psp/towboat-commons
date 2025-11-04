package mujica.reflect.modifier;

@CodeHistory(date = "2022/8/12", project = "Ultramarine", name = "AnyEnum")
@CodeHistory(date = "2025/10/29")
public @interface UseEnumMapping {

    /**
     * @return name of the static field of type {@link EnumMapping}
     */
    String value();
}
