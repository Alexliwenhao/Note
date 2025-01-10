package com.alex.heap;

/**
 * @auther: liwenhao
 * @Date: 2023/4/25 23:09
 * @Description:
 */
public class Heap {
    class MyMaxHeap{
        private int[] heap;
        private final int limit;
        private int heapSize;

        MyMaxHeap(int limit) {
            heap = new int[limit];
            this.limit = limit;
            heapSize = 0;
        }
        public void push (int value){
            if (heapSize == limit) {
                throw new RuntimeException("heap is full");
            }
            heap[heapSize] = value;
            heapInsert(heap, heapSize++);
        }

        //上浮
        private void heapInsert(int[] heap, int index) {
            while (heap[index] > heap[(index - 1) / 2]) {
                swap(heap, index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

       public int pop(){
            int top = heap[0];
            swap(heap, 0, heapSize--);
            heapify(heap, 0, heapSize);
            return top;
       }

        public void swap(int[] heap, int l, int r) {
            int temp = heap[l];
            heap[l] = heap[r];
            heap[r] = temp;
        }

        //下沉
        private void heapify(int[] heap, int index, int heapSize) {
            int left = 2 * index + 1;
            while (left < heapSize) {
                int largest = left + 1 < heapSize && heap[left + 1 ] > heap[left] ? left+1:left;
                //最大节点等于当前数
                if (largest == index) {
                    break;
                }
                swap(heap, index, largest);
                index = largest;
                left  = 2 * index + 1;
            }
        }
    }




}
