개발 공통 사항
1. 주석 
   - 변수 및 메소드에는 /** 명칭 */ javadoc 형태로 주석처리
   - 이외 주석처리의 경우에는 윗줄에 /* */ 주석처리

2. URL 명명규칙
   - 어절의 구분은 Dash(-)로 한다.
   - 축약하여 사용하지 않는다. (checkupRoster -> roster(x))
   - 데이터베이스상 명명된 필드이름 기준으로 작성한다.
   - 조회(단건, 다건), 생성, 수정, 삭제로 구성되고 구분은 Method(GET, POST, PUT, DELETE)로 구분하며 PATCH는 사용하지 않는다
    : 선등록조회 단건 > GET /checkup-roster/{checkupRosterNo}
    : 선등록조회 다건 > GET /checkup-roster-list
    : 선등록 등록 > POST /checkup-roster
    : 선등록 수정 > PUT /checkup-roster
    : 선등록 삭제 > DELETE /checkup-roster
  - KEY를 사용할수 있는경우는 PATH변수를 사용하고, 그외에는 REQUESTPARAM을 사용한다.

3. Method 명명규칙
   - 카멜케이스로 작성
   - 조회(단건, 다건), 생성, 수정, 삭제로 구분
    : 선등록조회 단건 > selectCheckupRoster
	: 선등록조회 다건 > selectCheckupRosterList
	: 선등록 등록 > insertCheckupRoster
	: 선등록 수정 > updateCheckupRoster
	: 선등록 삭제 > deleteCheckupRoster
	
4. 모델 Class 작성 규칙
   - @Data 어노테이션을 사용한다.
   - @Schema 어노테이션 작성
   - javadoc 스타일의 변수명 주석

5. 파라미터
   - Data : model.data 패키지 > *Dto.java
   - In : model.request 패키지 > *Request.java
   - Out : model.response 패키지 > *Response.java
   - Out 파라미터의 경우에는 VitaResponse<?> 생성자에 담아서 반환
   
6. Mapper xml 쿼리 작성규칙
   - 쿼리는 전체 대문자로 작성한다.
   - 따로 포맷은 지정하지 않으나 가독성 있도록 들여쓰기 등을 이용한다.

7. 구조
   - Controller에 로직을 작성하지 않고 return service 형태로 작성하되, 여러가지 서비스 호출할 경우에는 예외로 한다.
   - ServiceImpl에 공통적으로 트랜잭션을 부여한다.
   - 트랜잭션 분리가 필요한경우 Controller에서 서비스를 분리해서 호출한다.
   - 기능단위로 구분하여 BizUtil을 만들고, @Component를 사용하여 mapper를 주입하여 DB처리한다. 
   - BizUtil은 트랜잭션처리하지 않고 서비스의 트랜잭션을 따른다.
   - Vue, 외부와 직접 통신하지 않고 Connector를 통해 통신한다. 통신유틸제공.

2025.01.22 : 초안작성
  
