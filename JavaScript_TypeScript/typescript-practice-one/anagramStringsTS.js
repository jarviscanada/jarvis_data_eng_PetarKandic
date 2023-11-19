var stringOne = "whom";
var stringTwo = "whom";
console.log(anagramString(stringOne, stringTwo));
function anagramString(strOne, strTwo) {
    var norm = function (str) { return str.toLowerCase().replace(/[^a-z\d]/g, '').split('').sort().join(''); };
    return norm(strOne) === norm(strTwo);
}
