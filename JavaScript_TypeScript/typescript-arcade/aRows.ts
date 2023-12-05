function solution(a: number[]): number[]
{
    let firstWeight = 0;
    let secondWeight = 0;

    let finalWeight = [];

    for (let i in a)
    {
        if (Number(i) % 2 === 0)
        {
            firstWeight = a[i];
        }
        else
        {
            secondWeight = a[i];
        }
    }

    finalWeight.push(firstWeight);
    finalWeight.push(secondWeight);

    return finalWeight;
}

console.log(solution([50, 60, 60, 45, 70]))