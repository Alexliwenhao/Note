package com.alex.one_xor_operation;


public class KM {
    public static void main(String[] args) {
        int[] arr = { 3,3,3,2,2,2,2,1,1,1,1 };
        int k = 3;
        int m = 4;
        findNum(arr, k, m);
    }




    private static void findNum(int[] arr, int k, int m) {
        int[] count = new int[31];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < count.length; j++) {
                count[j]+=((arr[i]>>j) & 1);
            }
        }
        int ans = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] % m != 0) {
                ans |=(1<<i);
            }
        }

        System.out.println("ans = " + ans);

    }
}
 