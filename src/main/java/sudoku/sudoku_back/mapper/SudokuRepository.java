package sudoku.sudoku_back.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SudokuRepository {
    List<Map<String, Object>> getSudoku();
}
