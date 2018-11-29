//
//  MyDefine.swift
//  BTC_Sample
//
//  Created by Dong on 2018/11/12.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import Foundation
import web3swift

var myAddress:String {
    get{
        switch coinType {
        case .bitcoinTest:
            return "mkkjcX4s4zoJJaE5E1NnfvsbuMvmzgBWTo"
        case.bitcoinMain:
            return "13WVtuUFwoEp3wsBmocRA1KDQ8C95P5wS3"
        case .ethMain, .ethTest:
            return "0x3b8332b7E9B4dCb85d7f4E93642d563cA2E783DC"
        }
    }
}
var myPrivateKey:String {
    get{
        switch coinType {
        case .bitcoinTest:
            return "93AaWXJMutsyX5KPCXzGjK9uPm18ezP5jiFjcCtvZwELYX9LAkk"
        case.bitcoinMain:
            return "5JoXZk5nh3FRpfHEFeZmeYimCCjPwvFUgWBAKdSTr6WREe6i9vW"
        case .ethMain, .ethTest:
            return "b899621ffeac06f4b6ae6e1b5f04f365de1e0e822e890830759373383179f5f5"
        }
    }
}
//var myPublicKey:String {
//    get{
//        switch coinType {
//        case .bitcoinTest:
//            return "04b33ac0f3c51e5a197a77b55998c1da575e0ef9e52b75056885dcd59d78bb340a0f8c25b4b7eaca07c242bb71d56507b887f1957999ff2565b9fbee238d6e756e"
//        case.bitcoinMain:
//            return "0417032639bfaa86d0ce949a7cfa3113e2f303a1a143d63b23d8a9f8e4082727c00ca6184832a58fc9397f142c41bb99b38cb679ce9205ad35bdbe195c13109428"
//        case .ethMain, .ethTest:
//            return "04ec8dfc59cab16380fefa556c08df339209ef91427809eb2b1475299f4090df18216f934782895fdda9b5532ac55ab87b3f22a9c1e01e534ed828eeac0688c287"
//        }
//    }
//}

/// 货币最小单位的和基本单位之间的汇率
var rate:Decimal {
    get{
        switch coinType {
        case .bitcoinMain,.bitcoinTest:
            return pow(10, 8)
        case .ethMain, .ethTest:
            // 1 ETH = 10^18 wei 
            return pow(10, 18)
        }
    }
}

/// 手续费
var fees:Decimal {
    get{
        switch coinType {
        case .bitcoinMain, .bitcoinTest:
            return 0.00001
        case .ethMain, .ethTest:
            // 单位是ETH
            return (gasPrice * 21000) / rate
        }
    }
    set{}
}

/// wei
var gasPrice:Decimal = 5 * pow(10, 9)

var infura : NetworkId {
    if coinType == .ethMain {
        return .mainnet
    }else {
        return .ropsten
    }
}


var currencySymbol:String {
    get{
        switch coinType {
        case .bitcoinMain, .bitcoinTest:
            return "BTC"
        case .ethMain, .ethTest:
            return "ETH"
        }
    }
}


// MARK: - 打印方法
func MyLog<T>(_ message : T,file:String = #file,methodName: String = #function, lineNumber: Int = #line){
    #if DEBUG
    let fileName = (file as NSString).lastPathComponent
    let dateForm = DateFormatter.init()
    dateForm.dateFormat = "HH:mm:ss:SSS"
    print("debug:[\(fileName)][\(lineNumber)][\(dateForm.string(from: Date()))]\(methodName):\(message)")
    #endif
    
}
