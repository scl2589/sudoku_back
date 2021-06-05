package sudoku.sudoku_back.dao;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import sudoku.sudoku_back.model.SudokuModel;

import java.util.List;

public interface SudokuDao {
    @Bean
    List<SudokuModel> getSudokuTable();

    void insertSudokuTable(JSONObject jsonObj);

    void deleteSudokuTable(int id);
}
