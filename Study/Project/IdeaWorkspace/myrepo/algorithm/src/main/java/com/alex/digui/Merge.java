package com.alex.digui;

/**
 * TODO
 *
 * @author lwh
 * @date 2023/4/12 16:44
 * @copyright 成都精灵云科技有限公司
 */
public class Merge {
    public static void main(String[] args) {

    }

    public static void process(int[] arr, int L, int R) {
        if (L == R) {
            return;
        }
        int mid = L + ((R - L) >> 1);
        process(arr, L, mid);
        process(arr, mid + 1, R);
        merge(arr, L, mid, R);

    }

    public static void process2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int mergeSize = 1;
        int N = arr.length;
        int l = 0;
        while (mergeSize < N) {
            int m = l + mergeSize - 1;
            int r = Math.min(m + mergeSize, N);
            merge(arr, l, m, r);

            if (mergeSize > N/2) {
                break;
            }
            mergeSize <<= 1;
            l = r +1;
        }
    }

    private static void merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[l - r + 1];
        int p1 = l;
        int p2 = mid + 1;
        int i = 0;
        while (p1 <= mid && p2 <= r) {
            help[i++] = arr[p1] > arr[p2] ? arr[p2++] : arr[p1++];
        }

        while (p1 <= mid) {
            help[i++] = arr[p1++];
        }

        while (p2 <= r) {
            help[i++] = arr[p2++];
        }

        for (int i1 = 0; i1 < help.length; i1++) {
            arr[l + i1] = help[i1];
        }

    }
}
 