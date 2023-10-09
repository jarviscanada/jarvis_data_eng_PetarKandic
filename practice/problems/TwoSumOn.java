import java.util.Arrays;

/**
 * @author Petar Kandic
 * We calculate the Two Sum problem using Sorting.
 */
public class TwoSumSorting
{
    /**
     * The time complexity is O(nlogn), due to the sorting algorithm.
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] sortedNums = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sortedNums);
        int left = 0;
        int right = sortedNums.length - 1;
        while (left < right) {
            int sum = sortedNums[left] + sortedNums[right];
            if (sum == target)
            {
                int index1 = indexOf(nums, sortedNums[left]);
                int index2 = indexOf(nums, sortedNums[right]);
                return new int[]{index1, index2};
            }
            else if (sum < target)
            {
                left++;
            }
            else
            {
                right--;
            }
        }

        throw new IllegalArgumentException("No two elements add up to the target.");
    }

    private static int indexOf(int[] array, int target)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == target)
            {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args)
    {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(nums, target);

        System.out.println("Indices of the two numbers that add up to the target:");
        System.out.println("Index 1: " + result[0]);
        System.out.println("Index 2: " + result[1]);
    }
}

