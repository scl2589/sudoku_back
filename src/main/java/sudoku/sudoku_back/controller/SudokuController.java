package sudoku.sudoku_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static Logger logger = LoggerFactory.getLogger(SudokuController.class);

    @Autowired
    SudokuService sudokuService;

    @GetMapping("/initializetable")
    public List<SudokuModel> list (Model model) {
        List<SudokuModel> sudoku = sudokuService.getSudokuTable();
        logger.info("스도쿠 게임 기록들을 가져왔습니다.");
        return sudoku;
    }

    @GetMapping("/generate")
    public ArrayList<ArrayList<Integer>> generateSudokuBoard() {
        ArrayList<ArrayList<Integer>> sudoku = sudokuService.generateSudokuBoard();
        logger.info("스도쿠 게임들을 만들었습니다.");
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
        if (isCorrect == true) {
            logger.info("정답입니다.");
        } else {
            logger.info("오답입니다.");
        }

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

    @DeleteMapping("/sudoku/{id}")
    public void deleteSudoku(@PathVariable("id") int id) {
        sudokuService.deleteSudoku(id);
    }
}
