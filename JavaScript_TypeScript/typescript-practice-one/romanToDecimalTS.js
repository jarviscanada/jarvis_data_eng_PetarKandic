var romanNum = "III";
console.log(romanToDecimal(romanNum));
function romanToDecimal(roman) {
    var romanNumerals = {
        'I': 1,
        'V': 5,
        'X': 10,
        'L': 50,
        'C': 100,
        'D': 500,
        'M': 1000
    };
    var total = 0;
    var previousValue = 0;
    for (var i = roman.length - 1; i >= 0; i--) {
        var currentValue = romanNumerals[roman[i]];
        if (currentValue < previousValue) {
            total -= currentValue;
        }
        else {
            total += currentValue;
        }
        previousValue = currentValue;
    }
    return total;
}
