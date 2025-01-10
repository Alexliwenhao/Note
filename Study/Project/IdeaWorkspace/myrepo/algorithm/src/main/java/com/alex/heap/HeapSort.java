package com.alex.heap;

/**
 * @author liwenhao
 * @date 2023/5/5 16:28
 */
public class HeapSort {

    public static void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = arr.length - 1; i >= 0; i++) {
            heapify(arr, i, arr.length - 1);
        }


    }

    private static void heapify(int[] arr, int index, int heapSize) {
        int left = (index + 1) / 2;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, index, largest);
            index = largest;
            left = (index + 1) /2;
        }
    }

    public static void swap(int[] arr, int L, int R) {
        int temp = arr[L];
        arr[L] = arr[R];
        arr[R] = temp;
    }
}
