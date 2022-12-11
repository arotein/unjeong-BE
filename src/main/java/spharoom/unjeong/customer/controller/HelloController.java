package spharoom.unjeong.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${global.api.base-path}/hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "안녕";
    }
}
