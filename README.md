## corona app

2월 4일 

workshop

- android app과 서버를  연동 하고 오늘 학습한 위치기반 서비스와 지도 보여주기 를 활용한 주제를 선정한다

> 대중적으로 아니 전 세계적으로 신종 코로나 바이러스가 유행하면서 사람들이 방역에 관심을 가지면서 코로나 맵이라는 웹사이트가 등장했다 여기서 모티브를 얻어 우리는 corona adroid app을 만들어보기로 했다.

- 시스템 구성도 

> ![그림1](https://user-images.githubusercontent.com/58680521/90516540-19f8b200-e19f-11ea-82cf-954d10ca66d6.png)
>
> 서버가 따로 없기 떄문에 eclipse와 톰캣을 이용한다. MainActivity에서 Login 유효성 검사는 qqq/111 로 임의로 지정하여 값 비교  SecondActivity에 Adapter를 이용해 ListView에 list를 형성 listview 에안에  담긴  JSON 데이터는 실 데이터를 보고 장소의 위도, 경도 좌표를 직접 가져와서 작성 map에 좌표를 찍는다

- UI

  > ![image-20200208144121403](C:\Users\student\AppData\Roaming\Typora\typora-user-images\image-20200208144121403.png)

- 특징

> | Activity 명   | 내용 및 특징                                                 |
> | ------------- | ------------------------------------------------------------ |
> | MainActivity  | 1.메인화면 및 로그인 화면 ( login.jsp 와의 통신을 확인)                      2.메인화면 우하단 날짜는 유저(안드로이드폰) 의 systemdate 를 받아와서 표시 à 추 후 데이터 업데이트 날짜로 변경할 예정                               3. 로그인 성공 시 SecondActivity 실행                                                   4.실패 시 Toast 로 Login Denied 출력                                                     5.통신 실패 시 Dialog가 일정시간 뜨다가 앱이 종료 |
> | SecondActiviy | 1.Getdata 함수를 만들어 AWS 와 통신한다. AWS에는 확진자의 정보를 담고 있는 corona.jsp 파일이 있고, 그 안의 JSON data 를 가져온다.  2. 가져온 JSON data 를 ArrayList  (List)로 담아준다.                            3.가져온 데이터 중 위치 정보만을 따로 빼서 다른 ArrayList(List2)로 담는다.                                                                                                        4.ListView에 어댑터로 가져온 데이터 기반으로 리스트(List)를 넣어 만들고, 그 리스트에 (OnclickListner)를 만들어 position  값에 맞는 데이터를 다음 Activity ( MapsAcitiviy) 로 보내준다.                                          5.제일 상단에 있는 버튼은 클릭 시 위치정보만을 따로 빼서 만든 ArrayList(List2) 값을 다음 Activity 로 보내준다. |
> | MapsActivity  | 1.받아온 정보를 기반으로 맵에 마커를 찍어서 보여준다.                    2.자신의 위치 표시하는 마커가 있고 , 그 주변 반경에 원이 생성된다.   3.마커를 선택하면 그 장소 이름과 그 장소까지의 거리가 snippet 으로 나온다.                                                                                                    4.위치가 바뀌면 거리가 다시계산되어 찍힌다.                                         5.자신의 위치정보는 7초간격으로 가져와 반영한다. |

