/* 
 * 
	eth.js
	
 * 
 */

//获取手续费价格
$(function() {
	$.ajax({
		data : "",
		url : "getGasPrice",
		type : "post",
		dataType : "json",
		success : function(data) {
			if (data != null) {
				$('#gasPriceInput').val(data);
			} else {
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log(xhr.status);
			console.log(thrownError);
		}
	});
});

// 生成钱包
$('#createWalletBtn').click(
		function() {
			// 查询的地址
			var createWalletFile = $('#createWalletFile').val();
			var addressPassword = $('#addressPassword').val();

			if (createWalletFile == null || createWalletFile == ''
					|| createWalletFile == undefined) {
				alert("请输入创建钱包文件的存放路径");
				return false;
			}
			if (addressPassword == null || addressPassword == ''
					|| addressPassword == undefined) {
				alert("请输入创建钱包文件的密码");
				return false;
			}
			// 创建钱包
			$.ajax({
				data : {
					"createWalletFile" : createWalletFile,
					"addressPassword" : addressPassword
				},
				url : "creatWallet",
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.walletFileName != null
							&& data.walletFileName != "err") {
						alert("成功创建钱包");
					} else {
						alert("创建钱包失败,请输入正确的文件夹路径");
					}

				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("创建钱包失败");
					console.log(xhr.status);
					console.log(thrownError);
				}
			});
		});

// 根据钱包文件获取钱包地址
$('#loadWalletBtn').click(
		function() {
			// 输入钱包文件路径和钱包密码
			var selectWalletFile = $('#selectWalletFile').val();
			var addressPassword = $('#addressPassword').val();

			if (selectWalletFile == null || selectWalletFile == ''
					|| selectWalletFile == undefined) {
				alert("请输入需要获取的钱包文件路径");
				return false;
			}
			if (addressPassword == null || addressPassword == ''
					|| addressPassword == undefined) {
				alert("请输入需要获取的钱包密码");
				return false;
			}
			// 加载钱包
			$.ajax({
				data : {
					"selectWalletFile" : selectWalletFile,
					"addressPassword" : addressPassword
				},
				url : "loadWallet",
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.Walletaddress != null
							&& data.Walletaddress != "err") {
						// 显示查询到的钱包地址
						$('#loadWalletAddress').val(data.Walletaddress);
					} else {
						alert("钱包文件错误或密码错误");
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					console.log(xhr.status);
					console.log(thrownError);
					alert("钱包文件错误或密码错误");
				}
			});
		});

// 点击查询余额
$('#selectAllMoneyBtn').click(function() {
	// 查询的地址
	var myAddress = $('#myAddress').val();
	if (myAddress == null || myAddress == '' || myAddress == undefined) {
		alert("请输入钱包地址");
		return false;
	}
	// 查询以太币余额
	$.ajax({
		data : {
			"address" : myAddress
		},
		url : "getBlance",
		type : "post",
		dataType : "json",
		success : function(data) {
			if (data != null) {
				$('#balanceInput').val(data);
			} else {
				alert("请输入正确的钱包地址");
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("getBlance err");
			console.log(xhr.status);
			console.log(thrownError);
		}
	});
});

// 点击发送交易
$('#sendMoneyBtn').click(
		function() {
			// 钱包文件路径，钱包密码
			var selectWalletFile = $('#selectWalletFile').val();
			var addressPassword = $('#addressPassword').val();

			if (selectWalletFile == null || selectWalletFile == ''
					|| selectWalletFile == undefined) {
				alert("请输入需要交易的钱包路径");
				return false;
			}
			if (addressPassword == null || addressPassword == ''
					|| addressPassword == undefined) {
				alert("请输入钱包密码");
				return false;
			}

			var toAddressInput = $('#toAddressInput').val();
			var sendMoneyInput = $('#sendMoneyInput').val();

			if (toAddressInput == null || toAddressInput == ''
					|| toAddressInput == undefined) {
				alert("请输入接收地址");
				return false;
			}
			if (sendMoneyInput == null || sendMoneyInput == ''
					|| sendMoneyInput == undefined) {
				alert("请输入发送eth金额");
				return false;
			}
			// 发送交易
			$.ajax({
				data : {
					"toAddressInput" : toAddressInput,
					"sendMoneyInput" : sendMoneyInput,
					"selectWalletFile" : selectWalletFile,
					"addressPassword" : addressPassword
				},
				url : "transTo",
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data != null) {
						alert("交易成功");
					} else {
						alert("交易失败");
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("交易失败");
					console.log(xhr.status);
					console.log(thrownError);
				}
			});
		});

// 点击查询交易记录
$('#selectChangeMessBtn').click(function() {
	var myAddress = $('#myAddress').val();
	if (myAddress == null || myAddress == '' || myAddress == undefined) {
		alert("请输入查询钱包地址");
		return false;
	}
	$.ajax({
		data : {
			"address" : myAddress
		},
		url : "getAllTransactions",
		type : "post",
		dataType : "json",
		success : function(data) {
			if (data != null) {
				$('#changeMess').text(JSON.stringify(data));
			} else {
				alert("网络异常或输入的钱包地址有误");
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log(xhr.status);
			console.log(thrownError);
		}
	});
});

// 生成钱包二维码图片
var qrcode = new QRCode("qrcode");
function makeCode() {
	var elText = document.getElementById("myAddress");
	if (!elText.value) {
		alert("请输入要生成二维码的钱包地址");
		elText.focus();
		return;
	}
	qrcode.makeCode(elText.value);
}

$('#createQRCode').click(function() {
	makeCode();
}).on("keydown", function(e) {
	if (e.keyCode == 13) {
		makeCode();
	}
});
