package com.alex.digui;

/**
 * TODO
 *
 * @author lwh
 * @date 2023/4/15 15:08
 * @copyright 成都精灵云科技有限公司
 */
public class ReversePair {
    public static void main(int[] args) {
        if (args == null || args.length < 2) {
            return;
        }

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
        int p1 = m;
        int p2 = r;
        int[] help = new int[r - l + 1];
        int i = help.length - 1, ans = 0;

        while (p1 >= l && p2 >= m + 1) {
            ans += (arr[p2] < arr[p1] ? (p2 - m) : 0);
            help[i--] = arr[p1] > arr[p2] ? arr[p1--] : arr[p2--];
        }
        while (p1 >= l) {
            help[i--] = arr[p1--];
        }
        while (p2 >= m + 1) {
            help[i--] = arr[p2--];
        }
        for (int j = 0; j < help.length; j++) {
            arr[l + j] = help[j];
        }
        return ans;
    }

}
 