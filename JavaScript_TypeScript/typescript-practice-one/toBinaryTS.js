var theInt = 6;
console.log(decimalToBinary(theInt));
function decimalToBinary(decimal) {
    if (decimal === 0) {
        return '0';
    }
    var binary = '';
    while (decimal > 0) {
        binary = (decimal % 2) + binary;
        decimal = Math.floor(decimal / 2);
    }
    return binary;
}
