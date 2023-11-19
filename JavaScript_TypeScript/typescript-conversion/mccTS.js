var theArrOne = [1, 2, 2, 3, 3, 3, 4, 4, 4, 4];
console.log(mostCommonCount(theArrOne));
function mostCommonCount(arr) {
    var freqMap = {};
    arr.forEach(function (element) {
        if (freqMap[element]) {
            freqMap[element]++;
        }
        else {
            freqMap[element] = 1;
        }
    });
    var maxFreq = 0;
    for (var key in freqMap) {
        if (maxFreq < freqMap[key]) {
            maxFreq = freqMap[key];
        }
    }
    return maxFreq;
}
