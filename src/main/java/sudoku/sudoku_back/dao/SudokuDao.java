package sudoku.sudoku_back.dao;

import org.json.simple.JSONObject;
import sudoku.sudoku_back.model.SudokuModel;

import java.util.List;

public interface SudokuDao {
    List<SudokuModel> getSudokuTable();

    void insertSudokuTable(JSONObject jsonObj);
}
