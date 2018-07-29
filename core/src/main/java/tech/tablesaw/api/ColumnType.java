package tech.tablesaw.api;

import com.google.common.base.Preconditions;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.List;

public interface ColumnType {

    List<ColumnType> values = new ArrayList<>();

    // standard column types
    ColumnType BOOLEAN = new StandardColumnType(Byte.MIN_VALUE, 1, "BOOLEAN", "Boolean");
    ColumnType STRING = new StandardColumnType("", 4, "STRING", "String");
    ColumnType NUMBER = new StandardColumnType(Double.NaN, 8, "NUMBER", "Number");
    ColumnType LOCAL_DATE = new StandardColumnType(Integer.MIN_VALUE, 4, "LOCAL_DATE", "Date");
    ColumnType LOCAL_DATE_TIME = new StandardColumnType(Long.MIN_VALUE, 8, "LOCAL_DATE_TIME","DateTime");
    ColumnType LOCAL_TIME = new StandardColumnType(Integer.MIN_VALUE, 4, "LOCAL_TIME", "Time");
    ColumnType SKIP = new StandardColumnType(null, 0, "SKIP", "Skipped");

    static void register(ColumnType type) {
        values.add(type);
    }

    static ColumnType[] values() {
        return values.toArray(new ColumnType[0]);
    }

    static ColumnType valueOf(String name) {
        Preconditions.checkNotNull(name);

        for (ColumnType type : values) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(name + " is not a registered column type.");
    }

    default Column create(String name) {
        final String columnTypeName = this.name();
        switch (columnTypeName) {
            case "BOOLEAN": return BooleanColumn.create(name);
            case "STRING": return StringColumn.create(name);
            case "NUMBER": return DoubleColumn.create(name);
            case "LOCAL_DATE": return DateColumn.create(name);
            case "LOCAL_DATE_TIME": return DateTimeColumn.create(name);
            case "LOCAL_TIME": return TimeColumn.create(name);
            case "SKIP": throw new IllegalArgumentException("Cannot create column of type SKIP");
        }
        throw new UnsupportedOperationException("Column type " + name() + " doesn't support column creation");
    }

    String name();

    Comparable<?> getMissingValue();

    int byteSize();

    String getPrinterFriendlyName();

}
