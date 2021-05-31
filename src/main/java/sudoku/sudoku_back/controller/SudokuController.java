package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.ArrayList;
import java.util.List;


@RestController
public class SudokuController {

    @Autowired
    SudokuService sudokuService;

    @GetMapping("/initializetable")
    public List<SudokuModel> list (Model model) {
        List<SudokuModel> sudoku = sudokuService.getSudokuTable();
        return sudoku;
    }

    @GetMapping("/generate")
    public ArrayList<ArrayList<Integer>> generateSudokuBoard() {
        ArrayList<ArrayList<Integer>> sudoku = sudokuService.generateSudokuBoard();
        return sudoku;
    }

    @GetMapping("/generate/remove")
    public ArrayList<ArrayList<Integer>> removeSudokuElement() {
        ArrayList<ArrayList<Integer>> sudoku = sudokuService.removeSudokuElement();
        return sudoku;
    }

    @GetMapping("/correct/{userSudoku}")
    public Boolean checkCorrect(@PathVariable("userSudoku") String userSudoku) {
        Boolean isCorrect = sudokuService.checkCorrect(userSudoku);
        return isCorrect;
    }
}
