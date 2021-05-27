package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sudoku.sudoku_back.mapper.SudokuMapper;

@Service
public class SudokuService {
    @Autowired
    SudokuMapper sudokuMapper;

    public int getSudoku() {
        return sudokuMapper.getSudoku();
    }
}
