package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.service.IndexService;

@Controller
public class IndexContrller {

    @Autowired
    private IndexService indexService;
    /**
     * value:访问地址
     * produces：解决乱码问题
     * @return
     *
     */
    @RequestMapping(value = "/index1", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String index1() {
        return "手写springboot。。。";
    }

    @RequestMapping(value = "/index2", produces = "text/html;charset=UTF-8")
    public String index2(Model model) {
        String str = indexService.show();
        model.addAttribute("str",str);
        return "index";
    }
}
