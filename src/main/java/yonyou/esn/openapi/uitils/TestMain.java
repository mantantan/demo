package yonyou.esn.openapi.uitils;

import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Map;
import java.util.logging.Logger;

/**
 * 本类对开放平台向isv套件推送的ticket和临时code进行解密测试
 */
public class TestMain {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(HandlerInterceptorAdapter.class);


    // 下列token、suiteKey、encodingAesKey是开放平台测试用用的，接入需要自行设置
    private static String token = "test";
    private static String suiteKey = "8fa57251-22f2-4094-a513-50ec568a2170";
    private static String encodingAesKey = "U2FsdGVkX1+7B9cRaOvvmhP6ciCwOqCadAGr9vrfj/M";

    //===================================Ticket================================================
//    private static String msgSignature = "4f6ba45ecd45514910a143ac7c961253ceb5b6ce";
//    private static String timeStamp = "1516342864435";
//    private static String nonce = "163867604";
//    private static String postData = "6KN4+hyeD9Oiub7pkgpBuQK5wxte//QH9k6fyJvZWI5lbcq9ds/qxS6K3qX/w2Xm+xeva+W4mMHLuXwxZiTbwh7/TOABleCgQxf1lyC8QE7YKY8KOvUWiDv4PW1/j9h076fdduYEntFgJUbVS+1FtrSh4v0KVwlyPnKn/XiZjOczxuG+DijKCLi5URWKkB+KHAZIVIDLJW/hiCXAEJPFltBb4BwZLsVuAgw4ZHTi+k6Vjvii/Ih/ThIIhdYlZ3WneIKXGu0pcr5MbarIsLr+F4emq1Q0voBYDXRz5pm7Y3LKF4U1ZRibn0eYMzXrN/Y/sVNgOH7Nu5Y0vTWmhcOt65HJvfB3PVoQaBAmhKMsyHWDiD+42RTg/aSON72LpZd4tctpqy87rtggGYB4k/Uxjfsd7vID9QmMU6sHDs2Yj9w=";

    //===================================临时code================================================
    private static String msgSignature = "d1eba37e3f56234a1b52ed24f777a5f6f7867f01";
    private static String timeStamp = "1516346177832";
    private static String nonce = "904133514";
    private static String postData = "QVMgT+/CS9vLKi813ohJCaQiyJhpJSMHdM8JgJFDgpeKG2bNT3o3sCA4a4hqsqBc+b7wDa3aC72jj+9/ZQeTOKPsvldE11xulIULVE5ohLdRix4yCvG533A4RngP+etzjMUhIQ/pTIJ+YaMEqiUsznqIem69WL0gn+kTie8oOY8T8PhIpIssDVC9+hwJE4HQsP6jfPRc8vUvShhWIT9SXpWM3VpN/DR0Up5syv1qldxbZm5BFrgiTf+CAwVXx6YDGwzU994Uvhk/gJqQNONvAz2CU3a198LD6bCxHY7pVqNZk2vakiUs0C0WweRbWnv+S8ZG+EXcqnZ0nmi98L0hKoh6MzAwDuV9etetkstOczyYD2GETpEUXxKRCPz2vyljIBIBWce/cQ580RRt6qEpxQsx+PbEUbbiqWUNNbJT7m60LDedvAkV08QEdCfY54M8p2Y3e6WjURxRF0LuXUqv52b40jOAigwLcvLknx3rXRbW0naF23pXWJul+3ZRXZOJ8snEcx9kKTB4IZcq62C1jj706Kf8b5f1HcDFcjs4BTI=";

    public static void main(String[] args) {
        decodeData(msgSignature, timeStamp,  nonce,  postData);
    }

    public static Map<String,String>  decodeData(String msgSignature, String timeStamp, String nonce, String postData) {
        WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, encodingAesKey, suiteKey);
        String xmlString = msgCrypt.DecryptMsg(msgSignature, timeStamp, nonce, postData);
        logger.info(xmlString);
        return MapUtil.xmlToMap(xmlString);
    }

}
