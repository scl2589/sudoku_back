package sudoku.sudoku_back.controller;

import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.List;


@RestController
public class SudokuController {

    @Autowired
    SudokuService sudokuService;

//    @GetMapping("/sudoku")
//    public List<SudokuModel> list(Model model) {
//        List<SudokuModel> sudoku = sudokuService.printSudoku();
//        return sudoku;
//    }

    @GetMapping("/initializetable")
    public List<SudokuModel> list (Model model) {
        List<SudokuModel> sudoku = sudokuService.getSudokuTable();
        return sudoku;
    }

}
