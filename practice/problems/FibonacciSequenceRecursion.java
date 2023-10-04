public class FibonacciSequenceRecursion
{

    /**
     * This method calculates the Fibonacci Sequence using pure recursion.
     * It finds the value at the given position.
     * This is not very efficient or fast.
     * The time complexity is O(2^N), as there are two recursive calls.
     * @param position
     * @return the value at position
     */
    public static int calculateFibonacciRecursion(int position)
    {
        if (position == 0)
        {
            return 0;
        }
        else if (position == 1)
        {
            return 1;
        }
        else
        {
            return calculateFibonacciRecursion(position-1)+calculateFibonacciRecursion(position-2);
        }
    }
}
