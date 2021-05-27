package sudoku.sudoku_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sudoku.sudoku_back.mapper.SudokuRepository;

import java.util.List;
import java.util.Map;

@Service
public class SudokuServiceImpl implements SudokuService {
    @Autowired
    private SudokuRepository sudokuRepository;

    @Override
    public List<Map<String, Object>> getSudoku() {
        return sudokuRepository.getSudoku();
    }
}
