package sudoku.sudoku_back.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sudoku.sudoku_back.dao.SudokuDao;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.List;

@Service
public class SudokuServiceImpl implements SudokuService {

    @Autowired
    private SudokuDao dao;

    @Override
    public List<SudokuModel> printSudoku() {
        List<SudokuModel> sudoku = dao.getSudoku();
        System.out.println(sudoku);

        return sudoku;
    }
}
