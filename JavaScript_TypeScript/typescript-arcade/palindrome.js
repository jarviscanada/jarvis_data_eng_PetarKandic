function solution(s) {
    var palindrome;
    palindrome = "";
    var longestPalindrome = "";
    var firstTraverse = 0;
    var secondTraverse = 0;
    while (true) {
        for (var i = 0; i < s.length; i++) {
            palindrome += s.charAt(i);
            secondTraverse = palindrome.length;
            while (secondTraverse >= firstTraverse) {
                if (palindrome.charAt(firstTraverse) != palindrome.charAt(secondTraverse)) {
                    palindrome = "";
                }
                secondTraverse--;
                firstTraverse++;
            }
            if (longestPalindrome.length <= palindrome.length) {
                longestPalindrome = palindrome;
            }
        }
        break;
    }
    return longestPalindrome;
}
console.log(solution("aaaacodedoc"));
