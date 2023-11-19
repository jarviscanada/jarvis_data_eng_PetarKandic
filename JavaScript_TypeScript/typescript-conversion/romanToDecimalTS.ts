let romanNum = "III";
console.log(romanToDecimal(romanNum));

function romanToDecimal(roman)
{
    const romanNumerals =
        {
            'I': 1,
            'V': 5,
            'X': 10,
            'L': 50,
            'C': 100,
            'D': 500,
            'M': 1000
        };

    let total = 0;
    let previousValue = 0;

    for (let i = roman.length - 1; i >= 0; i--)
    {
        const currentValue = romanNumerals[roman[i]];

        if (currentValue < previousValue)
        {
            total -= currentValue;
        }
        else
        {
            total += currentValue;
        }

        previousValue = currentValue;
    }

    return total;
}