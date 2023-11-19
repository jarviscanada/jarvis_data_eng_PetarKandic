const theArrOne = [1,2,2,3,3,3,4,4,4,4]
console.log(mostCommonCount(theArrOne))
function mostCommonCount(arr) {
    let freqMap = {};

    arr.forEach(element => {
        if (freqMap[element]) {
            freqMap[element]++
        } else {
            freqMap[element] = 1
        }
    });

    let maxFreq = 0;
    for (let key in freqMap)
    {
        if (maxFreq < freqMap[key])
        {
            maxFreq = freqMap[key];
        }
    }
    return maxFreq;
}