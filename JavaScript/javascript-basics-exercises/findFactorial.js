let theNum = 5;
console.log(findFactorial(theNum))

function findFactorial(theInt)
{
    if (theInt === 1)
    {
        return 1;
    }
    else
    {
        return findFactorial(theInt-1) * theInt;
    }
}