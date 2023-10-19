import java.util.Scanner;

public class sum_of_array
{
    /**
     * The main method handles the game itself.
     * An array of length x is generated.
     * We initiate a Scanner object. This will recieve the mediator's number, and the player's guesses.
     * We begin a loop, which will continue until the player wins.
     * If the mediator's number - n - is in the array, the first and last numbers will be swapped.
     * The player guesses the sum of the array. If they are correct, they get a point.
     * The player who gets to the target score first wins.
     * @param args
     */
    public static void main(String[] args)
    {
        int[] array = generateArray(3);
        int arrLength = array.length;

        int currentScore = 0;
        int targetScore = 4;
        Scanner scanner = new Scanner(System.in);

        System.out.println("The game begins! Mediator, present your number!");
        while (currentScore < targetScore)
        {
            int n = scanner.nextInt();
            boolean found = false;
            for (int i = 0; i < arrLength; i++)
            {
                if (array[i] == n)
                {
                    int temp = array[0];
                    array[0] = array[arrLength - 1];
                    array[arrLength - 1] = temp;
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                array[arrLength-1] = n;
            }
            int sum = calculateSum(array);
            System.out.println("Time to guess! Don't dissapoint us...");
            int playerGuess = scanner.nextInt();
            if (sum == playerGuess)
            {
                currentScore++;
                System.out.println("You scored a point! Current Score: " + currentScore);
            }
        }
        System.out.println("Congratulations! The locals cheer for you!");
        scanner.close();
    }

    /**
     * This function generates an array of length len.
     * @param len
     * @return an array of [1,2,...n]
     */
    private static int [] generateArray(int len)
    {
        int [] newArr = new int[len];
        for (int i = 0; i < len; i++)
        {
            newArr[i] = i + 1;
        }
        return newArr;
    }

    /**
     * Helper function to calculate the sum of the array.
     * @param array
     * @return the sum of the array.
     */
    private static int calculateSum(int[] array)
    {
        int sum = 0;
        for (int value : array) 
        {
            sum += value;
        }
        return sum;
    }
}
