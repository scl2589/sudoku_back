package sudoku.sudoku_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import sudoku.sudoku_back.controller.SudokuServiceImpl;
import sudoku.sudoku_back.dao.SudokuDTO;
import sudoku.sudoku_back.mapper.SudokuMapper;

import java.sql.Date;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class MapperTests {
    @Autowired
    private SudokuServiceImpl sudokuService;

    @Test
    public void testOfGet() {
        SudokuDTO params = new SudokuDTO();
        List<Map<String, Object>> result = sudokuService.getSudoku();
        System.out.println(result);
    }
}
