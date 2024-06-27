package indi.um.json.api;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created on 2022/7/17.
 */
public enum DatabaseColumnType {

    INT(false, false),
    INT_UNSIGNED(true, false),
    INT_NULLABLE(false, true),
    INT_UNSIGNED_NULLABLE(true, true),
    LONG(false, false),
    LONG_UNSIGNED(true, false),
    LONG_NULLABLE(false, true),
    LONG_UNSIGNED_NULLABLE(true, true),
    TIMESTAMP(false, false),
    TIMESTAMP_NULLABLE(false, true),
    STRING(false, false),
    STRING_NULLABLE(false, true);

    private static final long serialVersionUID = 0x2F5314B5B4AEB7BCL;

    public final boolean unsigned, nullable;

    DatabaseColumnType(boolean unsigned, boolean nullable) {
        this.unsigned = unsigned;
        this.nullable = nullable;
    }

    @SuppressWarnings("unused") // Preserve this
    @Nullable
    public Object get(ResultSet resultSet, int columnIndex) throws SQLException {
        switch (this) {
            case INT:
                return resultSet.getInt(columnIndex);
            case INT_UNSIGNED:
                return Integer.parseUnsignedInt(resultSet.getString(columnIndex));
            case INT_NULLABLE: {
                int value = resultSet.getInt(columnIndex);
                if (!resultSet.wasNull()) {
                    return value;
                }
                break;
            }
            case INT_UNSIGNED_NULLABLE: {
                String value = resultSet.getString(columnIndex);
                if (value != null) {
                    return Integer.parseUnsignedInt(value);
                }
                break;
            }
            case LONG:
                return resultSet.getLong(columnIndex);
            case LONG_UNSIGNED:
                return Long.parseUnsignedLong(resultSet.getString(columnIndex));
            case LONG_NULLABLE: {
                long value = resultSet.getLong(columnIndex);
                if (!resultSet.wasNull()) {
                    return value;
                }
                break;
            }
            case LONG_UNSIGNED_NULLABLE: {
                String value = resultSet.getString(columnIndex);
                if (value != null) {
                    return Long.parseUnsignedLong(value);
                }
                break;
            }
            case TIMESTAMP:
                return resultSet.getTimestamp(columnIndex).getTime();
            case TIMESTAMP_NULLABLE: {
                Timestamp value = resultSet.getTimestamp(columnIndex);
                if (value != null) {
                    return value.getTime();
                }
                break;
            }
            case STRING:
            case STRING_NULLABLE:
                return resultSet.getString(columnIndex);
        }
        return null;
    }

    public void get(ResultSet resultSet, int columnIndex, JsonConsumer jc) throws SQLException {
        switch (this) {
            case INT:
                jc.numberValue(resultSet.getInt(columnIndex));
                break;
            case INT_UNSIGNED:
                jc.numberValue(Integer.parseUnsignedInt(resultSet.getString(columnIndex)));
                break;
            case INT_NULLABLE: {
                int value = resultSet.getInt(columnIndex);
                if (resultSet.wasNull()) {
                    jc.nullValue();
                } else {
                    jc.numberValue(value);
                }
                break;
            }
            case INT_UNSIGNED_NULLABLE: {
                String value = resultSet.getString(columnIndex);
                if (value != null) {
                    jc.numberValue(Integer.parseUnsignedInt(value));
                } else {
                    jc.nullValue();
                }
                break;
            }
            case LONG:
                jc.numberValue(resultSet.getLong(columnIndex));
                break;
            case LONG_UNSIGNED:
                jc.numberValue(Long.parseUnsignedLong(resultSet.getString(columnIndex)));
            case LONG_NULLABLE: {
                long value = resultSet.getLong(columnIndex);
                if (resultSet.wasNull()) {
                    jc.nullValue();
                } else {
                    jc.numberValue(value);
                }
                break;
            }
            case LONG_UNSIGNED_NULLABLE: {
                String value = resultSet.getString(columnIndex);
                if (value != null) {
                    jc.numberValue(Long.parseUnsignedLong(value));
                } else {
                    jc.nullValue();
                }
                break;
            }
            case TIMESTAMP:
                jc.numberValue(resultSet.getTimestamp(columnIndex).getTime());
                break;
            case TIMESTAMP_NULLABLE: {
                Timestamp value = resultSet.getTimestamp(columnIndex);
                if (value != null) {
                    jc.numberValue(value.getTime());
                } else {
                    jc.nullValue();
                }
                break;
            }
            case STRING:
                jc.stringValue(resultSet.getString(columnIndex));
                break;
            case STRING_NULLABLE: {
                String value = resultSet.getString(columnIndex);
                if (value != null) {
                    jc.stringValue(value);
                } else {
                    jc.nullValue();
                }
                break;
            }
            default:
                jc.nullValue();
                break;
        }
    }
}
