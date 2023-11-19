function solution(a) {
    var firstWeight = 0;
    var secondWeight = 0;
    var finalWeight = [];
    for (var i in a) {
        if (Number(i) % 2 === 0) {
            firstWeight = a[i];
        }
        else {
            secondWeight = a[i];
        }
    }
    finalWeight.push(firstWeight);
    finalWeight.push(secondWeight);
    return finalWeight;
}
console.log(solution([50, 60, 60, 45, 70]));
