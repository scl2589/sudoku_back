package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.List;

@Controller
public class SudokuController {

    @Autowired
    SudokuService sudokuService;

    @GetMapping("/sudoku")
    public String list(Model model) {
        List<SudokuModel> sudoku = sudokuService.printSudoku();
        model.addAttribute("sudokuList", sudoku);
        return "list";
    }

}
