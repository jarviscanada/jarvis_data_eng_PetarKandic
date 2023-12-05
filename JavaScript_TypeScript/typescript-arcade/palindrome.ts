function solution(s: string): string
{
    let palindrome: string;

    palindrome = "";
    let longestPalindrome = "";
    let firstTraverse = 0;
    let secondTraverse = 0;

    while (true)
    {
        for (let i = 0; i < s.length; i++)
        {
            palindrome += s.charAt(i);
            secondTraverse = palindrome.length-1;
            while (secondTraverse >= firstTraverse)
            {
                if (palindrome.charAt(firstTraverse) != palindrome.charAt(secondTraverse))
                {
                    palindrome = "";
                }
                secondTraverse--;
                firstTraverse++;
            }
            if (longestPalindrome.length <= palindrome.length)
            {
                longestPalindrome = palindrome;
            }
        }
        break;
    }


    return longestPalindrome;

}

console.log(solution("aaaacodedoc"))