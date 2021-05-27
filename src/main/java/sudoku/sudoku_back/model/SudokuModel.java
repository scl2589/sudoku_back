package sudoku.sudoku_back.model;

import lombok.Getter;

import java.sql.Date;

@Getter
public class SudokuModel {
    private int id;
    private Date start_time;
    private Date end_time;
    private int spent_time;
    private String answer;
    private String problem;
    private String nickname;
}
