let stringOne = "whom";
let stringTwo = "whom";
console.log(anagramString(stringOne, stringTwo));

function anagramString(strOne, strTwo)
{
    const norm = str =>  str.toLowerCase().replace(/[^a-z\d]/g, '').split('').sort().join('');
    return norm(strOne) === norm(strTwo);
}