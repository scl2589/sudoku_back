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
    public List<SudokuModel> getSudokuTable() {
        List<SudokuModel> sudoku = dao.getSudokuTable();
        for (SudokuModel e : sudoku) {

        }
//        System.out.println(sudoku);

        return sudoku;
    }
}
