var fibIndex = 5;
console.log(findFibonacci(fibIndex));
function findFibonacci(index) {
    if (index === 1) {
        return 0;
    }
    if (index === 2) {
        return 1;
    }
    else {
        return findFibonacci(index - 1) + findFibonacci(index - 2);
    }
}
