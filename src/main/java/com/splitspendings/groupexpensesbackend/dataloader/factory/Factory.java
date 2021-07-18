package com.splitspendings.groupexpensesbackend.dataloader.factory;

public interface Factory<T> {

    String DELIMITER = "_";

    T generate();

    default String format(String columnName, int number) {
        return columnName + DELIMITER + number;
    }
}
