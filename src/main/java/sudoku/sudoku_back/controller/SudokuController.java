package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SudokuController {
    @Autowired
    private SudokuService sudokuService;

//    SudokuController(
//            SudokuService sudokuService
//    ) {
//        this.sudokuService = sudokuService;
//    }

    @GetMapping("/sudoku")
    @ResponseBody
    public List<Map<String, Object>> getSudoku() {
        return sudokuService.getSudoku();
    }

}
