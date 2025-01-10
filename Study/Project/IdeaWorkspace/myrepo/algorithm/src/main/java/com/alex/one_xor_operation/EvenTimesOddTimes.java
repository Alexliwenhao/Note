package com.alex.one_xor_operation;

/**
 * @auther: liwenhao
 * @Date: 2023/4/2 18:29
 * @Description:
 */
public class EvenTimesOddTimes {
    public static void main(String[] args) {

        // eor != 0
        // eor最右侧的1，提取出来
        // eor :     00110010110111000
        // rightOne :00000000000001000
//        int rightOne = eor & (-eor); // 提取出最右的1

        int[] arr1 = { 3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1 };
        printOddTimesNum1(arr1);

        int[] arr2 = { 4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2 };
        int[] arr3 = { 3,3,3,1,1,1,2,2,4,4 };
        System.out.println("arr3 = " + (3^1) +":"+ ((3^1) & (-(3^1))));
        printOddTimesNum2(arr3);

    }

    // arr中，只有一种数，出现奇数次
    private static void printOddTimesNum1(int[] arr1) {
        int eor = 0;
        for (int i = 0; i < arr1.length; i++) {
            eor ^= arr1[i];
        }
        System.out.println("eor = " + eor);
    }

    // arr中，有两种数，出现奇数次
    private static void printOddTimesNum2(int[] arr2) {
        int eor = 0;

        for (int i = 0; i < arr2.length; i++) {
            eor ^= arr2[i];
        }

        int lastOne = eor & (-eor);

        int onlyOne = 0;

        for (int i = 0; i < arr2.length; i++) {
            if ((lastOne & arr2[i]) != 0) {
                onlyOne ^= arr2[i];
            }
        }

        System.out.println("onlyOne = " + onlyOne);
        int b = onlyOne ^ eor;
        System.out.println("b = " + b);


    }
}
