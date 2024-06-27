package indi.qsq.json.api;

/**
 * Created in infrastructure on 2021/12/30, named UseOrder.
 * Recreated on 2022/6/11.
 */
public interface ReflectOperations {

    int REFLECT_COLLECT = 0x01;

    int JSON_PARSE = 0x02;

    int JSON_SERIALIZE = 0x04;

    int ALL = JSON_PARSE | JSON_SERIALIZE;
}
