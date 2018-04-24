$(document).ready(function () {
  $('[data-toggle="offcanvas"]').click(function () {
    $('.row-offcanvas').toggleClass('active')
  });

 $("#carouseInPage").click(function(){
    $("#centerMain").empty();
   $("#centerMain").load("./views/carousel/carousel.html");

/*    var html = "<p>hello</p> <img  src = \"./resources/img/coder/1.jpg\"/>";
    $("#centerMain").html(html);*/
 });

  $("a.sidebar-link").click(function(){
    $(".sidebar-link").filter(".active").removeClass("active");
    $(this).addClass("active");
    if($(this).attr("id") == "sidebar_baidu"){
      alert("你点击了"+$(this).text()+",我要跳出去了");
    }else{
      alert("你点击了"+$(this).text()+",然而我并没有做什么");
    }
    
 });

});