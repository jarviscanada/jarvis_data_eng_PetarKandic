const theArrUuuu = [1, -2, 4, -5, 1];
console.log(countNegSubbarrs(theArrUuuu))

function countNegSubbarrs(arr)
{
    let count = 0;
    for (let i = 0; i < arr.length; i++)
    {
        let sum = 0;
        for (let end = i; end < arr.length; end++)
        {
            sum += arr[end];
            if (sum < 0)
            {
                count++
            }
        }
    }
    return count;
}