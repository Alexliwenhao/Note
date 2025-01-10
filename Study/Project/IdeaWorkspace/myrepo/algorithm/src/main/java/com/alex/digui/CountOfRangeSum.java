package com.alex.digui;

/**
 * TODO
 *
 * @author lwh
 * @date 2023/4/17 20:54
 * @copyright 成都精灵云科技有限公司
 */
public class CountOfRangeSum {
    public static void main(int[] arr) {
        int[] preSum = new int[arr.length];
        int i = 1;
        preSum[0] = arr[0];
        while (i < arr.length){
            preSum[i] = arr[i] + preSum[i-1];
            i++;
        }
        int lower = 10;
        int upper = 20;
        process(arr, 0, preSum.length - 1, lower, upper);
    }

    public static int process(int[] arr, int l, int r, int lower, int upper) {
        if (l==r) {
            return (arr[l] >= lower && arr[l] <= upper) ? 1 : 0 ;
        }
        int mid = l + (r-l)>>1;

        return process(arr, l, mid, lower, upper) +
                process(arr, mid + 1, r,  lower, upper) +
                merge(arr, l ,mid, r, lower, upper);

    }

    private static int merge(int[] arr, int l, int mid, int r, int lower, int upper) {
        int winL = l,winR = l;
        int ans = 0;
        for (int i = mid+1; i <=r ; i++) {
            int min = arr[i] - upper;
            int max = arr[i] - lower;
            while (winL < min) {
                winL++;
            }
            while (winR <= max) {
                winR++;
            }
            ans+=winR-winL;
        }

        return ans;
    }
}
 