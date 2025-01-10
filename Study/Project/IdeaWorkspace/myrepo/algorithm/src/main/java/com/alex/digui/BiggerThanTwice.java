package com.alex.digui;

/**
 * TODO
 *
 * @author lwh
 * @date 2023/4/15 15:39
 * @copyright 成都精灵云科技有限公司
 */
public class BiggerThanTwice {
    public static void main(String[] args) {

    }

    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int mid = l + (r-l)>>1;
        return process(arr, l, mid) +
                process(arr, mid+1, r) +
                merge(arr, l, mid, r);
    }

    private static int merge(int[] arr, int l, int mid, int r) {
        int p1 = l;
        int p2 = mid+1;
        int winR = mid+1;
        int i = 0;
        int[] help = new int[r - l + 1];
        int ans = 0;
        for (int j = l; j<=mid; j++) {
            while (winR <=r && arr[j] > 2 * arr[winR++]){}
            ans+=winR-mid-1;
        }

        while (p1 <= mid && p2 <= r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (int j = 0; j < help.length; j++) {
            arr[l+j] = help[j];
        }
        return ans;
    }
}
 