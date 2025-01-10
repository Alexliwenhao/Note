package com.alex.quicksort;

/**
 * @author liwenhao
 * @date 2023/4/28 16:41
 */
public class QuickSort {

    public static void main(String[] args) {

    }

    public int[] netherlandsFlag(int[] arr, int L, int R) {
        if (L > R) {
            return new int[]{-1, -1};
        }
        if (L == R) {
            return new int[]{L, R};
        }
        int less = L - 1;
        int more = R;
        int index = L;
        while (index < more) {
            if (arr[index] == arr[R]) {
                index++;
            } else if (arr[index] < arr[R]) {
                swap(arr, ++less, index++);
            } else {
                swap(arr, --more, index++);
            }
        }
        swap(arr, more, R);
        return new int[]{less + 1, more};
    }

    private void swap(int[] arr, int i, int i1) {
        int temp = arr[i];
        arr[i] = arr[i1];
        arr[i1] = temp;
    }
}
