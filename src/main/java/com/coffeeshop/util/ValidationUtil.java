package com.coffeeshop.util;

import com.coffeeshop.exception.DataTidakValidException;
import com.coffeeshop.exception.InputKosongException;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static void requireNotBlank(String value, String fieldName) throws InputKosongException {
        if (value == null || value.isBlank()) {
            throw new InputKosongException(fieldName + " tidak boleh kosong.");
        }
    }

    public static void requirePositive(double value, String fieldName) throws DataTidakValidException {
        if (value <= 0) {
            throw new DataTidakValidException(fieldName + " harus lebih dari 0.");
        }
    }

    public static void requireNonNegative(int value, String fieldName) throws DataTidakValidException {
        if (value < 0) {
            throw new DataTidakValidException(fieldName + " tidak boleh negatif.");
        }
    }
}
