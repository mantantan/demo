package yonyou.esn.openapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mantantan on 2018/1/19.
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String testConnected(){
        return "200 ok";
    }

}
