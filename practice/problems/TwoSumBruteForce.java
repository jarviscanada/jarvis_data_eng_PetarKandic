/**
 * @author Petar Kandic
 * We calculate the Two Sum problem using brute force.
 * Problem URL: https://www.notion.so/jarvisdev/Two-Sum-a1e0f073ee0a4c668d68b503b18eab93
 */
public class TwoSumBruteForce
{
    /**
     * The time complexity is O(n^2), as there is a nested for-loop.
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSumBruteForce(int[] nums, int target)
    {
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++)
        {
            for (int j = i + 1; j < nums.length; j++)
            {
                if (nums[i] + nums[j] == target)
                {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        throw new IllegalArgumentException("No two elements add up to the target.");
    }

    public static void main(String[] args)
    {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSumBruteForce(nums, target);
        System.out.println("Indices of the two numbers that add up to the target:");
        System.out.println("Index 1: " + result[0]);
        System.out.println("Index 2: " + result[1]);
    }
}



