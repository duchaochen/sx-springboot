package springboot.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 加载springMVC的dispatchcerservlet
 */
public class DispatcherServletWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 加载根配置信息,比如service层，dao层等等
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SpringBeanScanConfig.class};
    }

    /**
     * springmvc 加载配置信息
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * springmvc映射 拦截url所有请求"/"
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
