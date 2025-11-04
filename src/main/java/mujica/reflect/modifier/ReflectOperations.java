package mujica.reflect.modifier;

/**
 * Created on 2025/10/30.
 */
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "UseOrder")
@CodeHistory(date = "2022/6/11", project = "Ultramarine")
@CodeHistory(date = "2025/10/30")
@ConstantInterface
public interface ReflectOperations {

    int PARSE_JSON              = 0x01;
    int SERIALIZE_JSON          = 0x02;

    int PARSE_XML               = 0x10;
    int SERIALIZE_XML           = 0x20;
}
