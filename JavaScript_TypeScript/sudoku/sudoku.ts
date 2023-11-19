type Board = string[][];

function solveSudoku(board: Board): void {
    solve(board);
}

function solve(board: Board): boolean {
    for (let row = 0; row < 9; row++) {
        for (let col = 0; col < 9; col++) {
            if (board[row][col] === '.') {
                for (let num = 1; num <= 9; num++) {
                    const char = num.toString();
                    if (isValid(board, row, col, char)) {
                        board[row][col] = char;

                        if (solve(board)) {
                            return true;
                        }

                        board[row][col] = '.';
                    }
                }
                return false;
            }
        }
    }
    return true;
}

function isValid(board: Board, row: number, col: number, char: string): boolean {
    for (let i = 0; i < 9; i++) {
        if (board[i][col] === char || board[row][i] === char || board[3 * Math.floor(row / 3) + Math.floor(i / 3)][3 * Math.floor(col / 3) + i % 3] === char) {
            return false;
        }
    }
    return true;
}

// Example usage
const board: Board = [
    ["5", "3", ".", ".", "7", ".", ".", ".", "."],
    ["6", ".", ".", "1", "9", "5", ".", ".", "."],
    [".", "9", "8", ".", ".", ".", ".", "6", "."],
    ["8", ".", ".", ".", "6", ".", ".", ".", "3"],
    ["4", ".", ".", "8", ".", "3", ".", ".", "1"],
    ["7", ".", ".", ".", "2", ".", ".", ".", "6"],
    [".", "6", ".", ".", ".", ".", "2", "8", "."],
    [".", ".", ".", "4", "1", "9", ".", ".", "5"],
    [".", ".", ".", ".", "8", ".", ".", "7", "9"]
];

solveSudoku(board);
console.log(board);