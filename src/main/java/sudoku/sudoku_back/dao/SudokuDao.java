package sudoku.sudoku_back.dao;

import sudoku.sudoku_back.model.SudokuModel;

import java.util.List;

public interface SudokuDao {
    List<SudokuModel> getSudoku();
}
