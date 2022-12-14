# Annotare 
> 써내려가다

## 사용 기술 스택
* Language : Java17
* DB : MySql
* ORM : Spring Data Jpa
* Spring Security
* LomBok
* Gradle
* Spring Boot 2.7.5

## 설명
* 온전한 text based 플랫폼
* 글을 쓰고 읽는 것이 즐거운 사람들을 위한 플랫폼
* 일상과 다양한 것들을 글로 공유하는 플랫폼
* 카카오의 브런치에서 영감을 많이 받음.
* 브런치와 다른점은 글을 쓰는 작가 자체가 브랜드가 됨.(작품이 존재하지 않음)
* rest-api 서버
* 화면을 위한 설계가 같이 녹아있음.

## 설계
* 권한은 두개 MEMBER와 ADMIN이 있다.
* 글은 모두가 작성가능하고, 모두가 볼 수있음(인증된 사용자 중).
* 게시글에는 조회수, 좋아요, 카테고리가 있다.
* 전체 게시판(홈)에는 새로올라온 순서대로 페이징 정렬(id desc)
* 카테고리 게시판 에는 좋아요를 기준으로 페이징 정렬(good desc)
* 검색에서는 조횟수를 기준으로 페이징 정렬(view desc)
* 팔로잉 기능이있음. 팔로잉 테이블 존재
* 마이페이지/작가페이지 존재
* 작가 페이지에서 팔로잉이 가능함.
* 게시글마다 댓글이 존재하며, 댓글은 등록, 수정, 삭제가 가능하다.
* 게시글, 댓글 모두 뷰(화면단)과 서버단 모두에서 작성자와 현재 유저 판별을 한다.

## 상세 설명
### map 으로 객체 전송시 규약
* Map<String, Object> 형식이다. 아래는 string 네이밍 규칙을 작성했다.
* 현재 유저 : user
* 현재 보낼 객체(데이터) : body
* 나머지 이름은 키와 값의 이름이 동일.
### 연관관계
* Comment & Board -> ManyToOne 단방향
* Board & Users -> ManyToOne 단방향
* Follow & Users -> ManyToOne 단방향
### 카테고리
```
카테고리는 
여행, 시사, it, 직장, 운동, 마케팅, 동물, 미정 이 있다. 
이중 미정에 대한 카테고리 게시판은 존재하지 않는다.
카테고리는 navbar와 같은곳에 <li>의 형태로 넣어두고,
게시글을 작성할 때 select box를 사용하여 입력받는다. 
이 카테고리의 범위를 넘어서지 않는다. 
카테고리가 없을경우 null로 하는것이 아니라 미정을 선택하여 저장한다.
select box에 selected를 미정으로 놓으면 아무것도 선택하지 않을시 미정으로 입력된다.
postman에서는 한글이 깨진다. 인코딩 하는 것이 귀찮으니 테스트 할때에는 영어로 바꾸어서 테스트했다.
ex) : 여행 -> travel 등
```
### 댓글 구조
* 댓글은 최상단에 form과 textarea로 댓글을 작성할 수 있는 공간이 있다.
* 그 밑에는 댓글 리스트가 존재하며, 작성자가 확인되면 수정/삭제 가능하다.
* 댓글을 유저와 연관관계를 걸지 않은 이유는 딱히 댓글을 가져오면서 유저정보에서 가져올 만한게 없기때문이다.
* 댓글 수정, 삭제시 이미 뷰단에서 작성자와 서버에서 같이 보낸 현재 객체를 판별하는것이 끝났다.
* 그렇지만 수정과 삭제는 민감한 것이기 때문에 서버 상에서 한 번 더 판별해준다.
* 다를경우 FORBIDDEN 403 에러코드를 보낸다.
### 팔로잉
* users(나)와 following(내가 팔로우 하는사람) 칼럼으로 나뉨.
* 한개의 테이블로 팔로잉 처리가 가능하다.
* 팔로잉은 pathVariable로 상대 이메일 받는것이 전부라 dto가 필요없다.
* 팔로잉 끊기가 가능하며 팔로우를 끊으면 삭제쿼리 나감. 
* 팔로우 끊기는 마이페이지를 위한 팔로잉&팔로우 리스트에서 한다.
* 마이페이지에서 접근 할 경우 principal로 현재 유저를 가져옴.
* 작가 페이지에서 접근 할 경우 작가의 이름이 pathvariable로 입력되어야한다.
* 즉 작가페이지를 위한 팔로잉&팔로우 리스트와 마이페이지를 위한 팔로잉&팔로우 리스트가 있어야한다.
* 팔로잉&팔로우 데이터를 넘겨줄때는 값을 string으로 변환하여 리스트 형식으로 넘겨준다.
### 마이페이지/작가페이지
* 작가를 클릭하여서 접근할때에는 작가페이지로 이동된다.
* 작가페이지에는 작가가 쓴 게시글을 보여준다. 
* 마이페이지와 달리 pathvariable로 입력받음.
* 마이페이지는 내가 작성한 글이 보여지고, 팔로잉 리스트, 팔로우 리스트로 이동가능.
* 마이페이지는 작가페이지와 달리 principal로 유저 정보에 접근한다.
* 마이페이지와 작가페이지 모두 페이징이 들어간다.
* 정렬기준은 생성날짜(id desc)이다.

## 나의 고민
### 사진 고민
* 사진 업로드에 대해 많이 고민해보았다.
* 개인적으로 온전한 text based 플랫폼을 정말 좋아함.
* 여러가지 이유가 있지만 진솔하고 편하게 전달하는 것이 text의 매력이라 생각함.
* 그렇지만 그런 플랫폼이 많지는 않음.
* 이런 플랫폼을 사용하고 싶다는 생각이 들었는데, 이러한 이유로 만들기로 함.
### 마크다운과 같은 기능 고민
* 마크다운을 지원하거나 하는 등의 기능이 있다면 너무나도 좋겠지만,
* 지금은 그런 기능은 제외하겠음

## json body
### users
```
{
    "email" : "yc1234@gmail.com",
    "password" : "1234"
}
{
    "email" : "ms1234@gmail.com",
    "password" : "1234"
}
{
    "email" : "admin@annotare.com",
    "password" : "1234"
}
```
### board
```
{
    "title" : "test1",
    "content" : "test content.",
    "category" : "travel"
}
{
    "title" : "updated test",
    "content" : "updated content.",
    "category" : "travel"
}
```
### comment
```
{
    "content" : "this is comment"
}
{
    "content" : "updated comment"
}
```

## api
### users
* /
* /user/signup
* /user/login
* /user/mypage
* /user/writer/{writer}
* /user/prohibition
* /admin
### board
* /board - get
* /board/search - get
* /board/category/{category} - get
* /board/post - get/post
* /board/{id} - get
* /board/good/{id} - post
* /board/edit/{id} - get/post
* /board/delete/{id} - post
### comment
* /comment/{boardId} - get
* /comment/post/{boardId} - post
* /comment/edit/{id} - get/post
* /comment/delete/{id} - post
### follow
* /follow/{email} - post
* /follow/myfollow - get
* /unfollow/{email} - post
* /follow/myfollower - get
* /follow/writerfollow/{writer} - get
* /follow/writerfollower/{writer} - get

## DB ERD diagram
![스크린샷(137)](https://user-images.githubusercontent.com/88976237/198957120-4ff7705c-a355-48b4-bbc7-9dde7fcc1dbb.png)