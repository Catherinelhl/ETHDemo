//
//  EthereumTool.swift
//  ETH_Sample
//
//  Created by Dong on 2018/11/21.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import web3swift

class EthereumTool {
    
    private static let keyStorePath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] + "/keystore"
    static let password = "notSafePassword"
    
    
    

    class func getKeystoreManager(by privateKey:String) -> KeystoreManager? {
        
        guard let privateKeyData = Data.fromHex(privateKey) else {
            return nil
        }
        
        do {
            if /*let keystoreManager = KeystoreManager.managerForPath(keyStorePath) ,*/
                let ks = try EthereumKeystoreV3.init(privateKey: privateKeyData, password: password){
                
                return KeystoreManager.init([ks])
//                let keyData = try JSONEncoder().encode(ks.keystoreParams)
//                FileManager.default.createFile(atPath: keyStorePath + "/key.json", contents: keyData, attributes: nil)
//                return keystoreManager
            }
        }catch let aError{
            MyLog(aError)
        }
        
        return nil
    }
    
    class func generateETHKey() {
        do {
            let ks = try EthereumKeystoreV3.init(password: password)
            if let address = ks?.getAddress() {
               let privateKeyString = try ks?.UNSAFE_getPrivateKeyData(password: password, account: address).hex
                MyLog(address.address)
                MyLog(privateKeyString)
            }
            
        }catch let aError {
            MyLog(aError)
        }
        
        
    }
    
//    class func generateETHKey() -> ETHKeyStore {
//        // 创建钱包
//        let privateKey = PrivateKey.init(network: .mainnet, isPublicKeyCompressed: false)
//        let wallet = Wallet.init(privateKey: privateKey)
//        MyLog("privateKey:\(wallet.privateKey.description)")
//        MyLog("publicKey:\(wallet.publicKey.description)")
//        MyLog("address:\(wallet.publicKey.raw.ethAddressString ?? "")")
//
//        let keyStore = ETHKeyStore()
//        keyStore.privateKey = wallet.privateKey.description
//        keyStore.publicKey = wallet.publicKey.description
//        keyStore.address = wallet.publicKey.raw.ethAddressString
//        return keyStore
//    }
    
    class func generateETHKeyBy(_ privateKey:String) -> ETHKeyStore? {
        
        guard let privateKeyData = Data.fromHex(privateKey) else {
            return nil
        }
        do {
            if let ks = try EthereumKeystoreV3.init(privateKey: privateKeyData, password: password), let address = ks.getAddress() {
                let privateKeyString = try ks.UNSAFE_getPrivateKeyData(password: password, account: address).hex
                MyLog("privateKey:\(privateKeyString)")
                MyLog("address:\(address.address)")
                let keyStore = ETHKeyStore()
                keyStore.privateKey = privateKeyString
                keyStore.address = address.address
                return keyStore
            }
        }catch let aError {
            MyLog(aError)
            return nil
        }
        return nil
        
    }
    
    
}
