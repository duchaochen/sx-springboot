package springboot;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import springboot.controller.IndexServlet;

import javax.servlet.ServletException;
import java.io.File;

public class SpringBootApp {

    public static void main(String[] args) throws LifecycleException, ServletException {
        start();
    }


    public static void start() throws ServletException, LifecycleException {

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


    private final static int TOMCAT_PORT = 8080;
    private static String CONTEX_PATH = "/itmayiedu";
    private static String SERVLET_NAME = "IndexServlet";
    private void start1() throws LifecycleException {
        // 创建tomcat服务器
        Tomcat tomcatServer = new Tomcat();
        // 指定端口号
        tomcatServer.setPort(TOMCAT_PORT);
        // 是否设置自动部署
        tomcatServer.getHost().setAutoDeploy(false);
        // 创建上下文
        StandardContext standardContex = new StandardContext();
        standardContex.setPath(CONTEX_PATH);
        // 监听上下文
        standardContex.addLifecycleListener(new Tomcat.FixContextListener());
        // tomcat容器添加standardContex
        tomcatServer.getHost().addChild(standardContex);

        // 创建Servlet
        tomcatServer.addServlet(CONTEX_PATH, SERVLET_NAME, new IndexServlet());
        // servleturl映射
        standardContex.addServletMappingDecoded("/index", SERVLET_NAME);
        tomcatServer.start();
        System.out.println("tomcat服务器启动成功..");
        // 异步进行接收请求
        tomcatServer.getServer().await();
    }
}
