<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Carousel Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Custom styles for this template -->
    <link rel="stylesheet" type="text/css" href="${ctx }/resources/css/login.css" />
  </head>
<!-- NAVBAR
================================================== -->
  <body>
    <div class="header">
		<div class="headerWrap">
			<div class="loginLogo">
				<a href="javascript:;" target="_self">
					<img src="${ctx}/resources/images/login/logo.png" height="60" alt="136旅游" title="136旅游">
				</a>
			</div>
			<div class="city" id="toggle-city">
				<a href="javascrip:;">
					<i class="icon-location-5"></i>${userCity.localCity} <label>切换</label>
				</a>
				<div class="more-city">
					<c:forEach items="${citys}" var="city">
						<a href="${ctx}/changeCity?cityId=${city.CITY_ID }">${fn:replace(city.SUB_AREA,'市', '') }</a>
					</c:forEach>
				</div>
			</div>
			<nav class="contact">
				<span class="icon-headphones">${siteAttr.SERVICE_PHONE}</span>  
				<span>周一至周日 8:00-18:00</span>
			</nav>	
			<nav class="nav">
			</nav>
		</div>
	</div>


    <!-- Carousel
    ================================================== -->
    <div class="container">
    	<!-- 焦点图 -->
		<div class="focusImg" id="js_focus">
			<ul class="sliderCon">
				<li style="display:block;background:url(${ctx}/resources/images/ad/focusimg.png) no-repeat center 0;">
					<a href="javascript:;" ></a>
				</li>
				<li style="display:block;background:url(${ctx}/resources/images/ad/focusimg01.jpg) no-repeat center 0;">
					<a href="javascript:;" ></a>
				</li>
				<c:forEach items="${ads}" var="ad">
				<li style="display:block;background:url(${visit}/${ad.PATH }) no-repeat center 0;">
					<a href="${ad.HREF }" target="_blank"></a>
				</li>
				</c:forEach>
			</ul>
			<div class="control" id="js_focus_control">
				<a href="javascript:;" class="prev">前一个</a>
				<a href="javascript:;" class="next">后一个</a>
			</div>
		</div>
		<!-- 登录框 -->
		<div class="loginWrap">
			<div class="loginBox">
				<h1>欢迎登录136旅游!</h1>
				<div class="input-cell">
					<span class="icon-user-1 inputIcon"></span>
					<label>用户名/手机号</label>
					<input type="text" class="inputFields" id="userName">
					<b></b>
					<div class="toolTip" id="login_err">
						<div class="tipArrow"></div>
						<div class="tipInner" id="login_err_msg">用户名或密码不正确！</div>
					</div>
				</div>
				<div class="input-cell">
					<span class="icon-lock inputIcon"></span>
					<label>密码</label>
					<input type="password" class="inputFields" id="passWord">
				</div>
				<div class="login-status">
					<input type="checkbox" id="autoLogin"/>
					<label class="checkbox" id="cookieloginck" style="background-repeat:repeat-x;"></label>
					<label>7天内免登录</label>
					<a href="${ctx}/forget" target="_blank">找回密码</a>
				</div>
				<div class="input-cell btns">
				<a href="javascript:;" class="btnSign" id="btn_login">登录</a>
				<a href="${ctx}/regist" target="_blank" class="btnSign reg">入驻136</a>
				</div>
			</div>
		</div>
	</div>

    <!-- Marketing messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container marketing">

      <!-- Three columns of text below the carousel -->
      <div class="row">
        <div class="col-lg-4">
          <img class="img-circle" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" width="140" height="140">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna.</p>
          <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-4">
          <img class="img-circle" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" width="140" height="140">
          <h2>Heading</h2>
          <p>Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Cras mattis consectetur purus sit amet fermentum. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh.</p>
          <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-4">
          <img class="img-circle" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" width="140" height="140">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
          <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
      </div><!-- /.row -->


      <!-- START THE FEATURETTES -->

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-7">
          <h2 class="featurette-heading">First featurette heading. <span class="text-muted">It'll blow your mind.</span></h2>
          <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
        </div>
        <div class="col-md-5">
          <img class="featurette-image img-responsive center-block" data-src="holder.js/500x500/auto" alt="Generic placeholder image">
        </div>
      </div>

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-7 col-md-push-5">
          <h2 class="featurette-heading">Oh yeah, it's that good. <span class="text-muted">See for yourself.</span></h2>
          <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
        </div>
        <div class="col-md-5 col-md-pull-7">
          <img class="featurette-image img-responsive center-block" data-src="holder.js/500x500/auto" alt="Generic placeholder image">
        </div>
      </div>

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-7">
          <h2 class="featurette-heading">And lastly, this one. <span class="text-muted">Checkmate.</span></h2>
          <p class="lead">Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.</p>
        </div>
        <div class="col-md-5">
          <img class="featurette-image img-responsive center-block" data-src="holder.js/500x500/auto" alt="Generic placeholder image">
        </div>
      </div>

      <hr class="featurette-divider">

      <!-- /END THE FEATURETTES -->


      <!-- FOOTER -->
      <footer>
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>&copy; 2014 Company, Inc. &middot; <a href="#">Privacy</a> &middot; <a href="#">Terms</a></p>
      </footer>

    </div><!-- /.container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  </body>
</html>