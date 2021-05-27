package sudoku.sudoku_back.controller;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface SudokuService {
    List<Map<String, Object>> getSudoku();
}
