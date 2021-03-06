package sudoku.sudoku_back.service.Impl;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sudoku.sudoku_back.dao.SudokuDao;
import sudoku.sudoku_back.model.SudokuModel;
import sudoku.sudoku_back.service.SudokuService;

import java.util.*;

@Service
public class SudokuServiceImpl implements SudokuService {
    private ArrayList<Integer> removedArr;
    private ArrayList<Integer> allElements;
    private ArrayList<ArrayList<Integer>> arr;

    HashSet<Integer>[] rows = new HashSet[9];
    HashSet<Integer>[] cols = new HashSet[9];
    HashSet<Integer>[] box = new HashSet[9];

    HashSet<Integer>[] checkRows = new HashSet[9];
    HashSet<Integer>[] checkCols = new HashSet[9];
    HashSet<Integer>[] checkBox = new HashSet[9];

    @Autowired
    private SudokuDao dao;

    @Override
    public List<SudokuModel> getSudokuTable() {
        List<SudokuModel> sudoku = dao.getSudokuTable();
        return sudoku;
    }

    @Override
    public ArrayList<ArrayList<Integer>> generateSudokuBoard() {
        // 초기화한다.
        removedArr = new ArrayList<>();
        allElements = new ArrayList<>();
        arr = new ArrayList<>();

        for (int i = 0; i < 81; i++) {
            allElements.add(i);
        }

        for (int i = 0; i < 9; i++ ) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            box[i] = new HashSet<>();
            ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
            arr.add(temp);
        }

