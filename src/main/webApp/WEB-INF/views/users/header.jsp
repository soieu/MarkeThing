<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>test</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

    <!-- Custom styles template-->
    <link href="/static/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="/static/js/popper.min.js" rel="stylesheet">
    <!-- sweetalert -->
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <!-- icon CDN-->
    <link rel='stylesheet'
          href='https://cdn-uicons.flaticon.com/2.1.0/uicons-solid-rounded/css/uicons-solid-rounded.css'>
</head>

<style>
  #accordionSidebar {
    height: 100%;
    position: fixed;
    top: 0;
    z-index: 2;
    overflow-y: scroll;
  }
  #accordionSidebar::-webkit-scrollbar{
    width: 0;
  }
  #content-wrapper{
    width: 100%;
    position: fixed;
    top: 0;
    z-index: 1;
  }
  .contentField{
    padding-left: 250px;
    margin-top: 80px;
  }
  .heightcontrol{
    max-height: 200px;
    overflow-y: scroll;
  }
  .heightcontrol::-webkit-scrollbar{
    width: 0;
  }
  .alarmclick:hover{
    cursor: pointer;
  }
  .alarmDel:hover{
    cursor: pointer;
  }
  #alarmDiv{
    display: none;
    position: fixed;
    width: 280px;
    right: 30px;
    bottom: 10px;
    z-index: 100;
  }
  #alarmText{
    width: 220px;
  }

</style>

<body id="page-top">

<!-- Page Wrapper -->



<div id="content-wrapper" class="d-flex flex-column">

    <!-- Topbar -->
    <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

        <div class="sidebar-brand-icon">
            <img src="/static/img/Group 45.png" style="width: 200px;"/>
        </div>
        <!-- Topbar Navbar -->
        <ul class="navbar-nav ml-auto">

            <!-- 상단바 알림 -->
            <li class="nav-item dropdown no-arrow mx-1">
                <a  class="nav-link dropdown-toggle" href="#" id="alertsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="false">
                    <i class="fas fa-bell fa-fw"></i>
                    <!-- 알림 뱃지- Alerts -->
                    <span class="badge badge-danger badge-counter" id="alarmCnt">1</span>
                </a>

                <!-- 알림 드롭다운 -->
                <div	id="alarmDrop" class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="alertsDropdown">
                    <h6 class="dropdown-header">알림</h6>
                    <div class="dropdown-item text-center small text-gray-500">알림이 없습니다.</div>
                </div>
            </li>

            <!-- 알림/유저 아이콘 선-->

            <div class="topbar-divider d-none d-sm-block"></div>

            <!-- Nav Item - User Information -->
            <li class="nav-item dropdown no-arrow">
                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="mr-2 d-none d-lg-inline text-gray-600 small"></span>
                </a>

                <!-- Dropdown - User Information -->
                <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                    <a class="dropdown-item" href="">
                        <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                        내정보
                    </a>
                    <a class="dropdown-item" href="">
                        <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                        근태기록
                    </a>
                    <a class="dropdown-item" href="">
                        <i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
                        주소록
                    </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item"  onclick="logoutConfirm()" style="cursor:pointer;">
                        <i	class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                        로그아웃
                    </a>
                </div>
            </li>

        </ul>

    </nav>

</div>

<!-- 여기까지 사이드/상단바 -->

<!-- 페이지 상단 이동버튼-->
<a class="scroll-to-top rounded" href="#page-top" style="display: list-item">
    <i class="fas fa-angle-up"></i>
</a>


<!-- 알림오면 생기는 창 -->
<div id="alarmDiv" class="alert alert-primary" role="alert">
    <div id="alarmText" class="float-left"></div>
    <div class="float-right alarmDel"><i class="fas fa-times" onclick="alarmHidden()"></i></div>
</div>



!-- 	</div>



<script	src="/static/js/bootstrap.bundle.min.js"></script>
<script src="/static/js/jquery.easing.min.js"></script>
<script src="/static/js/sb-admin-2.min.js"></script>
</body>

<script>
  // 알람창


  eventSource.addEventListener('connect', function(event) {
    console.log(event.data);
  });

  eventSource.addEventListener('alarm', function(event) {
    //console.log(event.data);
    $('#alarmText').html(event.data);
    $('#alarmDiv').css({'display':'block'});
    setTimeout(function () {
      alarmHidden();
    }, (1000*60));
    alarmList();
  });

  function alarmHidden(){
    $('#alarmDiv').css({'display':'none'});
  }

  alarmList();

  function alarmList() {
    $.ajax({
      type : 'post',
      url : '<c:url value="/alarm/getList"/>',
      data : {},
      success : function(data) {
        //console.log(data);
        drawAlarm(data.alarmList);
      },
      error : function(e) {
        console.log(e);
      }
    });
  }

  function drawAlarm(list) {

    $('#alarmCnt').html(list.length);

    $('#alarmDrop').html('');

    var dropdown = '<h6 class="dropdown-header">알림</h6>';

    if(list.length == 0){
      dropdown += '<div class="dropdown-item text-center small text-gray-500">알림이 없습니다.</div>';
    }else{
      dropdown += '<div class="heightcontrol">';
      for(var i = 0; i < list.length; i++){

        var con = '<div class="dropdown-item d-flex align-items-center"><div class="mr-3">';

        if(list[i].url.includes('/approval')){
          con += '<div class="icon-circle bg-success"><i class="fi fi-sr-assept-document text-white">';
        }else if(list[i].url.includes('/schedule')){
          con += '<div class="icon-circle bg-info"><i class="fi fi-sr-calendar-pen text-white">';
        }else{
          con += '<div class="icon-circle bg-light"><i class="far fa-bell text-black">';
        }

        con += '</i></div></div><div class="alarmclick mr-5" style="width: 100%" onclick="location.href=\'';
        con += '/Cocean'+list[i].url+'\'"><div class="small text-gray-500">';
        con += list[i].notificationTime;
        con += '</div><div>';
        con += list[i].content;
        con += '</div></div><div class="float-right alarmDel"><i class="fas fa-times hover" onclick="alarmDel('
        con += list[i].historyID;
        con += ')"></i></div></div>';
        dropdown += con;
      }
      dropdown +='</div><div class="dropdown-item text-center small text-gray-500 alarmDel" onclick="alarmDel(\'all\')">모두 삭제</div>';
    }
    $('#alarmDrop').html(dropdown);

  }

  function alarmDel(id){
    var url = '<c:url value="/alarm/del/"/>'+employeeID+'/'+id;
    if(id == 'all'){
      url = '<c:url value="/alarm/allDel/"/>'+employeeID;
    }

    $.ajax({
      type:'get',
      url:url,
      data:{},
      dataType:'JSON',
      success:function(data){
        drawAlarm(data.alarmList);
      },
      error:function(e){
        console.log(e);
      }
    });
  }


  var currentPathName = location.pathname;
  var currentAction = currentPathName.split('/')[2];
  if(currentAction == 'board'){
    $('#board-click').click();
  }else if(currentAction == 'approval'){
    $('#approval-click').click();
  }else if(currentAction == 'personnel'){
    $('#personnel-click').click();
  }else if(currentAction == 'store'){
    $('#store-click').click();
  }

</script>

</html>


















