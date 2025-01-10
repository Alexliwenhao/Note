package com.alex.stack;

import java.util.Stack;

/**
 * TODO
 *
 * @author lwh
 * @date 2023/4/12 15:35
 * @copyright 成都精灵云科技有限公司
 */
public class findMin {
    static Stack<Integer> nums = new Stack<Integer>();
    static Stack<Integer> min = new Stack<Integer>();
    public static void main(String[] args) {

    }

    public static Integer pop(){
        if (!nums.empty()) {
            min.pop();
            return nums.pop();
        }
        return -1;
    }
    public static void push(Integer i) {
        nums.push(i);
        if (!min.empty()) {
            min.push(Math.min(min.peek(), i));
        }
    }


}
 