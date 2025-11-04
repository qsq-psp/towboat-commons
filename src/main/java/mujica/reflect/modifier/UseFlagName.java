package mujica.reflect.modifier;

@CodeHistory(date = "2022/9/11", project = "Ultramarine", name = "AnyFlag")
@CodeHistory(date = "2025/10/29")
public @interface UseFlagName {

    /**
     * @return name of the static field of type {@link FlagName}
     */
    String value();
}
