$(function(){
	var $windowTool = $('.window-tool');
	var toolWin = $('.toolbar-window');
	var $consultTool = $('.consult-tool');
	var toolCon = $('.toolbar-consult');
	var $qrTool = $('.qr-tool');
	var qrImg = $('.qr-img');
	
	var $qrTool1 = $('.qr-tool1');
	var qrImg1 = $('.qr-img1');
	
		$(window).scroll(function () {
			var scrollHeight = $(document).height();
			var scrollTop = $(window).scrollTop();
			var $windowHeight = $(window).innerHeight();
			scrollTop > 50 ? $("#scrollUp").fadeIn(200).css("display","block") : $("#scrollUp").fadeOut(200);			
		});
		$('#scrollUp').click(function (e) {
			e.preventDefault();
			$('html,body').animate({ scrollTop:0});
		});
		$windowTool.hover(function () {
			toolWin.fadeIn();
		}, function(){
			toolWin.fadeOut();
		});
		$consultTool.hover(function () {
			toolCon.fadeIn();
		}, function(){
			toolCon.fadeOut();
		});
		$qrTool.hover(function () {
			qrImg.fadeIn();
		}, function(){
			qrImg.fadeOut();
		});
		
		$qrTool1.hover(function () {
			qrImg1.fadeIn();
		}, function(){
			qrImg1.fadeOut();
		});
});

