# 프로젝트 개요

- 목표: Spring Boot를 활용하여 MySQL과 연결하고 HttpClient를 활용하여 Front와 Back 통신하기 
- 기간: 5/26(수) ~ 6/1(화)



# 프로젝트 구성

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/47227380-1ef3-47bd-a22e-34b8bee9485b/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/47227380-1ef3-47bd-a22e-34b8bee9485b/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T022114Z&X-Amz-Expires=86400&X-Amz-Signature=6df49bd2e52d06c94bbbe63ef841d57b1981abe4e419e7eb29afe51e951510c4&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

- 프로젝트의 MVC를 나누었다
  - View는 Sudoku Front 프로젝트가 담당하므로, View를 제외한 나머지 부분들로 구성하였다.
- Model, Controller, DAO, Service, Config로 패키지를 나누었다.
- Config
  - DataAccessConfig파일
    - Mapper에서 매칭된 쿼리문이 적힌 xml 파일을 가져와 DB 데이터에 직접 접근할 수 있도록 설정해주는 클래스이다.
  - DataSourceConfig 파일
    - [application.properties](http://application.properties) 파일에 적힌 설정값을 토대로 데이터에 접근하기 위한 기본 작업을 하는 클래스이다.
  - Controller
    - Controller파일은 브라우저가 요청한 URL에 맞는 함수를 실행해서 어떤 장보를 나타낼지 설정한다.

# MySQL 및 MyBatis와 연동하기

## MyBatis란?

### MyBatis의 역할

- Java Object와 SQL문 사이에서 자동 mapping을 지원하는 ORM 프레임워크이다.
- MyBatis는 SQL을 별도의 파일로 분리해서 관리하게 해주며, 객체-SQL 사이의 파라미터 mapping 작업을 자동으로 해준다.
- MyBatis는 Hibernate나 JPA(Java Persistence Api)처럼 새로운 DB프로그래밍 패러다임을ㅜ익혀야하는 부담 없이, **개발자가 익숙한 SQL을 그대로 이용**하면서 **JDBC 코드작성의 불편함도 제거**해주고, 도메인 객체나 VO 객체를 중심으로 개발이 가능하다는 장점이 있다.

### MyBatis의 특징

- 간단한 persistence framework이다.
- XML형태로 서술된 JDBC 코드라고 생각해도 될 만큼 JDBC의 모든 기능을 MyBatis가 대부분 제공한다.
- 복잡한 JDBC 코드를 걷어내며 깔끔한 소스코드를 유지할 수 있다.
- 수동적인 parameter 설정과 쿼리 결과에 대한 매핑 구문을 제거할 수 있다.
- SQL문과 프로그래밍 코드가 분리되었다.
- SQL에 변경이 있을 때마다 자바 코드를 수정하거나 컴파일 하지 않아도 된다.
- SQL 작성과 관리 또는 검토를 DBA 같은 개발자가 아닌 다른 사람에게 맡길 수도 있다.

### MyBatis를 사용하는 데이터 액세스 계층 흐름도

![](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/1091be7e-a5ae-4166-9ec4-15aeb87451f0/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021554Z&X-Amz-Expires=86400&X-Amz-Signature=ecc2c652334a3253db10f4447d60cc74761084e735f845c6ee64d93a6404db15&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

[MyBatis 의 주요 컴포넌트 역할](https://www.notion.so/8b250e37f9ad41e48e1ea6c57b9c5f10)

## MyBatis와 MySQL 설정

### pom.xml에 추가

- MySQL Connector
- Mybatis
- Mybatis-spring (스프링에서 Mybatis 연동을 위한 모듈)
- spring-jdbc (기본 Java JDBC가 아닌 Spring의 JDBC)

```xml
			<dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
			<dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
```

### 테이블 생성

- sudoku라는 이름의 table생성 및 스키마 생성

![Table - Schema](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/b32026f8-b493-4db5-bfc0-71776f0ca463/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021612Z&X-Amz-Expires=86400&X-Amz-Signature=9d6de44f423352fa9cec680707c52cf4abd3aa7806d9ccbdec7f2cd227c96aae&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

### 데이터베이스 연결 정보 작성

- `[application.properties](<http://application.properties>)` 파일을 생성해 작성한다.

```xml
# 데이터베이스 세팅
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://주소/데이터베이스명?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username={username}
spring.datasource.password={password}

# 마이바티스 세팅
mybatis.type-aliases-package:매퍼용 클래스가 위치한 경로
mybatis.mapper-locations: xml파일이위치한 경로/*.xml
```

- properties 파일
  - DB 연결정보처럼 자주 변경될 여지가 있는 값을 properties 파일에 작성한 뒤 이를 참조해서 값을 가져올 수 있도록 한다.
  - property 파일이 **classpath**에 있으면, **resource 속성**을 사용해서 설정한다.
  - property 파일이 **classpath 밖에 있으면**, **url 속성**을 사용해서 설정한다.

### MyBatis 초기화하기

- DatabaseConfiguration을 작성한다.
- SqlSessionFactory는 데이터베이스와의 연결과 SQL의 실행에 대한 모든 것을 가진 가장 중요한 객체이다. 이 객체가 DataSource를 참조하여 MyBatis와 MySql 서버를 연동시켜준다.
  - SqlSessionFactory를 생성해주는 SqlSessionFactoryBean객체를 먼저 설정해야 한다.

```java
@Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml")
        );
        return sessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
```

- 이 설정은 설정한 datasource를 사용하고 **쿼리가 작성되는 xml위치를 지정**해 줌으로써 추후 Mapper or DAO 레벨에서 사용되는 쿼리를 인식해주는 과정이다.
- 여기서 classpath는 src/main/resources이고 해당 쿼리가 있는 xml 위치는 본인의 취향대로 위치키시고 그에 맞도록 설정해주면 된다.
- SqlSessionFactoryBean
  - SqlSessionFactory를 작성하고 Spring DI 컨테이너에 개체를 저장하는 구성 요소.
  - 표준 MyBatis3에서 SqlSessionFactory는 MyBatis 구성 파일에 정의된 정보를 기반으로 한다.
  - 그러나 SqlSessionFactoryBean을 사용하면 MyBatis 구성 파일이 없어도 SqlSessionFactory를 빌드할 수 있다.

# Sudoku 게임 기록 모두 가져오기 (TableView 연결)

## Front단

```java
private void initializeTable() throws Exception, JsonMappingException {
        // 테이블 초기화하기
        sudokuTable.getItems().clear();

        nicknameColumn.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        spentTimeColumn.setCellValueFactory(cellData -> cellData.getValue().spentTimeProperty().asObject());

        // 각각의 row마다 선택된 아이템에 대해 event listener 추가하기
        sudokuTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSudokuGame(newValue));

        // 테이블에 observable 리스트 데이터를 추가한다.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("<http://localhost:8080/initializetable>");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String content = EntityUtils.toString(entity, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(content);
        for (JsonNode node : nodes) {
            int id = node.get("id").asInt();
            String nickname = node.get("nickname").asText();
            int spent_time = node.get("spent_time").asInt();
            String problem = node.get("problem").asText();
            String answer = node.get("answer").asText();
            String start_time =node.get("start_time").asText();
            String end_time = node.get("end_time").asText();
            sudokuData.add(new Sudoku(id, nickname, start_time, end_time, spent_time, problem, answer));
        }

        sudokuTable.setItems(sudokuData);
    }
```

- Front와 Back과의 통신을 위해 `HttpClient`를 사용하였다.

- ```
  CloseableHttpClient httpClient = HttpClients.createDefault();
  ```

  - `createDefault()` 메소드는 내부에서 `HttpClientBuilder`를 통해 HttpClient를 생성한다.

- ```
  HttpGet httpGet = new HttpGet("<http://localhost:8080/initializetable>");
  ```

  - 어떤 주소로, 그리고 어떤 HttpMethod로 요청을 보낼지 정한다.

- ```
  CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
  ```

  - 이후, 통신을 보내서 오는 응답을 httpResponse 변수에 담고,

- ```
  HttpEntity entity = httpResponse.getEntity();
  ```

  - HttpEntity는 Http 프로토콜을 이용하는 통신의 header와 body 관련 정보를 저장할 수 있도록 한다.

- ```
  String content = EntityUtils.toString(entity, "UTF-8");
  ```

  - 이후 HttpEntity를 받은 정보를 String으로 변환하여 저장한다.

- ```
  ObjectMapper mapper = new ObjectMapper();
  ```

  - JSON 관련한 부분은 Jackson을 활용하였다.

- ```
  JsonNode nodes = mapper.readTree(content);
  ```

  - Object Mapper를 이용하여 String으로 구성되어있는 값을 JSON 형태로 변환하였다.

```java
for (JsonNode node : nodes) {
            int id = node.get("id").asInt();
            String nickname = node.get("nickname").asText();
            int spent_time = node.get("spent_time").asInt();
            String problem = node.get("problem").asText();
            String answer = node.get("answer").asText();
            String start_time =node.get("start_time").asText();
            String end_time = node.get("end_time").asText();
            sudokuData.add(new Sudoku(id, nickname, start_time, end_time, spent_time, problem, answer));
        }
```

- 이후 노드를 하나씩 돌면서 node의 값들을 받아 sudokuData로 추가하였다.

## Back단

- `query.xml` 작성

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "<http://mybatis.org/dtd/mybatis-3-mapper.dtd>">
  
  <mapper namespace="sudoku.sudoku_back.dao.SudokuDao">
      <select id="getSudokuTable" resultType="sudoku.sudoku_back.model.SudokuModel">
          select * from sudoku
      </select>
  </mapper>
  ```

  - mapper 태그의 namespace는 아직은 생성하지 않은 Mapper 인터페이스를 의미 (밑에 생성한다!)
  - 이렇게 xml에 쿼리문을 작성한다.

- `[SudokuDao.java](<http://sudokudao.java>)` (Interface 구현)

  ```java
  package sudoku.sudoku_back.dao
  import sudoku.sudoku_back.model.SudokuModel;
  
  import java.util.List;
  
  public interface SudokuDao {
      List<SudokuModel> getSudokuTable();
  }
  ```

  - getSudokuTable을 통해 쿼리문을 관리하는 `query.xml`에 접근해 DB와 상호작용한다.
  - 그렇기 때문에 `query.xml` 파일의 select 태그에서 지정한 **id** 값과 같아야 한다.

- `SudokuService.java` (Interface) Service 클래스 작성

  ```java
  package sudoku.sudoku_back.service;
  
  import sudoku.sudoku_back.model.SudokuModel;
  
  public interface SudokuService {
      List<SudokuModel> getSudokuTable();
  }
  ```

  - 이 클래스는 controller에서 MySQL에 접근하기 위한 첫 번째 관문이다.
  - 해당 객체는 싱글톤으로 사용되어야 하기 때문에 `@Service` 애노테이션을 붙여 Bean 객체로 만든다.

- `SudokuServiceImpl.java`

  ```java
  @Override
      public List<SudokuModel> getSudokuTable() {
          List<SudokuModel> sudoku = dao.getSudokuTable();
          return sudoku;
      }
  ```

  - 반환형 List<SudokuModel> 형태로 DB 정보를 컨트롤러에서 받을 수 있도록 getSudokuTable() 메서드를 작성한다.
  - ServiceImpl?
    - `Service` 는 인터페이스로 생성하고, `ServiceImpl` 을 통해 구현객체를 생성해 사용하고 있다.
    - 비즈니스 로직을 구현하는 구현부로, 순수 자바객체로 이루어져있고 Service와 ServiceImpl을 1:1 구조로 진행할 수 있다.

- `SudokuController.java`

  ```java
  @RestController
  public class SudokuController {
  
      @Autowired
      SudokuService sudokuService;
  
      @GetMapping("/initializetable")
      public List<SudokuModel> list (Model model) {
          List<SudokuModel> sudoku = sudokuService.getSudokuTable();
          return sudoku;
      }
  }
  ```

  - ```
    @RestController
    ```

    - `@RestController`는 Spring MVC Controller에 `@ResponseBody`가 추가된 것이다.

      RestController의 주용도는 Json 형태로 객체 데이터를 반환하는 것이다.

      ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ab2124cd-28ca-4c61-812c-b00ed0c4965e/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/ab2124cd-28ca-4c61-812c-b00ed0c4965e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021651Z&X-Amz-Expires=86400&X-Amz-Signature=963ff2c5a72f88999a9201d05cf6dfb439aab52aa7015571d61af8c7a0610bc5&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

      1. Client는 URI 형식으로 웹 서비스에 요청을 보낸다.
      2. Mapping되는 Handler와 그 Type을 찾는 DispatcherServlet이 요청을 인터셉트한다.
      3. RestController는 해당 요청을 처리하고 데이터를 반환한다.

  - ```
    @Autowired
    ```

    - 필요한 의존 객체의 “타입"에 해당하는 빈을 찾아 주입한다.
      - 생성자
      - setter
      - 필드
    - 위의 3가지의 경우에 Autowired를 사용할 수 있다. 그리고 Autowired는 기본값이 true이기 때문에 **의존성 주입을 할 대상을 찾지 못한다면 애플리케이션 구동에 실패**한다.

  - ```
    @GetMapping
    ```

    - Get 방식의 API 요청을 만들 때 사용된다.
    - `@RequestMapping(method = RequestMethod.GET ...)`과 동일한 효과를 볼 수 있다.

# 스도쿠 게임 generate 하기

- 완성된 sudoku 퍼즐을 만드는 요청 하나, 그리고 원하는 갯수만큼 지우는 요청을 통해 완성된 스도쿠 게임/problem을 만들 수 있도록 하였다.

## 완성된 sudoku 만들기

### Front단

```java
CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("<http://localhost:8080/generate>");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String content = EntityUtils.toString(entity, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(content);

        // Answer 버튼을 위해 정답 배열을 만들어서 완성된 스도쿠를 담아둔다.
        answer = new ArrayList<>(9);
        for (JsonNode node : nodes) {
            ArrayList<Integer> temp = new ArrayList<>(9);
            for (int j = 0; j < 9; j++) {
                int value = node.get(j).asInt();
                temp.add(value);
            }
            answer.add(temp);
        }
```

- 완성된 스도쿠 퍼즐이 만들어지면, 이를 읽어 정답 버튼을 위해, 정답 배열에 담아두었다.

### Back단

- Controller에서 `GetMapping`으로 요청을 받는다.

  ```java
  @GetMapping("/generate")
      public ArrayList<ArrayList<Integer>> generateSudokuBoard() {
          ArrayList<ArrayList<Integer>> sudoku = sudokuService.generateSudokuBoard();
          return sudoku;
      }
  ```

- Service 단에서는

  - `ArrayList<ArrayList<Integer>> generateSudokuBoard();` 을 통해 `generateSudokuBoard()`라는 메소드를 interface에 작성하고,
  - 기존에 Front에서 작성했던 코드를 옮겨왔다. (generateTopLeftBox(), generateFirstRow(), generateTopMiddleBox(), generateTopRightBox(), generateFirstCol(), backtracking())

## Sudoku 퍼즐 만들기

- 사용자에게 보여줄 스도쿠 퍼즐을 만들기 위해 완성된 스도쿠 퍼즐에서 몇 개의 숫자를 지우는 요청을 보낸다.

### Front단

```java
// 스도쿠 문제를 생성하기 위해 완성된 스도쿠의 element를 하나씩 지운다.
        httpGet = new HttpGet("<http://localhost:8080/generate/remove>");
        httpResponse = httpClient.execute(httpGet);
        entity = httpResponse.getEntity();
        content = EntityUtils.toString(entity, "UTF-8");
        mapper = new ObjectMapper();
        nodes = mapper.readTree(content);

        // 문제 저장하기
        question = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = nodes.get(i).get(j).toString();
                if ("0".equals(value)) {
                    question.append("_ ");
                    arr.get(i).get(j).setText("");
                } else {
                    question.append(value).append(" ");
                    arr.get(i).get(j).setText(value);
                }
            }
            question.append("\\n");
        }
```

- 숫자들이 지워져 스도쿠 문제가 만들어지면, 이 문제를 저장하고, 실제로 사용자에게 보여질 수 있도록 `setText` 를 통해 숫자를 기입한다.

### Back단

- Controller에서 `GetMapping`으로 요청을 받는다.

  ```java
  @GetMapping("/generate/remove")
      public ArrayList<ArrayList<Integer>> removeSudokuElement() {
          ArrayList<ArrayList<Integer>> sudoku = sudokuService.removeSudokuElement();
          return sudoku;
      }
  ```

- Service 단에서는

  - `ArrayList<ArrayList<Integer>> removeSudokuElement();` 을 통해 `removeSudokuElement`라는 메소드를 interface에 작성하였다.
  - 또 SudokuServiceImpl에 기존 Front에서 작성했던 숫자 제거하는 코드를 옮겨왔다.
    - `removeSudokuElement()`, `removeElement()` 참고!

# 데이터 추가하기 (게임기록 추가하기)

- 게임 기록을 추가하기 전에 먼저, 사용자가 입력한 값이 정답인지 확인하기 위한 요청이 필요했다.
- 위에 대한 응답으로 사용자가 입력한 값이 정답이 맞다면 Sudoku 테이블 스키마에 맞는 parameter과 함께 요청을 보내고 이를 DB에 추가할 수 있도록 하였다.

## 정답인지 확인하기

### Front단

```java
// 현재 값 String에 담아두기
            StringBuilder currentValue = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if ("".equals(arr.get(i).get(j).getText())) {
                        currentValue.append("0");
                    } else {
                        currentValue.append(arr.get(i).get(j).getText());
                    }
                }
            }

            //정답인지 확인하기
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("<http://localhost:8080/correct/>" +currentValue.toString());
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            Boolean content = Boolean.parseBoolean(EntityUtils.toString(entity, "UTF-8"));
```

- Get요청은 body에 담을 수 없으므로 어떻게 보내야 하나 고민하다보니 query string으로 보내기로 결정하였다.

  - 그래서 String에 현재 값들을 담았고, 이를 parameter로 보내기로 결정하였다.

  ```java
  HttpGet httpGet = new HttpGet("<http://localhost:8080/correct/>" +currentValue.toString());
  ```

### Back단

- Controller에서 `GetMapping`으로 요청을 받는다.

  ```java
  @GetMapping("/correct/{userSudoku}")
      public Boolean checkCorrect(@PathVariable("userSudoku") String userSudoku) {
          Boolean isCorrect = sudokuService.checkCorrect(userSudoku);
          return isCorrect;
      }
  ```

  - `@PathVariable` 애노테이션을 통해 user의 값을 받는다.

- Service 단에서는

  - `Boolean checkCorrect(String currentValue);` 메소드를 interface에 작성하고,
  - 기존에 Front에서 작성했던 correct() 메소드를 옮겨왔다.
  - 정답이라면 return true를, 오답이라면 return false를 하게끔 작성하였다.

## DB에 추가하기

### Front단

```java
DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");

ObjectMapper mapper = new ObjectMapper();
ObjectNode postParam = mapper.createObjectNode();
postParam.put("nickname", nickname);
postParam.put("starttime", df.format(start_time));
postParam.put("endtime", df.format(end_time));
postParam.put("count", count);
postParam.put("answer", sudokuAnswer.toString());
postParam.put("question", question.toString());
String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(postParam);

// DB에 데이터 추가하기
HttpPost httpPost = new HttpPost("<http://localhost:8080/sudoku/>");
httpPost.addHeader("accept", "application/json");
httpPost.addHeader("Content-Type", "application/json");
HttpEntity stringEntity = new StringEntity(json, "UTF-8");
httpPost.setEntity(stringEntity);
httpClient.execute(httpPost);
```

- Jackson 을 이용해 JSON 형태의 object를 생성하였다. 그리고 이를 String으로 바꿔 httpPost 요청의 body에 추가하는 방향으로 진행했다.

### Back단

- Sudoku 기록을 저장하기 위함이므로 Controller에서 `PostMapping`으로 요청을 받는다.

  ```java
  @PostMapping("/sudoku")
      public void addSudoku(@RequestBody String sudokuData) throws JsonProcessingException, ParseException {
          ObjectMapper mapper = new ObjectMapper();
          JSONParser parser = new JSONParser();
          Object obj = parser.parse(sudokuData);
          JSONObject jsonObj = (JSONObject) obj;
          sudokuService.addSudoku(jsonObj);
      }
  ```

  - `@RequestBody` 애노테이션을 통해 Body로 넘어온 스도쿠 게임의 관련 값들을 받는다.
  - 그리고 Jackson 을 이용하여 String으로 넘어온 sudokuData를 JSON object로 변환한다.
  - 이후 해당 JSON Object를 Service로 넘겨준다.

- Service

  - `void addSudoku(JSONObject jsonObj);` 메소드를 interface에 작성하였다.

  - 다만 이전 method와 다르게 return 하는 값이 별도로 없고, DB에 저장만 하는 거이므로 `void` 로 작성하였다.

  - 이후 ServiceImpl에서는 쿼리와 연결하기 위해 dao의 메소드를 호출하였다.

    ```java
    public void addSudoku(JSONObject jsonObj){
        dao.insertSudokuTable(jsonObj);
    }
    ```

- Dao

  ```java
  void insertSudokuTable(JSONObject jsonObj);
  ```

  - Sudoku 기록을 추가하기 위해 JSONObject와 함께 넘겨주었다.

- Query.xml

  - Dao에서의 method 와 같은 이름으로 id를 작성하고, 어떤 식으로 DB에 추가할 지 query를 작성하였다.
  - Mapper 태그의 namespace 를 통해 어떤 dao와 연결을 했는지 미리 선언했기 때문에 자동으로 DB에 쿼리를 실행할 수 있도록 mapping해준다.

  ```java
  <mapper namespace="sudoku.sudoku_back.dao.SudokuDao">
      <insert id="insertSudokuTable">
          INSERT INTO SUDOKU(nickname, start_time, end_time, spent_time, answer, problem)
          VALUES(#{nickname}, #{starttime}, #{endtime}, #{count}, #{answer}, #{question})
      </insert>
  </mapper>
  ```

### AUTO_INCREMENT

- DB에 넣다 보니 지속적으로 ID가 duplicated 됐다는 오류가 나왔다. 확인 결과, 새로운 데이터가 추가되면 id 값이 1씩 증가해야 하는데 그러지 않아서 생기는 문제였다.
- 이를 위해서 Id column이 자동으로 증가할 수 있도록 쿼리를 작성하는 방법을 찾아보았다.
- 다음은 지정한 column에 Auto_increment를 추가하는 코드이다.
  - `ALTER TABLE 테이블이름 MODIFY COLUMN 컬럼이름 INT auto_increment`
- 위 방법을 사용하여 id값을 1씩 증가시킬 수 있었고, 기존 문제를 해결하였다.



# Sudoku 게임 기록 삭제하기

- 어떤 Sudoku 게임 기록을 삭제하고 싶은지 판단하기 위해 그 Sudoku 게임 기록의 id를 parameter로 받아 DB에서 삭제할 수 있도록 하였다.

## Front단

```java
@FXML
    private void handleDeleteSudoku() throws Exception {
        int selectedIndex = sudokuTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            int selectedDBIndex = sudokuTable.getSelectionModel().getSelectedItem().getId().getValue().intValue();
            // Delete Http 통신 진행
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete("<http://localhost:8080/sudoku/>" + selectedDBIndex);
            httpClient.execute(httpDelete);
            sudokuTable.getItems().remove(selectedIndex);
        } else {
            // 아무 sudoku 게임 기록도 선택하지 않은 경우)
            Alert alert = createAlert("warning", "오류", "선택된 기록이 없습니다.", "스도쿠 게임 기록을 선택해주세요.");
            alert.showAndWait();
        }
    }
```

- ```
  selectedIndex
  ```

  : sudokuTable에서 해당 게임을 선택했을 때, 선택되는 Index를 먼저 파악했다.

  - 이를 통해 sudokuTable의 Index를 기반으로 먼저 Front에서 삭제하였다.

- 또한 

  ```
  getId()
  ```

   메서드를 통해 선택된 게임 기록의 실제 DB에서의 ID를 가져왔고, 이를 

  ```
  selectedDBIndex
  ```

   에 저장하였다.

  - 이 Index 값과 함께 Http Delete 요청을 Back으로 보냈다.

## Back단

- Sudoku 기록을 삭제하기 위해 `DeleteMapping`으로 요청을 받는다.

  ```java
      @DeleteMapping("/sudoku/{id}")
      public void deleteSudoku(@PathVariable("id") int id) {
          sudokuService.deleteSudoku(id);
      }
  ```

  - `@PathVariable`애노테이션을 통해 queryString으로 넘어온 삭제할 스도쿠 게임의 id를 받는다.
  - 이후 sudokuService의 `deleteSudoku` 메소드를 요청한다.

- Service

  - `void deleteSudoku(int id);` 메소드를 interface에 작성하였다.

  - 이후 ServiceImpl에서는 쿼리와 연결하기 위해 dao의 메소드를 호출하였다.

    ```java
    public void deleteSudoku(int id) {
        dao.deleteSudokuTable(id);
    }
    ```

- Dao

  ```java
  void deleteSudokuTable(int id);
  ```

  - Sudoku 기록을 삭제하기 위해 id 값과 함께 넘겨주었다.

- Query.xml

  ```java
  <mapper namespace="sudoku.sudoku_back.dao.SudokuDao">
      <delete id="deleteSudokuTable">
          DELETE FROM sudoku WHERE id=#{id}
      </delete>
  </mapper>
  ```

  - id 가 일치하면 삭제할 수 있도록 query를 작성하였다.



# 한글 인코딩 추가

- 닉네임에 한글을 적었더니, Table에서 '???' 형태로 나오는 것을 발견할 수 있었다.
- 이를 해결하기 위해서 찾아본 결과, 인코딩 문제였다.
- Request를 보낼 때 "UTF-8" 로 보내야 한다. 그래야 한글 인식이 가능해 잘 출력되는 모습을 확인할 수 있다.

```java
HttpEntity stringEntity = new StringEntity(json, "UTF-8");
httpPost.setEntity(stringEntity);
```



# 로그

## 개념

### 로그의 목적과 요구사항

- 서비스 동작 상태 파악
- 장애 파악 & 알림
- 위 목적으로 작성된 로그를 분석하면 서비스 지표의 확인, 트랜잭션, 성능 등 다양한 정보 확인 가능

### 로그의 필요성

- 스프링으로 개발할 때 콘솔에 엄청 많은 로그들이 출력되는데 실제로 서비스할 때는 이런 로그 메시지들이 필요 없게 된다.
- 이런 경우에는, 일일이 찾아서 해당 로그들을 하나씩 삭제해주어야 하는데 매우 번거롭다.
- 이럴 때 필요한 것이 `log4j`이다!

### log4j.xml을 이루는 태그

```xml
<log4j:configuration xmlns:log4j="<http://jakarta.apache.org/log4j/>"> 
    <!-- Appenders --> 
    <appender name="console" class="org.apache.log4j.ConsoleAppender"> 
        <param name="Target" value="System.out" /> 
        <layout class="org.apache.log4j.PatternLayout"> 
            <param name="ConversionPattern" value="%-5p: %c - %m%n" /> 
        </layout> 
    </appender> 
    
    <!-- Application Loggers --> 
    <logger name="com.myspring.pro27"> 
        <level value="info" /> 
    </logger> 
    
    <!-- 3rdparty Loggers --> 
    <logger name="org.springframework.core"> 
        <level value="info" /> 
    </logger> 
    <logger name="org.springframework.beans"> 
        <level value="info" /> 
    </logger> 
    <logger name="org.springframework.context"> 
        <level value="info" /> 
    </logger> 
    <logger name="org.springframework.web"> 
        <level value="info" /> 
    </logger> 
    
    <!-- Root Logger --> 
    <root> <priority value="debug" /> 
        <appender-ref ref="console" /> 
    </root> 
</log4j:configuration>
```

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0eecdbe4-cf0d-4feb-a14d-95ffecdcbb72/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/0eecdbe4-cf0d-4feb-a14d-95ffecdcbb72/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021734Z&X-Amz-Expires=86400&X-Amz-Signature=ca70a9ddca8943faad56912bbd82338a04350547c6fadc4adb354a58e3b9d8a5&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

### 로그 레벨

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/13de02ac-a2fd-451c-8d0d-086d5296a00b/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/13de02ac-a2fd-451c-8d0d-086d5296a00b/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021746Z&X-Amz-Expires=86400&X-Amz-Signature=34ff1d89e998a93c1b67171ffb255250e9ec53480d146a79cc3e215869893086&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

- 지정한 레벨의 낮은 레벨의 메세지들은 출력되지 않지만, **그보다 높은 레벨의 메시지들은 출력**된다.
- 예시
  - 만약 INFO로 설정했다면 DEBUG와 TRACE 관련 메시지는 출력되지 않지만, 그보다 높은 나머지 3레벨은 INFO와 같이 출력됩니다.

### Appender가 가지는 클래스

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e03e24b2-1b73-4368-8902-701adfeaad51/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/e03e24b2-1b73-4368-8902-701adfeaad51/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021757Z&X-Amz-Expires=86400&X-Amz-Signature=9c077c55126497a3252d4f541f460fd99400bbb8ae53df52131962e1fb8ffd4d&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

### PatternLayout 클래스에서 사용되는 속성들

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/17b06b3c-6f49-4572-940c-1c06d1ed4944/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/17b06b3c-6f49-4572-940c-1c06d1ed4944/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021811Z&X-Amz-Expires=86400&X-Amz-Signature=3cc29fc9cdb0e6adb49a267f5bfbe591b4ffdd9346e1a50afc301c0f0c78364b&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)



## 사용 방법

### [log4j.properties](http://log4j.properties) 파일 작성

- src 하위 경로에 파일을 작성한다.

```java
# Root logger option
 
# Log4j Setting file
# 첫 번째 인자 DEBUG: 로그 레벨을 의미하며 해당 로그레벨 이상만 출력하겠다는 의미
# 로그레벨 : TRACE < DEBUG < INFO < WARN < ERROR < FATAL
# 두 번째 인자 console. R : appender뒤에 붙일 이름으로 사용할 키워드 (그냥 변수라 보면됨)
# appender? : 로깅 메시지를 파일로 보낼 것인지, 콘솔로 출력할 것인지를 구분하기 위한 변수
log4j.rootLogger=DEBUG, console, R
 
# Daily file log
# 위에서 선언한 R이라는 이름을 로그 파일 세팅변수로 선언, 파일에 남길 로그셋팅을 할 수 있다.
log4j.appender.R=org.apache.log4j.DailyRollingFileAppender
# 로그를 남길 경로를 지정
log4j.appender.R.File=C:/Users/Desktop/web.log
# 로그 파일은 하루가 지날 시 백업파일을 생성하는데 로그 파일 뒤에 붙일 파일명을 설정 ex) web.log.2019-01-01
log4j.appender.R.DatePattern='.'yyyy-MM-dd
# 로그 메시지를 어떻게 보여줄 지에 대한 레이아웃 설정( HTMLLayout, PatternLayout, XMLLayout, SimpleLayout ... ) 패턴레이아웃이 주로 쓰임
log4j.appender.R.layout=org.apache.log4j.PatternLayout
# 로그 출력 레이아웃을 설정
log4j.appender.R.layout.ConversionPattern=[%d{HH:mm:ss}][%-5p](%F:%L)-%m%n
 
# Console log
# console이라는 이름을 콘솔에 출력할 로그 세팅변수로 선언
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%d{HH:mm:ss}][%-5p](%F:%L)-%m%n
# 로그 메시지 버퍼 사용유무(default: true)
log4j.appender.console.ImmediateFlush=true
```



### properties 경로 설정

- src 경로 아래에 있을 때는 properties의 위치를 따로 잡아줄 필요는 없다.

- 만약 web-inf 아래에 있을 때는, web.xml에 아래 코드를 추가해야 한다.

  ```xml
  <!-- Log4j 설정 -->
  <context-param>
  	<param-name>log4jConfigLocation</param-name>
  	<param-value>/WEB-INF/config/log4j.properties</param-value>
  </context-param>
  
  <listener>
  	<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
  </listener>
  ```

## 로그 출력

- 로그 객체 생성에는 2가지 방법이 있다.
- 메서드는 중요도에 따라 사용하면 된다.

```java
@Controller
public class LogTest {
	@RequestMapping("/test")
	public String test(){
		Logger logger = Logger.getRootLogger();
		/*Log logger = LogFactory.getLog(this.getClass());*/
		logger.debug("디버그");
		logger.info("인포");
		return "test.jsp";
	}
}

// Logger logger = Logger.getRootLogger(); -> build path 설정 필요
// Log logger = LogFactory.getLog(this.getClass()) -> lib폴더에 jar필요
```



## Controller에 적용

```java
static Logger logger = LoggerFactory.getLogger(SudokuController.class);    

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
        logger.info("스도쿠 기록을 추가합니다.");
        sudokuService.addSudoku(jsonObj);
    }

    @Transactional
    @DeleteMapping("/sudoku/{id}")
    public void deleteSudoku(@PathVariable("id") int id) {
        logger.info("스도쿠 게임 기록 " + id + "번이 삭제됩니다.");
        sudokuService.deleteSudoku(id);
    }
```

- 현재로써는 간단한 프로젝트이기 때문에, log level은 모두 info로 설정하여 로그를 출력할 수 있도록 하였다.



# 트랜잭션

## 트랜잭션이란?

- **데이터베이스 연산들의 논리적 단위**이며 트랜잭션 내 모든 연산들이 **정상적으로 완료되지 않으면** 아무것도 수행되지 않은 **원래 상태로 복원**되어야 한다.
- 데이터에 대한 무결성을 유지하기 위한 처리 방법을 '트랜잭션 처리'라고 한다.
- 예시
  - 친구에게 인터넷 뱅킹으로 10000원 송금할 경우, 나의 계좌에서 10000원을 줄이고, 친구의 계좌에 10000원을 증가시켜야 한다. 하지만 알 수 없는 오류로 인해 나의 계좌에서 10000원이 줄었지만 친구 계좌에는 10000원이 증가되지 않는다면? 나의 10000원은 증발해버리게 된다.
  - 이런 오류는 엄청난 비용손실을 유발한다.
- 위와 같은 예시의 경우가 생기지 않도록, 중간에 오류가 발생하면 다시 처음부터 송금을 하게 하는 것이 `rollback`이다.

## Transaction의 기본 방법

- Transaction은 2개 이상의 쿼리를 하나의 커넥션으로 묶어 DB에 전송하고, 이 과정에서 에러가 발생할 경우 자동으로 모든 과정을 원래대로 되돌려 놓습니다.
- 이러한 과정을 구현하기 위해 Transaction은 하나 이상의 쿼리를 처리할 때 동일한 Connection 객체를 공유하도록 합니다

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/09e93dbb-94b3-48dc-a668-ec095b47c5cf/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/09e93dbb-94b3-48dc-a668-ec095b47c5cf/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021844Z&X-Amz-Expires=86400&X-Amz-Signature=74a23af1399d0ab60941b845d26ffc9361bd63bfc099667bdb05a48f44b69b75&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)



## 트랜잭션 처리 방법

1. 선언적 트랜잭션
2. `@Transactional` 어노테이션을 통한 트랜잭션

### 선언적 트랜잭션

- XML에 트랜잭션에 대한 설정을 함으로써 트랜잭션을 적용할 범위와 대상을 선언하는 방식이다.

- 트랜잭션 설정을 위해 새로운 설정 파일을 만든다.

  - src/main/resources → spring → `tx-context.xml` 파일 생성

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  
  <beans xmlns="<http://www.springframework.org/schema/beans>"
  	xmlns:xsi="<http://www.w3.org/2001/XMLSchema-instance>"
  	xmlns:tx="<http://www.springframework.org/schema/tx>"
  	xmlns:aop="<http://www.springframework.org/schema/aop>"
  	xsi:schemaLocation="
  		<http://www.springframework.org/schema/beans> 
  		<http://www.springframework.org/schema/beans/spring-beans.xsd>
  		<http://www.springframework.org/schema/aop> 
  		<http://www.springframework.org/schema/aop/spring-aop.xsd>
  		<http://www.springframework.org/schema/tx> 
  		<http://www.springframework.org/schema/tx/spring-tx.xsd>">
  
  	
  
  	<!-- Transaction 설정 -->
  	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
   		<property name="dataSource" ref="dataSource"/>
   	</bean>
  
   	 	 
  
   	<!-- Transaction을 위한 AOP 설정 -->
   	<aop:config proxy-target-class="true">
  		<aop:pointcut id="servicePublicMethod" expression="execution(public * com.freehoon.web.board..*(int))" />
  		<aop:advisor advice-ref="txAdvice" pointcut-ref="servicePublicMethod" />
  	</aop:config>
  
  	
  
  	<!-- 선언적 Transaction 설정  -->
  	<tx:advice id="txAdvice" transaction-manager="transactionManager">
  		<tx:attributes>
  			<tx:method name="getBoardContent" rollback-for="Exception" />
  		</tx:attributes>    
   	</tx:advice> 	
  </beans>
  ```

  - 트랜잭션을 위해 tx와 aop라는 2가지 namespace를 사용한다.
  - 또한, 트랜잭션 설정을 위해 스프링에서 제공하는 `org.springframework.jdbc.datasource.DataSourceTransactionManager` 클래스를 `transactionManager` 이라는 이름의 빈으로 등록합니다. `transactionManager` 빈의 property로 DB 설정에 사용했던 `dataSource` 빈을 참조할 수 있도록 추가합니다.
  - 선언적 트랜잭션 처리는 aop를 이용합니다.



### `@Transactional` 어노테이션을 통한 트랜잭션

- 일반적으로 Spring에서는 `Service Layer` 에서 `@Transactional`을 추가하여 Transaction 처리를 한다.

  - Transaction이 필요한 서비스 클래스에 `@Transactional` 어노테이션을 달아주면 된다.

  ```java
  //게시글 삭제
  @Transactional
  @Override
  public void removeBoard(BoardVO vo) throws Exception {
    replyDAO.removeAllRepl(vo.bno);  //삭제할 게시글의 답글 삭제
    boardDAO.deleteBoard(vo.bno);    //게시글 삭제
  }
  ```

  - 위 예시처럼 간단한 설정으로 트랜잭션을 설정할 수 있다.

- `@Transactional` 애노테이션은 **클래스의 메소드**뿐만 아니라, **인터페이스**, **클래스** 선언에도 사용이 가능하다.

  - 메소드에 선언된 `@Transactional` 의 설정이 가장 우선되기 때문에, 공통적인 규칙은 인터페이스, 클래스 등에 적용하고 특별한 설정은 메소드에 적용할 수 있다.

- 아래의 예시에는 상점과 관련된 Service 부분이고, 데이터의 조회만 일어나는 select 메소드에서는 `@Transactional` 을 활용하고 있지 않지만, **값을 추가하거나 변경 또는 삭제하는 insert, update, delete 메소드**에는 `@Transactional`을 추가하여 **트랜잭션을 설정**해두었다.

  ```java
  package com.mang.store.service; 
  import com.mang.store.vo.StoreVO; 
  import org.springframework.transaction.annotation.Transactional; 
  import java.util.List; 
  public interface StoreService { 
  		List<StoreVO> selectStoreInfoList(StoreVO storeVO); 
  		StoreVO selectStoreInfo(StoreVO storeVO); 
  
  		@Transactional 
  		int insertStoreInfo(StoreVO storeVO); 
  
  		@Transactional 
  		int updateStoreInfo(StoreVO storeVO); 
  
  		@Transactional 
  		int deleteStoreInfo(StoreVO storeVO); 
  }
  ```

- `@Transactional` 어노테이션을 사용하기 위해 Spring bean 설정을 아래와 같이 해야한다 (Spring boot 는 그냥 사용하면 된다.)

  ```java
  ....
  <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
  	destroy-method="close">
  	<property name="driverClassName" value="org.apache.derby.jdbc.ClientDriver" />
  	<property name="url" value="jdbc:derby://localhost:1527/sample" />
  	<property name="username" value="user" />
  	<property name="password" value="jmlim123" />
  </bean>
  
  <!-- transaction manager, use JtaTransactionManager for global tx -->
  <bean id="transactionManager"
  	class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  	<property name="dataSource" ref="dataSource" />
  </bean>
  
  <!-- enable transaction demarcation with annotations -->
  <tx:annotation-driven />
  ...
  ```

  - `@Transactional`이 적용되어 있을 경우, 이 클래스에 트랜잭션 기능이 적용된 **프록시 객체가 생성**된다. 이 프록시 객체는 `@Transactional`이 포함된 메소드가 호출 될 경우, `PlatformTransactionManager`를 사용하여 트랜잭션을 시작하고, 정상 여부에 따라 Commit 또는 Rollback 한다.

  - 정상 여부는 RuntimeException이 발생했는지 기준으로 결정되며, RuntimeException 외 다른 Exception(대표적으로 SQLException이 있다.)에도 트랜잭션 롤백처리를 적용하고 싶으면 `@Transactional`의 rollbackFor 속성을 활용하면 된다.

  - 사용 예시

    ```java
    /**
    	 * <pre>
    	 *  
    	 *  설정시간이 지난 미결제 예약 레코드 삭제 및 이력 레코드 삽입 
    	 * 
    	 * </pre>
    	 * 
    	 * @throws Exception 오류가 발생할 경우 예외를 발생시킵니다.
    	 */
    	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
    	public int deleteExpiredReserve() throws Exception {
    		
    		int count = 0;
    		
    		Date nowTime = new Date();
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
    		
    		List<HashMap<String, Object>> m_reservCdList = reserveDAO.selectExpireReserveCdList(format.format(nowTime));
    		
    		List<String> reservCdList = new ArrayList<String>();
    		for(HashMap<String, Object> obj : m_reservCdList){
    			reservCdList.add(obj.get("RESERV_CD").toString());
    		}
    		
    		if(reservCdList.size() > 0){
    			reserveDAO.insertExpiredReserveMaster(format.format(nowTime));
    			count += reserveDAO.deleteExpiredReserveMaster(reservCdList);
    			count += reserveDAO.deleteExpiredReserve(reservCdList);				
    		}		
    		
    		return count;
    	}
    ```

  ```java
  @Transactional
  	@Override
  	public BoardVO getBoardContent(int bid) throws Exception{
  		BoardVO boardVO = new BoardVO();
  		boardDAO.updateViewCnt(bid);
  	//	boardVO = boardDAO.getBoardContent(bid);
  		try {
  			boardVO.setBid(bid);
  			boardVO.setCate_cd("1111111111111111111111111111111111111");
  			boardDAO.updateBoard(boardVO);
  		} catch (RuntimeException e) {
  			throw new NotFoundException();
  		}
  		return boardVO;
  	}
  ```



### `@Transactional` 속성

**isolation (격리레벨)**

- **DEFAULT**: DB 설정, 기본 격리 수준(기본설정)

- **SERIALIZABLE** : 가장 높은 격리, 성능 저하의 우려가 있음

- **READ_UNCOMMITED** : 커밋되지 않는 데이터에 대한 읽기를 허용

- **READ_COMMITED** : 커밋된 데이터에 대해 읽기 허용

- **REPEATEABLE_READ** : 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장

  설정 예: @Transactional(isolation=Isolation.DEFAULT)

**propagation (전파속성)**

- **PROPAGATION_MANDATORY** : 작업은 반드시 특정한 트랜잭션이 존재한 상태에서만 가능

- **PROPAGATION_NESTED** : 기존에 트랜잭션이 있는 경우, 포함되어서 실행

- **PROPAGATION_NEVER** : 트랜잭션 상황에 실행되면 예외 발생

- **PROPAGATION_NOT_SUPPORTED** : 트랜잭션이 있는 경우에는 트랜잭션이 끝날 때까지 보류된 후 실행

- **PROPAGATION_REQUIRED : 트랜젝션이 있으면 그 상황에서 실행, 없으면 새로운 트랜잭션 실행(기본설정)**

- **PROPAGATION_REQUIRED_NEW** : 대상은 자신만의 고유한 트랜잭션으로 실행

- **PROPAGATION_SUPPORTS** : 트랜젝션을 필요료 하지 않으나, 트랜잭션 상황에 있다면 포함되어서 실행

  설정 예: @Transactional(propagation=Propagation.REQUIRED)

**readOnly 속성**

- true인 경우 insert, update, delete 실행 시 예외 발생, 기본 설정은 false

  설정 예: @Transactional(readOnly = true)

**rollbackFor 속성**

- 특정 예외가 발생 시 강제로 Rollback

  설정 예: @Transactional(rollbackFor=Exception.class)

**noRollbackFor 속성**

- 특정 예외의 발생 시 Rollback 처리되지 않음

  설정 예: @Transactional(noRollbackFor=Exception.class)

**timeout 속성**

- 지정한 시간 내에 해당 메소드 수행이 완료되지 않은 경우 rollback 수행. -1일 경우 no timeout(Default=-1)

  설정 예: @Transactional(timeout=10)



# Swagger 적용하기

## Configuration

- `@EnableSwagger2` 어노테이션을 추가한다.

```java
import static springfox.documentation.builders.PathSelectors.*;
  import static com.google.common.base.Predicates.*;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("business-api")
            .select()
               //Ignores controllers annotated with @CustomIgnore
              .apis(not(withClassAnnotation(CustomIgnore.class)) //Selection by RequestHandler
              .paths(paths()) // and by paths
              .build()
            .apiInfo(apiInfo())
            .securitySchemes(securitySchemes())
            .securityContext(securityContext());
  }
```

- 최소 하나 이상의 Docket 인스턴스를 선언해야한다.
  - Docket 인스턴스에는 `@Bean` 애노테이션을 추가해야 한다.
- `@EnableSwagger` 애노테이션은 필수이다.

## Descriptions

- 아래는 Swagger를 통해 사용 가능한 description 애노테이션들이다.

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7494b85d-18a2-4cc8-a32e-e6b14a13a37d/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/7494b85d-18a2-4cc8-a32e-e6b14a13a37d/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T021934Z&X-Amz-Expires=86400&X-Amz-Signature=35f32f35ebd03bc0c02552a8b360a5c5fde5e16f609e77598cdf8398e99a1165&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

- 예시

  ```java
  @ApiOperation(value = "Find pet by Status",
        notes = "${SomeController.findPetsByStatus.notes}"...) 
    @RequestMapping(value = "/findByStatus", method = RequestMethod.GET, params = {"status"})
    public Pet findPetsByStatus(
        @ApiParam(value = "${SomeController.findPetsByStatus.status}", 
             required = true,...)
        @RequestParam("status",
            defaultValue="${SomeController.findPetsByStatus.status.default}") String status) { 
        //...
    }
  
    @ApiOperation(notes = "Operation 2", value = "${SomeController.operation2.value}"...) 
    @ApiImplicitParams(
        @ApiImplicitParam(name="header1", value="${SomeController.operation2.header1}", ...) 
    )
    @RequestMapping(value = "operation2", method = RequestMethod.POST)
    public ResponseEntity<String> operation2() {
      return ResponseEntity.ok("");
  }
  ```



## Swagger 적용 코드

- `SwaggerConfig.java`

  - 먼저 Swagger관련 configuration 코드부터 작성하였다.

  ```java
  package sudoku.sudoku_back.config;
  
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import springfox.documentation.builders.PathSelectors;
  import springfox.documentation.builders.RequestHandlerSelectors;
  import springfox.documentation.spi.DocumentationType;
  import springfox.documentation.spring.web.plugins.Docket;
  import springfox.documentation.swagger2.annotations.EnableSwagger2;
  
  @Configuration
  @EnableSwagger2
  public class SwaggerConfig {
  
      @Bean
      public Docket api() {
          return new Docket(DocumentationType.SWAGGER_2)
                  .select()
                  .apis(RequestHandlerSelectors.any())
                  .paths(PathSelectors.any())
                  .build();
      }
  
  }
  ```

- 이후 Controller에 Swagger 관련 코드를 추가하였다.

  ```java
  @RestController
  public class SudokuController {
      static Logger logger = LoggerFactory.getLogger(SudokuController.class);
  
      @GetMapping("/correct/{userSudoku}")
      @ApiImplicitParams({
              @ApiImplicitParam(name="userSudoku", value="사용자의 Sudoku 게임 플레이 결과", dataType="String", paramType="query", required=true, defaultValue="1 3 5 4 6 7 8 9 2 \\\\n6 8 7 1 2 9 4 3 5 \\\\n4 9 2 3 5 8 6 7 1 \\\\n2 1 4 5 3 6 7 8 9 \\\\n3 5 9 7 8 1 2 4 6 \\\\n8 7 6 2 9 4 1 5 3 \\\\n5 2 1 8 4 3 9 6 7 \\\\n7 6 8 9 1 5 3 2 4 \\\\n9 4 3 6 7 2 5 1 8 \\\\n")
      })
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
      @ApiImplicitParams({
              @ApiImplicitParam(name="id", value="스도쿠 게임 기록 id", dataType="int", paramType="query", required=true)
      })
      public void addSudoku(@RequestBody String sudokuData) throws JsonProcessingException, ParseException {
          ObjectMapper mapper = new ObjectMapper();
          JSONParser parser = new JSONParser();
          Object obj = parser.parse(sudokuData);
          JSONObject jsonObj = (JSONObject) obj;
          logger.info("스도쿠 기록을 추가합니다.");
          sudokuService.addSudoku(jsonObj);
      }
  
      @Transactional
      @DeleteMapping("/sudoku/{id}")
      @ApiImplicitParams({
              @ApiImplicitParam(name="id", value="스도쿠 기록 번호", dataType="int", paramType="query", defaultValue="1", required=true)
      })
      public void deleteSudoku(@PathVariable("id") int id) {
          logger.info("스도쿠 게임 기록 " + id + "번이 삭제됩니다.");
          sudokuService.deleteSudoku(id);
      }
  }
  ```

## Swagger 화면

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6cacc59e-f5c1-473a-acb2-f0046758e167/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/6cacc59e-f5c1-473a-acb2-f0046758e167/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T022009Z&X-Amz-Expires=86400&X-Amz-Signature=1f5485ea1a05c516be273a5cb2541e4b9a9898a5ea2eb7bc1dbbc1085eb15e3a&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d29f553b-1e77-4b37-9301-eafdaf7184a5/Untitled.png](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/d29f553b-1e77-4b37-9301-eafdaf7184a5/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210618%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210618T022018Z&X-Amz-Expires=86400&X-Amz-Signature=437401510f45fa5ef7abd7e8c9266c145038cfd1985a772ed3e128da9aafd365&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)



# 씬나는 에러 ^_^

- 무슨 이유인지는 모르겠지만 하단과 같은 에러가 지속적으로 났다.
- 이런 경우 해결 방법은,
  - File > Invalidate Caches 를 사용하거나,
  - .m2 폴더가 깔려있는 위치를 찾아서, .m2 폴더를 삭제하고, 프로젝트를 다시 열고 re-build 하는 방법이 있다.

```bash
.   ____          _            __ _ _
 /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\
( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\
 \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.6)

2021-06-01 16:52:12.010  INFO 6420 --- [           main] s.sudoku_back.SudokuBackApplication      : Starting SudokuBackApplication using Java 1.8.0_291 on K100210419051 with PID 6420 (C:\\Users\\clshin\\Desktop\\Sudoku\\Sudoku_Back\\target\\classes started by clshin in C:\\Users\\clshin\\Desktop\\Sudoku\\Sudoku_Back)
2021-06-01 16:52:12.013  INFO 6420 --- [           main] s.sudoku_back.SudokuBackApplication      : No active profile set, falling back to default profiles: default
2021-06-01 16:52:12.689  INFO 6420 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-06-01 16:52:12.694  INFO 6420 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-06-01 16:52:12.694  INFO 6420 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.46]
2021-06-01 16:52:12.771  INFO 6420 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-06-01 16:52:12.771  INFO 6420 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 731 ms
2021-06-01 16:52:12.777 ERROR 6420 --- [           main] o.s.b.web.embedded.tomcat.TomcatStarter  : Error starting Tomcat context. Exception: org.springframework.beans.factory.BeanCreationException. Message: Error creating bean with name 'formContentFilter' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.web.servlet.filter.OrderedFormContentFilter]: Factory method 'formContentFilter' threw exception; nested exception is java.lang.NoClassDefFoundError: Could not initialize class com.fasterxml.jackson.databind.ObjectMapper
2021-06-01 16:52:12.791  INFO 6420 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2021-06-01 16:52:12.796  WARN 6420 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
2021-06-01 16:52:12.802  INFO 6420 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-06-01 16:52:12.819 ERROR 6420 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:162) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:577) ~[spring-context-5.3.7.jar:5.3.7]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:771) [spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:763) [spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:438) [spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) [spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) [spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1318) [spring-boot-2.4.6.jar:2.4.6]
	at sudoku.sudoku_back.SudokuBackApplication.main(SudokuBackApplication.java:12) [classes/:na]
