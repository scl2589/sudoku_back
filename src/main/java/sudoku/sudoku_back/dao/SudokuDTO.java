package sudoku.sudoku_back.dao;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Getter
@Setter
public class SudokuDTO {

    @Autowired
    private SqlSession sqlsession;

    private int id;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

    private int spent_time;

    private String answer;

    private String problem;

    private String nickname;
}
