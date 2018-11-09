package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//表示<beans></beans>
@Configuration
//表示配置视图解析器，开启注解的相关配置，以及<bean></bean>等等。
@EnableWebMvc
//表示包扫描
@ComponentScan(basePackages = {"springboot.controller"})
public class WebConfig extends WebMvcConfigurationSupport {
    /**
     * 配置springMVC视图解析器
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //配置前缀
        viewResolver.setPrefix("/WEB-INF/views/");
        //配置后缀
        viewResolver.setSuffix(".jsp");
        //可以在jsp页面中通过${}访问beans
        viewResolver.setExposeContextBeansAsAttributes(true);
        return viewResolver;
    }
}
