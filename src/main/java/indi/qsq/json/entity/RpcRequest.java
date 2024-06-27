package indi.qsq.json.entity;

import indi.qsq.json.api.JsonName;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.SerializeToUndefined;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created on 2024/4/2.
 *
 * https://www.jsonrpc.org/specification
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 0x8C01755B2B0B9656L;

    public static final String DEFAULT_VERSION = "2.0";

    @JsonName("jsonrpc")
    @SerializeToUndefined(SerializeFrom.NULL | SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING)
    public String version;

    @JsonName("id")
    public int transactionID;

    @JsonName("method")
    public String methodName;

    @JsonName("params")
    @SerializeToUndefined(SerializeFrom.NULL)
    public Object parameters;

    public RpcRequest() {
        super();
    }

    public RpcRequest defaultVersion() {
        version = DEFAULT_VERSION;
        return this;
    }

    public RpcRequest setParameterList() {
        parameters = List.of();
        return this;
    }

    public RpcRequest setParameterMap() {
        parameters = Map.of();
        return this;
    }
}
