<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>장보기 구매 대행 의뢰서 작성</title>
</head>
<body>
<h3>장보기 구매 대행 의뢰서</h3>
<div>
    <p>시장 정보</p>
    <p>약속 장소</p>
    <div id="map" style="width:100%;height:350px;"></div>
    <p><em>약속 장소를 지도에서 클릭해주세요!</em></p>
    <div id="clickLatlng"></div>

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoMapKey}"></script>
    <script>
      var mapContainer = document.getElementById('map'), // 지도를 표시할 div
          mapOption = {
            center: new kakao.maps.LatLng(${latlon[1]},${latlon[0]}), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
          };

      // 지도 생성
      var map = new kakao.maps.Map(mapContainer, mapOption);

      // 지도를 클릭한 위치에 표출할 마커
      var marker = new kakao.maps.Marker({
        // 지도 중심좌표에 마커 생성
        position: map.getCenter()
      });
      // 지도에 마커 표시
      marker.setMap(map);

      // 지도에 클릭 이벤트를 등록
      // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출
      kakao.maps.event.addListener(map, 'click', function(mouseEvent) {

        // 클릭한 위도, 경도 정보를 가져옵니다
        var latlng = mouseEvent.latLng;

        // 마커 위치를 클릭한 위치로 이동
        marker.setPosition(latlng);

        var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
        message += '경도는 ' + latlng.getLng() + ' 입니다';

        var resultDiv = document.getElementById('clickLatlng');
        resultDiv.innerHTML = message;

      });
    </script>
</div>
</body>
</html>
