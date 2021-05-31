package sudoku.sudoku_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    @PostMapping("/sudoku")
    public void addSudoku(@RequestBody String sudokuData) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(sudokuData);
        JSONObject jsonObj = (JSONObject) obj;
        sudokuService.addSudoku(jsonObj);
    }
}
