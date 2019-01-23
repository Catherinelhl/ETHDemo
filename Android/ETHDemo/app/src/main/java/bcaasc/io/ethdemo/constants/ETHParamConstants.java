package bcaasc.io.ethdemo.constants;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/23
 * <p>
 * 定义ETH相关参数的常量
 */
public class ETHParamConstants {
    //当前是否测试环境
    public static boolean isTest = true;

    //根据是否是测试环境，返回网络参数
    public static final String NetworkParameter = isTest ? "https://ropsten.infura.io/v3/7c4ebc9898924f0aa003ab19df9c36eb" : "https://mainnet.infura.io/v3/7c4ebc9898924f0aa003ab19df9c36eb";

}
