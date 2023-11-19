let doubArr = [1,2,3,4,5,6,7,8];
console.log(findMedian(doubArr));

function findMedian(arr)
{
    arr.sort();
    if (arr.length % 2 !== 0)
    {
        return arr[Math.floor(arr.length/2)];
    }
    else
    {
        return (arr[Math.floor(arr.length/2)-1] + arr[arr.length/2])/2
    }
}