Caused by: org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:142) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:104) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(TomcatServletWebServerFactory.java:450) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatServletWebServerFactory.java:199) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:181) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:159) ~[spring-boot-2.4.6.jar:2.4.6]
	... 9 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'formContentFilter' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.web.servlet.filter.OrderedFormContentFilter]: Factory method 'formContentFilter' threw exception; nested exception is java.lang.NoClassDefFoundError: Could not initialize class com.fasterxml.jackson.databind.ObjectMapper
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:658) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:486) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1334) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1177) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:564) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:524) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:213) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.boot.web.servlet.ServletContextInitializerBeans.getOrderedBeansOfType(ServletContextInitializerBeans.java:212) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(ServletContextInitializerBeans.java:175) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAsRegistrationBean(ServletContextInitializerBeans.java:170) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.ServletContextInitializerBeans.addAdaptableBeans(ServletContextInitializerBeans.java:155) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.ServletContextInitializerBeans.<init>(ServletContextInitializerBeans.java:87) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getServletContextInitializerBeans(ServletWebServerApplicationContext.java:259) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.selfInitialize(ServletWebServerApplicationContext.java:233) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.web.embedded.tomcat.TomcatStarter.onStartup(TomcatStarter.java:53) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5161) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1384) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1374) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.util.concurrent.FutureTask.run(FutureTask.java:266) ~[na:1.8.0_291]
	at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:134) ~[na:1.8.0_291]
	at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:909) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:829) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1384) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1374) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.util.concurrent.FutureTask.run(FutureTask.java:266) ~[na:1.8.0_291]
	at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:134) ~[na:1.8.0_291]
	at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:909) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:262) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardService.startInternal(StandardService.java:433) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:930) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.startup.Tomcat.start(Tomcat.java:486) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:123) ~[spring-boot-2.4.6.jar:2.4.6]
	... 14 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.web.servlet.filter.OrderedFormContentFilter]: Factory method 'formContentFilter' threw exception; nested exception is java.lang.NoClassDefFoundError: Could not initialize class com.fasterxml.jackson.databind.ObjectMapper
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185) ~[spring-beans-5.3.7.jar:5.3.7]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-5.3.7.jar:5.3.7]
	... 55 common frames omitted
