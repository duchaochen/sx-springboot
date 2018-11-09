package springboot.service;

import org.springframework.stereotype.Service;

@Service
public class IndexService {

    public String show() {
        return "我是service层";
    }
}
