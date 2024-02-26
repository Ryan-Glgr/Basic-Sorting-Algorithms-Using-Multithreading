public class DriverThread extends Thread {

    public int seed;
    public static final int N = 50000;
    public int[] sortedNums;
    public long executionTime;
    public static int[] numsToSort = new int[N];

    public DriverThread(int s) {
        seed = s;
    }

    @Override
    public void run() {
        long sTime = System.currentTimeMillis();
        sort();
        executionTime = System.currentTimeMillis() - sTime;
    }

    public void sort() {

        //make a deep copy of myNum every single time, and sort THAT one so that we don't get the fighting over the references
        int[] copyOfNumsToSort = new int[numsToSort.length];
        System.arraycopy(numsToSort, 0, copyOfNumsToSort, 0, numsToSort.length);

        switch (seed) {
            case 0 -> sortedNums = selectionSort(copyOfNumsToSort);
            case 1 -> sortedNums = insertionSort(copyOfNumsToSort);
            case 2 -> sortedNums = bubbleSort(copyOfNumsToSort);
            case 3 -> sortedNums = mergeSort(copyOfNumsToSort, 0, copyOfNumsToSort.length - 1);
            case 4 -> sortedNums = quickSort(copyOfNumsToSort, 0, copyOfNumsToSort.length-1);
            case 5 -> sortedNums = heapSort(copyOfNumsToSort);
            case 6 -> sortedNums = ryanSort(copyOfNumsToSort);
            case 7 -> sortedNums = countingSort(copyOfNumsToSort);
            default -> System.out.println("MASSIVE PROBLEM HAS OCCURRED");
        }
    }

    public static int[] mergeSort(int[] nums, int start, int end) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            mergeSort(nums, start, mid);//left sub-array
            mergeSort(nums, mid + 1, end);//right sub-array
            merge(nums, start, mid, end);
        }
        return nums;
    }

    public static void merge(int[] nums, int start, int mid, int end) {
        int[] left = new int[mid - start + 1];
        int[] right = new int[end - mid];
        //copy left
        System.arraycopy(nums, start, left, 0, left.length);
        //copy right
        System.arraycopy(nums, (mid + 1), right, 0, right.length);
        int l = 0, r = 0;
        while (l < left.length && r < right.length) {
            if (left[l] < right[r]) {
                nums[start] = left[l];
                l++;
            } else {
                nums[start] = right[r];
                r++;
            }
            start++;
        }
        while (l < left.length) {
            nums[start] = left[l];
            start++;
            l++;
        }
        while (r < right.length) {
            nums[start] = right[r];
            r++;
            start++;
        }
    }

    public static int[] selectionSort(int[] n) {
        for (int i = 0; i < n.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < n.length; j++) {
                if (n[j] < n[minPos]) minPos = j;
            }
            if (minPos != i) swap(n, minPos, i);
        }
        return n;
    }

    public static int[] insertionSort(int[] n) {
        for (int i = 1; i < n.length; i++) {
            int key = n[i];
            int ii = i - 1;
            while (ii >= 0 && n[ii] > key) {
                swap(n, ii + 1, ii);
                ii--;
            }
            n[ii + 1] = key;
        }//outer loop that is going to run all the way through
        return n;
    }

    public static int[] bubbleSort(int[] n) {
        int lastIndex = n.length - 1;
        while (lastIndex > 0) {
            int swapIndex = 0;
            for (int i = 0; i < lastIndex; i++) {
                if (n[i] > n[i + 1]) {
                    swap(n, i, i + 1);
                    swapIndex = i;
                }//swap if we get a number thats too big for its index
            }//we are going to run all the way through the index running our inner loop
            lastIndex = swapIndex;
        }
        return n;
    }

    public static void swap(int[] num, int n, int m) {
        int temp = num[n];
        num[n] = num[m];
        num[m] = temp;
    }

    public static int[] quickSort(int[] n, int start, int end) {
        if (start < end) {
            int mid = partition(n, start, end);
            quickSort(n, start, mid-1);
            quickSort(n, mid + 1, end);
        }
        return n;
    }

    public static int partition(int[] n, int start, int end) {
        int pivot = n[end];

        int j = start;
        for (int i = start; i < end; i++) {
            if (n[i] < pivot) {
                swap(n, i, j);
                j++;
            }
        }
        swap(n, j, end);
        return j;
    }

    public static int[] heapSort(int[] n){
        int[] keys = new int[n.length];
        int size = keys.length;

        //this builds our heap, we are just going to heapify the entire top half of the array, which in turn heapfies the whole thing
        for (int i = keys.length / 2 - 1; i >= 0; i--){
            heapify(n, i, size);
        }
        while(size > 0){
            swap(n, 0, size -1);
            size--;
            heapify(n,0, size);
        }
        return n;
    }
    public static void heapify(int[] keys, int index, int size){
        while(true){
            int l = 2 * index + 1;
            int r = 2 * index + 2;

            //our two break conditions, we have made it too far
            if (l >= size) break;
            if (r >= size) break;

            //just assume that l is bigger, but if r was bigger we swap that instead
            int largest = l;
            if(r < size && keys[r] > keys[l]) largest = r;

            if (keys[largest] <= keys[index]) break;

            swap(keys, largest, index);
            index = largest;
        }
    }

    //ryanSort is the destructive version of counting sort, it functions much the same, but just creates new integers to fill the return array with, rather than moving the entries from the input to a specific location
    public static int[] ryanSort(int[] n){
        //we are going to do counting sort ryans way and compare results
        //first step is just get the max
        int max = 0;
        for (int j : n) {
            if (j > max) max = j;
        }

        //now we make our temp array that is size max+1
        int[] countArray = new int[++max];
        for (int j : n) {
            countArray[j]++;
        }
        //now we've incremented all the positions 'i', that are represented in n.
        //so all we do, is add the number at index specified by temp array, the number of times of the value of that index in temp array to our return array
        int[] retArr = new int[n.length];
        int lastIndexFilled = 0;

        //make a run through count array, and decrement each index position to 0, while also adding that index position to the return array if it wasn't 0, that amount of times.
        for(int i = 0; i < countArray.length; i++){
            while(countArray[i] > 0){
                retArr[lastIndexFilled] = i;
                countArray[i]--;
                lastIndexFilled++;
            }
        }
        return retArr;
    }

    public static int[] countingSort(int[] n){
        //this is going to be pretty efficient since we know that our numbers are just between 0 and 100.
        int[] values = new int[100];
        //increment that value in our values count
        for (int j : n) values[j]++;
        for(int i = 1; i < values.length; i++) values[i] += values[i-1];
        for(int i = 0; i < values.length; i++) values[i]--;

        int[] retArr = new int[n.length];

        for(int i = n.length-1; i >= 0; i--){
            retArr[values[n[i]]] = n[i];
            values[n[i]]--;
        }
        return retArr;
    }

}

