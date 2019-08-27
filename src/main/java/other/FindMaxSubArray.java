package other;

import java.util.Arrays;
import static java.util.Objects.requireNonNull;

public class FindMaxSubArray {

    private static int[] array = {
            13, -3, -25, 20,
            -3, -16, -23, 18,
            20, -7, 12, -5,
            -22, 15, -4, 7
    };

    public static void main(String[] args) {
        System.out.println(Arrays.toString(findMaxSubArray(array)));
        System.out.println(Arrays.toString(findMaxSubArraySequence(array)));
    }

    public static int[] findMaxSubArraySequence(int[] array) {
        int sum = array[0];
        int startIndex = 0;
        int endIndex = 0;

        for (int i = 0; i < array.length; i++) {
            int intermediateVal = array[i];
            if (intermediateVal > sum) {
                sum = intermediateVal;
                startIndex = i;
                endIndex = i;
            }
            for (int j = i + 1; j < array.length; j++) {
                intermediateVal += array[j];
                if (intermediateVal > sum) {
                    sum = intermediateVal;
                    startIndex = i;
                    endIndex = j;
                }
            }
        }

        return Arrays.copyOfRange(array, startIndex, endIndex + 1);
    }

    public static int[] findMaxSubArray(int[] array) {
        if (array.length == 1) {
            return array;
        } else {
            int mid = (int) Math.ceil((double) array.length / 2);
            int[] maxSubArrayLeft = findMaxSubArray(Arrays.copyOfRange(array, 0, mid));
            int[] maxSubArrayRight = findMaxSubArray(Arrays.copyOfRange(array, mid, array.length));
            int[] maxCrossingSubArray = findCrossingSubArray(array, mid);

            int maxValueLeft = sumArray(requireNonNull(maxSubArrayLeft));
            int maxValueRight = sumArray(requireNonNull(maxSubArrayRight));
            int maxValueCrossing = sumArray(requireNonNull(maxCrossingSubArray));

            if (maxValueLeft >= maxValueRight & maxValueLeft >= maxValueCrossing) {
                return maxSubArrayLeft;
            } else if (maxValueRight >= maxValueLeft & maxValueRight >= maxValueCrossing) {
                return maxSubArrayRight;
            } else {
                return maxCrossingSubArray;
            }

        }
    }

    private static int[] findCrossingSubArray(int[] array, int mid) {
        int leftSum = array[mid - 1];
        int leftSumTmp = array[mid - 1];
        int leftBorder = mid - 1;
        for (int i = mid - 2; i >= 0; i--) {
            leftSumTmp += array[i];
            if (leftSumTmp > leftSum) {
                leftSum = leftSumTmp;
                leftBorder = i;
            }
        }

        int rightSum = array[mid];
        int rightSumTmp = array[mid];
        int rightBorder = mid;

        for (int i = mid + 1; i < array.length; i++) {
            rightSumTmp += array[i];
            if (rightSumTmp > rightSum) {
                rightSum = rightSumTmp;
                rightBorder = i;
            }
        }

        int sum = leftSum + rightSum;

        if (leftSum >= rightSum & leftSum >= sum) {
            return Arrays.copyOfRange(array, leftBorder, leftBorder + 1);
        } else if (rightSum >= leftSum & rightSum >= sum) {
            return Arrays.copyOfRange(array, rightBorder, rightBorder + 1);
        } else {
            return Arrays.copyOfRange(array, leftBorder, rightBorder + 1);
        }
    }

    private static int sumArray(int[] array) {
        int sum = array.length == 0 ? 0 : array[0];
        for (int i = 1; i < array.length; i++) {
            sum += array[i];
        }

        return sum;
    }
}
