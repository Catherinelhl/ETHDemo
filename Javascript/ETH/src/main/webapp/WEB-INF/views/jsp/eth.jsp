<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/eth.css">
<!-- js -->
<!-- tools [Start] -->
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery-1.12.4.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery.hoverDelay.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery.hoverDelay.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery-ui-1.12.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery.sprintf.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/aes.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/aes-min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/qrcode.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/clipboard.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/clipboard.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery-migrate-1.2.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/jquery.jqprint-0.3.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/decimal.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/decimal.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/tools/BigDecimal.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/eth/eth.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/eth/qrcode.min.js"></script>
<!-- js-end -->

<title>ETH</title>
</head>
<body>
	钱包地址:
	<input type="text" id="myAddress" />
	<br />
	<br />
	<!-- 点击显示余额 -->
	<button class="btn" id="selectAllMoneyBtn">查询余额</button>(输入需要查询的钱包地址)
	<input type="text" id="balanceInput" />ETH
	<br />
	<br />
	<button class="btn" id="createQRCode">生成二维码</button>(输入钱包地址)
	<div id="qrcode" class="qrcode"></div>
	<br />
	<br /> 钱包密码:
	<input type="password" id="addressPassword" />
	<br />
	<br /> 生成钱包文件夹路径：
	<input type="text" id="createWalletFile" class="inputLength" />
	<br />
	<br /> 查询需要的钱包文件路径：
	<input type="text" id="selectWalletFile" class="inputLength" />
	<br />
	<br />
	<button class="btn" id="createWalletBtn">生成钱包</button>(输入存放钱包文件夹路径和设置钱包密码)
	<br />
	<br />
	<button class="btn" id="loadWalletBtn">获取钱包地址</button>(输入钱包文件路径和钱包密码)
	<input type="text" id="loadWalletAddress" />

	<br />
	<br />

	<!-- 发送交易接受地址 -->
	接受地址：
	<input type="text" id="toAddressInput">
	<br />
	<br />

	<!-- 输入交易金额，点击发送交易 -->
	<button class="btn" id="sendMoneyBtn">发送交易</button>(输入1钱包文件地址2钱包密码3接受钱包地址4交易金额)
	<input type="text" id="sendMoneyInput">ETH
	<br />
	<br />
	<!-- 点击查询交易记录，显示交易信息。 -->
	<button class="btn" id="selectChangeMessBtn">查询交易记录</button>(输入需要查询的钱包地址) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<!-- 获取手续费 -->
	Gas Price：
	<input type="text" id="gasPriceInput">ETH
	<br />
	<br />
	<span> 手续费计算规则：Gas Price是时刻变化的，这里的手续费不是自定义的，是系统根据Gas Price和Gas
		Limit计算。 <br /> Gas Limit一般为21000 (可自定义Gas Price和Gas Limit) <br /> 手续费
		= Gas Price * Gas Limit
	</span>
	<br />
	<br />
	<!-- 显示交易信息记录 -->
	<div id="changeMess" class="txDiv"></div>

	<script src="${pageContext.request.contextPath}/resources/js/eth/eth.js"></script>


</body>
</html>