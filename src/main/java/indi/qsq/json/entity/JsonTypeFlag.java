package indi.um.json.entity;

/**
 * Created on 2022/7/26.
 */
public interface JsonTypeFlag {

    int NULL            = 0x01;
    int BOOLEAN         = 0x02;
    int INTEGRAL        = 0x04;
    int DECIMAL         = 0x08;
    int STRING          = 0x10;
    int OBJECT          = 0x20;
    int ARRAY           = 0x40;
}
