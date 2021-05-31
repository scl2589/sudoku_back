package sudoku.sudoku_back.service;

import org.json.simple.JSONObject;
import sudoku.sudoku_back.model.SudokuModel;

import java.util.ArrayList;
import java.util.List;

public interface SudokuService {
    List<SudokuModel> getSudokuTable();

    ArrayList<ArrayList<Integer>> generateSudokuBoard();

    ArrayList<ArrayList<Integer>> removeSudokuElement();

    Boolean checkCorrect(String currentValue);

    void addSudoku(JSONObject jsonObj);
}
