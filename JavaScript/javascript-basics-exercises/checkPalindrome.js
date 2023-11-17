let checkOne = "levevelk";
console.log(checkPalindrome(checkOne))

function checkPalindrome(str)
{
    let firstPoint = 0;
    let secondPoint = str.length-1;

    while (firstPoint <= secondPoint)
    {
        if (str.charAt(firstPoint) !== str.charAt(secondPoint))
        {
            return false;
        }
        firstPoint++;
        secondPoint--;
    }
    return true;
}