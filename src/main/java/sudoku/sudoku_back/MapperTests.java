package sudoku.sudoku_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import sudoku.sudoku_back.controller.SudokuService;

@SpringBootTest
public class MapperTests {
    @Autowired
    private SudokuService sudokuService;

    @Test
    public void testOfGet() {
        int result = sudokuService.getSudoku();
        System.out.println(result);
    }
}
