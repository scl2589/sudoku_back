package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sudoku.sudoku_back.dao.SudokuDTO;

import java.util.List;
import java.util.Map;

@RestController
public class SudokuController {
    @Autowired
    private SudokuService sudokuService;


    @RequestMapping("/sudoku")
    public int getSudoku() {
        return sudokuService.getSudoku();
    }

}
