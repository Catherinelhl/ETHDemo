/* 
 * 
	eth2.js
	
 * 
 */
var Web3 = require('web3');
//var web3;
if (typeof web3 !== 'undefined') {
    web3 = new Web3(web3.currentProvider);
} else {
	//web3 = new Web3(new Web3.providers.HttpProvider("https://mainnet.infura.io"));
	web3 = new Web3(new Web3.providers.HttpProvider("https://mainnet.infura.io/v3/a53a28a37ec943f6aa4ba5bbf8e1d24c"));
}
console.log(web3);
console.log(web3.currentProvider);

//web3.eth.accounts.privateKeyToAccount("0x3741f6cc45865b98e9d027febe5b2b801671001e0b21c3011c62e9eb867e993b",function(err,result){
//	alert(err);
//	alert(result);
//});

//address
var currentAccount = "0x47Da216faCF48C58Caca2d1F86c11aC75758c47E";
web3.eth.defaultAccount = '0x47Da216faCF48C58Caca2d1F86c11aC75758c47E';
//手续费
web3.eth.getGasPrice(function(err,result){
    var gasPrice = Number(web3.fromWei(result, "ether"));
	$("#gasPriceInput").val(gasPrice);
})

//点击查询余额
$("#selectAllMoneyBtn").click(
		function() {
			 //查询以太币余额
			web3.eth.getBalance(currentAccount, 'latest', function(err, result) {
			    if (err != null) {
			    	alert(result);
			    }
			    var balance = Number(web3.fromWei(result, "ether"));
				$("#balanceInput").val(balance);
			}); 
		});


//点击查询交易记录
$("#selectChangeMessBtn").click(
		function() {
//			交易记录
			var info = web3.eth.getBlock(3150,function(err,result){
			//	alert("getBlock");
			//	alert(JSON.stringify(result));
			});

			var transaction = web3.eth.getTransaction('0x3f46f71daf7e6fbd8a37aa66a5f0217a56a4749a061c10d6aabfc6dfbd7382ec',function(err,result){
			//	alert(JSON.stringify(result));
				$("#changeMess").text(JSON.stringify(result));
			});
			
//			web3.eth.getAccounts(function(err, accounts){
//			    if (err != null) alert("err"+err+"accounts:"+accounts);
//			    else if (accounts.length == 0) alert("User is not logged in to MetaMask"+accounts);
//			    else alert("User is logged in to MetaMask"+accounts);
//			});
		});

//点击发送交易
$("#sendMoneyBtn").click(
//		function() {
//			//引入ethereumjs-tx
//			
//			var Tx = window.ethereumjs;// require('ethereumjs-tx');
//			// 以太币转账    
//			// 先获取当前账号交易的nonce
//			web3.eth.getTransactionCount(currentAccount, web3.eth.defaultBlock.pending).then(function(nonce){
//			    // 获取交易数据
//			    var txData = {
//			        // nonce每次++，以免覆盖之前pending中的交易
//			        nonce: web3.utils.toHex(nonce++),
//			        // 设置gasLimit和gasPrice
//			        gasLimit: web3.utils.toHex(21000),   
//			        gasPrice: web3.utils.toHex(10e9),  
//			        // 要转账的哪个账号  
//			        to: '0x5836cc7b00696fd24e33f01c85f50371d87e9fd0',
//			        // 从哪个账号转
//			        from: "0x47Da216faCF48C58Caca2d1F86c11aC75758c47E",
//			        // 0.001 以太币
//			        value: web3.utils.toHex(0.0000000001),         
//			        data: ''
//			    }
//			 
//			    var tx = new Tx(txData);
//			 
//			    // 引入私钥，并转换为16进制
//			    const privateKey = new Buffer('0x3741f6cc45865b98e9d027febe5b2b801671001e0b21c3011c62e9eb867e993b', 'hex'); 
//			 
//			    // 用私钥签署交易
//			    tx.sign(privateKey);
//			 
//			    // 序列化
//			    var serializedTx = tx.serialize().toString('hex');
//			 
//			    web3.eth.sendSignedTransaction('0x' + serializedTx.toString('hex'), function(err, hash) {
//			        if (!err) {
//			            console.log(hash);
//			        } else {
//			            console.error(err);
//			        }
//			    });
//			});
//		}
		

		//交易（from只能是游览器插件登陆的账号）
		function(){
			web3.eth.sendTransaction({
			    from: "0x47Da216faCF48C58Caca2d1F86c11aC75758c47E",
			    to: "0x5836cc7b00696fd24e33f01c85f50371d87e9fd0",
			    value: '200'
			},function(err,result){
				alert("send:"+err);
				alert(result);
			})
		}
);
		
//		function(){
//		//	var Tx = require('ethereumjs-tx');
//			var Tx = window.ethereumjs;// require('ethereumjs-tx');
//			alert(window);
//			alert(window.buffer);
//			var Buffer = require('buffer');
//			alert(Buffer);
//			var privateKey = new Buffer('0x3741f6cc45865b98e9d027febe5b2b801671001e0b21c3011c62e9eb867e993b', 'hex');
//			var rawTx = {
//			 nonce: '0x00',
//			 gasPrice: '21000', 
//			 gasLimit: '0x2710',
//			 to: '0x5836cc7b00696fd24e33f01c85f50371d87e9fd0', 
//			 value: '0.000000000001', 
//			 data: ''
//			}
//			var tx = new Tx(rawTx);
//			tx.sign(privateKey);
//			var serializedTx = tx.serialize();
//			web3.eth.sendRawTransaction('0x' + serializedTx.toString('hex'), function(err, hash) {
//			 if (!err)
//			   console.log(hash); 
//			 	alert(hash);
//			});
//		});
		
		