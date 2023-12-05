var theArr = [1, 2, 3, 4];
console.log(maxValArr(theArr));
function maxValArr(int) {
    var max = 0;
    for (var i = 0; i < int.length; i++) {
        if (max < int[i]) {
            max = int[i];
        }
    }
    return max;
}
