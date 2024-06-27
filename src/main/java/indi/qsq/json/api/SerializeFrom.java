package indi.qsq.json.api;

/**
 * Created in infrastructure on 2021/12/31.
 * Recreated on 2021/6/9.
 */
public interface SerializeFrom {

    int NULL                = 0x0001;
    int FALSE               = 0x0002;
    int ZERO_INTEGRAL       = 0x0004;
    int ZERO_DECIMAL        = 0x0008;
    int INFINITE            = 0x0010;
    int NAN                 = 0x0020;
    int EMPTY_STRING        = 0x0040;
    int BLANK_STRING        = 0x0080;
    int EMPTY_ARRAY         = 0x0200;
    int EMPTY_OBJECT        = 0x0400;
    int CYCLIC_OBJECT       = 0x0800;
    int DEFAULT_ENUM        = 0x1000;
    int OUT_ENUM            = 0x2000;
    int SPECIFIC            = 0x4000;

    int MASK                = 0x7fff; // not 0x4fff
}
