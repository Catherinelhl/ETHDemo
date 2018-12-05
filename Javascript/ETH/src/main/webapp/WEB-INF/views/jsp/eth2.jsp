<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/resources/css/eth.css">
	<!-- js -->
	<!-- tools [Start] -->
	<script src="/resources/js/tools/jquery-1.12.4.min.js"></script>
	<script src="/resources/js/tools/jquery.hoverDelay.js"></script>
	<script src="/resources/js/tools/jquery.hoverDelay.min.js"></script>
	<script src="/resources/js/tools/jquery-ui-1.12.1.min.js"></script>
	<script src="/resources/js/tools/jquery.sprintf.js"></script>
	<script src="/resources/js/tools/jquery.cookie.js"></script>
	<script src="/resources/js/tools/aes.js"></script>
	<script src="/resources/js/tools/aes-min.js"></script>
	<script src="/resources/js/tools/qrcode.min.js"></script>
	<script src="/resources/js/tools/clipboard.min.js"></script>
	<script src="/resources/js/tools/clipboard.js"></script>
	<script src="/resources/js/tools/jquery-migrate-1.2.1.min.js"></script>
	<script src="/resources/js/tools/jquery.jqprint-0.3.js"></script>
	<script src="/resources/js/tools/decimal.js"></script>
	<script src="/resources/js/tools/decimal.min.js"></script>
	<script src="/resources/js/tools/BigDecimal.js"></script>
	<!-- tools [End] -->
<!-- 	<script src="/resources/js/eth/web3.min.js"></script> -->
	<script src="/resources/js/eth/web3.js"></script>
 	<script src="/resources/js/eth/ethereumjs-tx-1.3.3.js"></script>
<!--  	<script src="/resources/js/eth/express.js"></script> -->
	<script src="/resources/js/eth/eth2.js"></script>
	<!-- js-end -->

<title>ETH</title>
</head>
<body>

	<span>我的地址：0x47Da216faCF48C58Caca2d1F86c11aC75758c47E</span>
	<br/>
	<!-- 点击显示余额 -->
	<button class="btn" id="selectAllMoneyBtn">查询余额</button>
	<input type="text" id="balanceInput"/>ETH
	<br/>
	<!-- 发送交易接受地址 -->
	接受地址：
	<input type="text" id="toAddressInput">
	<br/>
	<!-- 输入交易金额，点击发送交易 -->
	<button class="btn" id="sendMoneyBtn">发送交易</button>
	<input type="text" id="sendMoneyInput">ETH
	<br/>
	<!-- 点击查询交易记录，显示交易信息。 -->
	<button class="btn" id="selectChangeMessBtn">查询交易记录</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<!-- 查询手续费 -->
	手续费：<input type="text" id="gasPriceInput">ETH
	<br/>
	<!-- 显示交易信息记录 -->
	<div id="changeMess">
	</div>
	
	<script src="/resources/js/eth/eth2.js"></script>
	
	
</body>
</html>