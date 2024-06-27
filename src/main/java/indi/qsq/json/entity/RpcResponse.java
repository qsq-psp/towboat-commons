package indi.um.json.entity;

import indi.um.json.api.JsonName;
import indi.um.json.api.SerializeFrom;
import indi.um.json.api.SerializeToUndefined;

import java.io.Serializable;

/**
 * Created on 2024/4/2.
 *
 * https://www.jsonrpc.org/specification
 */
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 0x45FF070BA4A475F0L;

    @JsonName("jsonrpc")
    @SerializeToUndefined(SerializeFrom.NULL | SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING)
    public String version;

    @JsonName("id")
    public int transactionID;

    @SerializeToUndefined(SerializeFrom.NULL)
    public Object result;

    @SerializeToUndefined(SerializeFrom.NULL)
    public ErrorObject error;

    public RpcResponse() {
        super();
    }

    public RpcResponse checkVersion() {
        if (!RpcRequest.DEFAULT_VERSION.equals(version)) {
            throw new RuntimeException("Bad JSON RPC version: " + version);
        }
        return this;
    }

    public static class ErrorObject implements Serializable {

        private static final long serialVersionUID = 0x7503FEA54E1FA5E6L;

        public int code;

        public String message;

        public Object data;

        public ErrorObject() {
            super();
        }

        public static final int CODE_PARSE_ERROR = -32700;

        public static final int CODE_INVALID_REQUEST = -32600;

        public static final int CODE_METHOD_NOT_FOUND = -32601;

        public static final int CODE_INVALID_PARAMETERS = -32602;

        public static final int CODE_INTERNAL_ERROR = -32603;

        public static final int MAX_CODE_SERVER_ERROR = -32000;

        public static final int MIN_CODE_SERVER_ERROR = -32099;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[code = ").append(code);
            switch (code) {
                case CODE_PARSE_ERROR:
                    sb.append("(parse error)");
                    break;
                case CODE_INVALID_REQUEST:
                    sb.append("(invalid request)");
                    break;
                case CODE_METHOD_NOT_FOUND:
                    sb.append("(method not found)");
                    break;
                case CODE_INVALID_PARAMETERS:
                    sb.append("(invalid parameters)");
                    break;
                case CODE_INTERNAL_ERROR:
                    sb.append("(internal error)");
                    break;
                default:
                    if (MIN_CODE_SERVER_ERROR <= code && code <= MAX_CODE_SERVER_ERROR) {
                        sb.append("(server error)");
                    }
                    break;
            }
            if (message != null) {
                sb.append(", message = ").append(message);
            }
            return sb.append("]").toString();
        }
    }
}
