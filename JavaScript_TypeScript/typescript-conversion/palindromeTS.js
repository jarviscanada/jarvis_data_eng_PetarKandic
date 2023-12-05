var checkOne = "level";
console.log(checkPalindrome(checkOne));
function checkPalindrome(str) {
    var firstPoint = 0;
    var secondPoint = str.length - 1;
    while (firstPoint <= secondPoint) {
        if (str.charAt(firstPoint) !== str.charAt(secondPoint)) {
            return false;
        }
        firstPoint++;
        secondPoint--;
    }
    return true;
}
