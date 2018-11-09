

### springboot是一个快速整合第三方框架的，简化了xml的配置，项目中再也不包含web.xml文件了，完全使用注解来完成操作的，并且内部自带tomcat启动。直接使用jar文件运行即可。
    
    1.导入依赖
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>8.5.34</version>
        </dependency>
        <!-- tomcat对jsp支持 -->
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper</artifactId>
            <version>8.5.16</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.0.10.RELEASE</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.10.RELEASE</version>
            <scope>compile</scope>
        </dependency>
    
    2.加载spring mvc的相关配置信息
        创建一个WebConfig类继承WebMvcConfigurationSupport类，
        需要加上@Configuration，@EnableWebMvc，@ComponentScan(basePackages = {"扫描mvc包的名称"})
        
        代码：
        //表示这是一个配置类，配置<beans></beans>
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
        
    3.加载spring容器相关配置信息
        创建Service_DaoConfig配置类，只需要@Configuration，@ComponentScan注解即可，
        这个类主要是扫描service和dao层的，代码如下：
        //表示配置类
        @Configuration
        //如果写了dao层，那么这里也需要扫描一下dao层
        @ComponentScan("springboot.service")
        public class Service_DaoConfig {
        
        }
        
    4.加载springMVC的dispatcherservlet
        创建SpittrWebAppInitializer类继承AbstractAnnotationConfigDispatcherServletInitializer类
            代码如下：
            public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
                /**
                 * 加载spring容器配置信息
                 * @return
                 */
                @Override
                protected Class<?>[] getRootConfigClasses() {
                     return new Class[] {Service_DaoConfig.class};
                }
            
                /**
                 * 加载springmvc配置信息
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
    5.在service层创建一个IndexService业务类,代码如下:
        @Service
        public class IndexService {
        
            public String show() {
                return "我是service层";
            }
        }
    6.创建一个IndexController类，代码如下：
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
            public String index() {
                return "手写springboot。。。";
            }
        
            @RequestMapping(value = "/index2", produces = "text/html;charset=UTF-8")
            public String springboot(Model model) {
                String str = indexService.show();
                model.addAttribute("str",str);//返回到jsp页面显示数据
                return "springboot";
            }
        } 
    
    7.新建一个WEB-INF => views => springboot.jsp文件,代码如下:
        <%@ page language="java" contentType="text/html; charset=UTF-8"
                 pageEncoding="UTF-8"%>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>springboot</title>
        </head>
        <body>
        
        <h1>这是一个手写的springboot框架</h1>
        这里显示后台返回的数据为：【${str}】
        </body>
        </html>  
        
    8.创建一个启动程序SpringBootApp类，代码如下：
        
        public class SpringBootApp {
            public static void main(String[] args) throws LifecycleException, ServletException {
                // 创建Tomcat容器对象
                Tomcat tomcatServer = new Tomcat();
                // 设置端口号
                tomcatServer.setPort(8080);
                // 读取项目路径 加载静态资源
                StandardContext ctx = (StandardContext) tomcatServer.addWebapp("/", new File("src/main").getAbsolutePath());
                // 禁止重新载入
                ctx.setReloadable(false);
                // class文件读取地址
                File additionWebInfClasses = new File("target/classes");
                // 创建WebRoot
                WebResourceRoot resources = new StandardRoot(ctx);
                // tomcat内部读取Class执行(/WEB-INF/classes虚拟出来的路劲)
                resources.addPreResources(
                        new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
                tomcatServer.start();
                // 异步等待请求执行
                tomcatServer.getServer().await();
            }
        }
    
    9.测试启动程序
        输入地址: http://localhost:8080/index1
                http://localhost:8080/index2
        
       