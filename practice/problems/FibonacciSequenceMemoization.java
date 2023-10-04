/** 
 * @author Petar Kandic
 * We calculate the Fibonnaci sequence two ways. First, we use recursion. Then, we use memoization.
 * We see that the latter method is faster and more efficient in terms of memory use.
 * Problem URL: https://www.notion.so/jarvisdev/How-to-compare-two-maps-3f85419e5e064c638974ae712e47e210
 */
public class FibonacciSequenceMemoization
{
	
       /**
        * memoArray stores previously calculated Fibonacci values.
        */
	private static long[] memoArray;

       /**
        * This function records previously calculated results in MemoArray.
        * This removes the need for us to calculate every single step in the sequence.
        * @param position
        * @return the value at position
        */
    public static long fibonacciMemoization(int position)
    {
        memoArray = new long[position + 1];
        for (int i = 0; i <= position; i++)
        {
            memoArray[i] = -1;
        }

        memoArray[0] = 0;
        memoArray[1] = 1;

        return calculateFibonacciMemoization(position);
    }

    /**
     * We use a form of recursion here.
     * We check if the value at position has been calculated.
     * If so, we return it.
     * There will be fewer recursive calls than with pure recursion.
     * The time complexity is O(n). memoArray has a size of n+1, and we compute the fibonacci value once,
     * for each value between 2 to n.
     * @param position
     * @return the value at position
     */
    private static long calculateFibonacciMemoization(int position)
    {
        if (memoArray[position] != -1)
        {
            return memoArray[position];
        }

        memoArray[position] = calculateFibonacciMemoization(position - 1) + calculateFibonacciMemoization(position - 2);
        return memoArray[position];
    }

    public static void main(String[] args)
    {
        int position = 4;
        long result = fibonacciMemoization(position);
        System.out.println("Fibonacci(" + position + ") = " + result);
    }
}
