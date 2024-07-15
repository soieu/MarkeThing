<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>선택한 시군구의 시장 마커 표시</title>
</head>
<body>
<p>${area}에는 총 ${testList.size()}개의 시장이 있습니다.</p>
<div id="map" style="width:100%;height:350px;"></div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoMapKey}&libraries=services"></script>
<script>
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
      mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 9// 지도의 확대 레벨
      };

  // 지도 생성
  var map = new kakao.maps.Map(mapContainer, mapOption);

  // 주소-죄표 변환 객체 생성
  var geocoder = new kakao.maps.services.Geocoder();

  // 시군구로 좌표 검색
  geocoder.addressSearch('${area}', function (result, status) {

    // 정상적으로 검색이 완료됐으면
    if(status === kakao.maps.services.Status.OK) {
      // 받은 결과값 저장
      var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
    }
    // 지도의 중심을 결과값의 위치로 이동
    map.setCenter(coords);

  });
  // 마커를 표시할 좌표와 내용 가지고 있는 객체 배열 생성
  var positions= []

  // 시장 리스트 전달받아서 보여주기
  <c:forEach items="${testList}" var="item">
  // 전달받은 시장의 정보들을 배열에 저장
  positions.push({
    content:'<div>${item.name}</div>'+'<div>${item.type}</div>'+'<div>${item.streetAddress}</div>',
    name:'${item.name}',
    latlng: new kakao.maps.LatLng(${item.lat}, ${item.lon})
  })
  </c:forEach>


  for (var i = 0; i < positions.length; i ++) {
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: positions[i].latlng, // 마커의 위치
      clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트 발생하지 않도록 설정
    });

    // 마커에 표시할 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
      content: positions[i].content,// 인포윈도우에 표시할 내용
      name: positions[i].name
    });

    // 마커에 mouseover 이벤트와 mouseout 이벤트 등록
    // 이벤트 리스너로는 클로저 만들어 등록
    // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트 등록
    kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
    kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
    kakao.maps.event.addListener(marker,'click',clickListener(positions[i].name));
  }

  // 인포윈도우를 표시하는 클로저 생성
  function makeOverListener(map, marker, infowindow) {
    return function() {
      infowindow.open(map, marker);
    };
  }

  // 인포윈도우를 닫는 클로저 생성
  function makeOutListener(infowindow) {
    return function() {
      infowindow.close();
    };
  }
  // 마커 클릭 시 이벤트 발생
  function clickListener(name) {
    return function () {
      console.log(name);
      window.open("/popup/"+name, "create", "width=400, height=300, left=100, top=50");
    }
  }
</script>
</body>
</html>
