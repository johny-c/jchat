package client.db.util;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {

    @Override
    public String classToTableName(String className) {
        String tableName = className.charAt(0) + "";
        for (char c : className.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tableName += "_";
            }
            tableName += Character.toUpperCase(c);
        }
        tableName = tableName.replaceFirst("_", "");
        tableName += "S";
        return tableName;
    }

    @Override
    public String propertyToColumnName(String propertyName) {

        String columnName = "";
        for (char c : propertyName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                columnName += "_";
            }
            columnName += Character.toUpperCase(c);
        }

        return columnName;
    }
}
