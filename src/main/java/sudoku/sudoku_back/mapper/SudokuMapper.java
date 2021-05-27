package sudoku.sudoku_back.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SudokuMapper {
    public int getSudoku();
}
