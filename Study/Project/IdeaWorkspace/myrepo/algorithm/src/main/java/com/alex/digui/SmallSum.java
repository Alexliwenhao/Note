package com.alex.digui;

/**
 * TODO
 *
 * @author LWH
 * @date 2023/4/15 14:49
 * @copyright 成都精灵云科技有限公司
 */
public class SmallSum {
    public static void main(String[] args) {

    }

    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int m = l + ((r - l) >> 1);
        return process(arr, l, m) +
                process(arr, m + 1, r) +
                merge(arr, l, m, r);

    }

    private static int merge(int[] arr, int l, int m, int r) {
        int p1 = l;
        int p2 = m + 1;
        int ans = 0;
        int[] help = new int[r - l + 1];
        int i = 0;
        while (p1 <= m && p2 <= r) {
            ans += (arr[p1] < arr[p2] ? (r - p2 + 1) * arr[p1] : 0);
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (int i1 = 0; i1 < help.length; i1++) {
            arr[l + i] = help[i];
        }
        return ans;
    }
}
 