Caused by: java.lang.NoClassDefFoundError: Could not initialize class com.fasterxml.jackson.databind.ObjectMapper
	at org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.build(Jackson2ObjectMapperBuilder.java:678) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.http.converter.json.MappingJackson2HttpMessageConverter.<init>(MappingJackson2HttpMessageConverter.java:59) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter.<init>(AllEncompassingFormHttpMessageConverter.java:91) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.web.filter.FormContentFilter.<init>(FormContentFilter.java:61) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.boot.web.servlet.filter.OrderedFormContentFilter.<init>(OrderedFormContentFilter.java:29) ~[spring-boot-2.4.6.jar:2.4.6]
	at org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.formContentFilter(WebMvcAutoConfiguration.java:174) ~[spring-boot-autoconfigure-2.4.6.jar:2.4.6]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_291]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_291]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_291]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_291]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) ~[spring-beans-5.3.7.jar:5.3.7]
	... 56 common frames omitted

Process finished with exit code 1
```



# 참고자료

- https://congsong.tistory.com/15
- https://smujihoon.tistory.com/221
- https://chojpsh1.tistory.com/114
- DB Insert
  - https://nakanara.tistory.com/225
  - https://attacomsian.com/blog/jackson-create-json-object
- Auto Increment
  - http://blog.naver.com/PostView.nhn?blogId=imf4&logNo=220762181574
- Hikari
  - https://bamdule.tistory.com/166