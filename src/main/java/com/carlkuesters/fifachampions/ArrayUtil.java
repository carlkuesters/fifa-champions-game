package com.carlkuesters.fifachampions;

import java.util.Objects;

public class ArrayUtil {

    public static <T> void swap(T[] array, int index1, int index2) {
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }

    public static <T> void swap(T[] array1, T object1, T[] array2, T object2) {
        swap(array1, getIndex(array1, object1), array2, getIndex(array2, object2));
    }

    public static <T> int getIndex(T[] array, T object) {
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], object)) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    public static <T> void swap(T[] array1, int index1, T[] array2, int index2) {
        T tmp = array1[index1];
        array1[index1] = array2[index2];
        array2[index2] = tmp;
    }
}
