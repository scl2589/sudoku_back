package sudoku.sudoku_back.service;

import sudoku.sudoku_back.model.SudokuModel;

import java.util.ArrayList;
import java.util.List;

public interface SudokuService {
    List<SudokuModel> getSudokuTable();

    ArrayList<ArrayList<Integer>> generateSudokuBoard();
}
