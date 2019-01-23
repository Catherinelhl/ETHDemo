//
//  ApiManager.swift
//  BTC_Sample
//
//  Created by Dong on 2018/11/12.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Moya
import Alamofire
import SwiftyJSON


enum CoinType : String {
    case bitcoinMain = "https://api.blockcypher.com/v1/btc/main"
    case bitcoinTest = "https://api.blockcypher.com/v1/btc/test3"
    case ethMain = "https://api.etherscan.io"
    case ethTest = "https://api-ropsten.etherscan.io"
}

var coinType:CoinType = .ethTest

let ApiManagerProvider = MoyaProvider<ApiManager>(requestClosure: { (endpoint:Endpoint, done: @escaping MoyaProvider.RequestResultClosure) in
    do{
        var request = try endpoint.urlRequest()
        request.timeoutInterval = 30 //设置请求超时时间
        done(.success(request))
    }catch{
        return
    }
})



/// API
///
/// - getBalance: 获取余额
/// - createTx: 创建交易
/// - sendTx: 发送交易
/// - getTxRecord: 获取交易记录
enum ApiManager {
    case getTxRecord(address:String)
//    case getTxDetail(txHash:String)
}

extension ApiManager : TargetType {
    var baseURL: URL {
        let urlString = coinType.rawValue
        return URL(string: urlString)!
    }
    
    var path: String {
        return "/api"
    }
    
    var method: Moya.Method {
        switch self {
        case .getTxRecord(_):
            return .get
        }
    }
    
    var sampleData: Data {
        return "{}".data(using: .utf8)!
    }
    
    var task: Task {
        switch self {
        case .getTxRecord(let address):
            let param:[String:Any] = ["module":"account",
                                     "action":"txlist",
                                     "address":address,
                                     "page":1,
                                     "offset":1000,
                                     "sort":"desc"]
            return .requestParameters(parameters: param, encoding: URLEncoding.default)

        }
        
    }
    
    var headers: [String : String]? {
        return nil
    }
    
    
}