        generateTopLeftBox();
        generateFirstRow();
        generateTopMiddleBox();
        generateTopRightBox();
        generateFirstCol();
        backtracking();
        if (backtracking()) {
            return arr;
        }
        return arr;
    }

    private void generateTopLeftBox() {
        // 좌상단 박스 생성하기
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(numbers);
        for (int k = 0; k < 9; k++) {
            int i = k / 3, j = k % 3;
            arr.get(i).set(j, numbers.get(k));

            // set 에 추가한다.
            rows[i].add(numbers.get(k));
            cols[j].add(numbers.get(k));
            box[0].add(numbers.get(k));
        }
    }

    private void generateFirstRow() {
        // 첫번째 col 생성하기
        ArrayList<Integer> firstRowAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (Integer e : rows[0]) {
            firstRowAvailable.remove(e);
        }
        Collections.shuffle(firstRowAvailable);

        for (int j = 3; j < 9; j++) {
            arr.get(0).set(j, firstRowAvailable.get(j-3));
            // HashSet에 저장해준다.
            rows[0].add(firstRowAvailable.get(j-3));
            cols[j].add(firstRowAvailable.get(j-3));
            int ij = j / 3;
            box[ij].add(firstRowAvailable.get(j-3));
        }
    }

    private void generateTopMiddleBox() {
        // 중앙 상단 박스 생성하기
        // 가능한 숫자 나열
        ArrayList<Integer> middleSecondRowAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // 중앙 박스에 이미 있는 요소들 제거
        for (Integer e : box[1]) {
            middleSecondRowAvailable.remove(e);
        }
        // 2번째 row에 있는 요소들 제거
        for (Integer e : rows[1]) {
            middleSecondRowAvailable.remove(e);
        }

        // 2번째 row에 필수적으로 있어야 하는 값 확인
        ArrayList<Integer> mustBeInMiddleSecondRow = new ArrayList<>();
        // 3번째 row에 숫자가 있어서 2번째 row에 필수적으로 들어가야 하는 값 추가
        for (Integer e: rows[2]) {
            if (middleSecondRowAvailable.contains(e)) {
                mustBeInMiddleSecondRow.add(e);
                middleSecondRowAvailable.remove(e);
            }
        }

        List<Integer> middleSecondRowValues = getNumbers(middleSecondRowAvailable, 3 - mustBeInMiddleSecondRow.size());
        middleSecondRowValues.addAll(mustBeInMiddleSecondRow);
        Collections.shuffle(middleSecondRowValues);

        for (int j = 3; j < 6; j++) {
            arr.get(1).set(j, middleSecondRowValues.get(j - 3));
            // HashSet에 저장해준다.
            rows[1].add(middleSecondRowValues.get(j-3));
            cols[j].add(middleSecondRowValues.get(j-3));
            box[1].add(middleSecondRowValues.get(j-3));
        }

        // 마지막 세번째 줄 추가하기
        ArrayList<Integer> middleThirdRowAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        // 중앙 박스에 이미 있는 요소들 제거
        for (Integer e : box[1]) {
            middleThirdRowAvailable.remove(e);
        }
        Collections.shuffle(middleSecondRowValues);

        // Text 추가하기
        for (int j = 3; j < 6; j++) {
            // text 추가한다.
            arr.get(2).set(j, middleThirdRowAvailable.get(j - 3));
            // HashSet에 저장해준다.
            rows[2].add(middleThirdRowAvailable.get(j-3));
            cols[j].add(middleThirdRowAvailable.get(j-3));
            box[1].add(middleThirdRowAvailable.get(j-3));
        }
    }

    private void generateTopRightBox() {
        ArrayList<Integer> rightSecondRowAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        ArrayList<Integer> rightThirdRowAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // 2번째 row에 있는 요소들 제거
        for (Integer e : rows[1]) {
            rightSecondRowAvailable.remove(e);
        }
        // 3번째 row에 있는 요소들 제거
        for (Integer e : rows[2]) {
            rightThirdRowAvailable.remove(e);
        }

        Collections.shuffle(rightSecondRowAvailable);
        Collections.shuffle(rightThirdRowAvailable);

        // Text 추가하기
        for (int j = 6; j < 9; j++) {
            // text 추가한다.
            arr.get(1).set(j, rightSecondRowAvailable.get(j - 6));
            arr.get(2).set(j, rightThirdRowAvailable.get(j - 6));
            // HashSet에 저장해준다.
            rows[1].add(rightSecondRowAvailable.get(j-6));
            rows[2].add(rightThirdRowAvailable.get(j-6));

            cols[j].add(rightSecondRowAvailable.get(j-6));
            cols[j].add(rightThirdRowAvailable.get(j-6));

            box[2].add(rightSecondRowAvailable.get(j-6));
            box[2].add(rightThirdRowAvailable.get(j-6));
        }
    }

    private void generateFirstCol() {
        ArrayList<Integer> firstColAvailable = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // 첫번째 column에 있는 요소들 제거
        for (Integer e : cols[0]) {
            firstColAvailable.remove(e);
        }

        Collections.shuffle(firstColAvailable);

        for (int i = 3; i < 9; i++) {
            arr.get(i).set(0, firstColAvailable.get(i-3));

            rows[i].add(firstColAvailable.get(i-3));
            cols[0].add(firstColAvailable.get(i-3));
            int ij = (i / 3) * 3 ;
            box[ij].add(firstColAvailable.get(i-3));
        }
    }

    private boolean backtracking() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (arr.get(i).get(j) != 0) continue;
                // 비어 있는 경우라면 해당 위치에 어떤 값이 들어갈 수 있는지 확인
                ArrayList<Integer> available = validValues(i, j);
                // 각각의 value를 넣고 가능한지 확인한다.
                for (Integer k : available) {
                    arr.get(i).set(j, k);
                    rows[i].add(k);
                    cols[j].add(k);
                    int ij = (i / 3) * 3 + j / 3;
                    box[ij].add(k);

                    if (backtracking()) {
                        return true;
                    }
                    else {
                        arr.get(i).set(j, 0);
                        rows[i].remove(k);
                        cols[j].remove(k);
                        box[ij].remove(k);
                    }
                }
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> validValues(int i, int j) {
        // 가능한 값들 선언
        ArrayList<Integer> available = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // 가로에 있는 값들을 다 제거한다.
        for (Integer e : rows[i]) {
            available.remove(e);
        }

        // 세로에 있는 값들을 다 제거한다.
        for (Integer e: cols[j]) {
            available.remove(e);
        }

        // 박스 값을 고려하며 박스 값 제거한다.
        int ij = (i / 3) * 3 + j / 3;

        for (Integer e: box[ij]) {
            available.remove(e);
        }

        return available;
    }


    private List<Integer> getNumbers(ArrayList<Integer> list, int totalItems ) {
        Random rand = new Random();
        List<Integer> returnList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(list.size());
            returnList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
        return returnList;
    }

    public ArrayList<ArrayList<Integer>> removeSudokuElement() {
        removeElement();
        return arr;
    }

    public void removeElement() {
        // 삭제할 element를 랜덤으로 구하고, removedArr 리스트에 추가한다.
        int removeNum = getNumbers(allElements, 1).get(0);
        removedArr.add(removeNum);

        // 없앤 숫자를 allElements 에서 삭제한다.
        allElements.remove(new Integer(removeNum));

        // 없애야 할 숫자를 board에서 삭제한다.
        for (Integer e: removedArr) {
            int i = e / 9; int j = e % 9;
            int value = arr.get(i).get(j);
            rows[i].remove(value);
            cols[j].remove(value);
            int ij = (i / 3) * 3 + j / 3;
            box[ij].remove(value);
            arr.get(i).set(j, 0);
        }

        if (removedArr.size()<= 2) {
            if (backtracking()) {
                removeElement();
            }
        }
    }

    public Boolean checkCorrect(String userSudoku) {
        for (int i = 0; i < 9; i++ ) {
            checkRows[i] = new HashSet<>();
            checkCols[i] = new HashSet<>();
            checkBox[i] = new HashSet<>();
        }

        for (int k = 0; k < 81; k++) {
            int i = k / 9;
            int j = k % 9;
            int ij = (i / 3) * 3 + j / 3;
            Integer current = Integer.parseInt(String.valueOf(userSudoku.charAt(k)));
            if (current == 0) {
                return false;
            } else {
                if (checkRows[i].contains(current) || checkCols[j].contains(current) || checkBox[ij].contains(current)) {
                    return false;
                } else {
                    checkRows[i].add(current);
                    checkCols[j].add(current);
                    checkBox[ij].add(current);
                }
            }
        }
        return true;
    }

    public void addSudoku(JSONObject jsonObj){
        dao.insertSudokuTable(jsonObj);
    }

    public void deleteSudoku(int id) {
        dao.deleteSudokuTable(id);
    }
}
