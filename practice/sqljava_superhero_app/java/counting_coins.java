import java.util.Arrays;

public class counting_coins
{
    /**
     * Given an amount, and the array of valid coins, return the *fewest* number of coins needed to reach the amount.
     * Dynamic programming is used. We create an array of size amount+1.
     * Initially, each element is set to 0 for the first element, and amount+1 for the others.
     * We search the list of coins, and if we find a coin which is equal to or smaller than amount, it is added to the array.
     * @param coinArray
     * @param amount
     * @return -1 if an applicable amount found, the amount if otherwise.
     */
    public static int findMinCoinAmount(int[] coinArray, int amount)
    {
        int[] minCoinArray = new int[amount + 1];
        Arrays.fill(minCoinArray, amount + 1);
        minCoinArray[0] = 0;
        for (int i = 1; i <= amount; i++)
        {
            for (int coin : coinArray)
            {
                if (i >= coin)
                {
                    minCoinArray[i] = Math.min(minCoinArray[i], minCoinArray[i - coin] + 1);
                }
            }
        }

        return minCoinArray[amount] > amount ? -1 : minCoinArray[amount];
    }

    /**
     * The main method.
     * We create an array of coins, and an amount.
     * These values are passed to minCoins.
     * If minCoins returns -1, we have a valid number of minimum coins.
     * Otherwise, we can not for a valid set of coins
     * @param args
     */
    public static void main(String[] args)
    {
        int[] cointArray = {1, 7, 22};
        int amount = 3;
        int minCoins = findMinCoinAmount(cointArray, amount);

        if (minCoins != -1)
        {
            System.out.println("The minimum number of coins needed to make " + amount + " is: " + minCoins);
        }
        else
        {
            System.out.println("We can't make " + amount + " with the given coins!");
        }
    }
}
