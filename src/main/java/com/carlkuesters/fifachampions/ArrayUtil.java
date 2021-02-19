package com.carlkuesters.fifachampions;

public class ArrayUtil {

    public static <T> void swap(T[] array, int index1, int index2) {
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }

    public static <T> void swap(T[] array1, int index1, T[] array2, int index2) {
        T tmp = array1[index1];
        array1[index1] = array2[index2];
        array2[index2] = tmp;
    }
}
