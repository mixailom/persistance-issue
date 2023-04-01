# Reproducing c3p0 connection pool leak

## Prerequisites
- Java 17 (or later)
- MySQL instance running in the localhost
- Apache Maven

## Starting the app

run the app by `mvn spring-boot:run`

You should be able to navigate to the browser and open [localhost:8080](http://localhost:8080)

Refresh page several times, in the console, you should be able to see outputs like:  
Connections in connectionsGraveYard: 2  
Connections in connectionsGraveYard: 3  
Connections in connectionsGraveYard: 4  
Connections in connectionsGraveYard: 5 ...

10 seconds after the first request you should see output in the console:  

>2023-04-01T18:03:57.023+02:00  INFO 37736 --- [        Timer-0] c.m.v2.resourcepool.BasicResourcePool    : A checked-out resource is overdue, and will be destroyed: com.mchange.v2.c3p0.impl.NewPooledConnection@37d6e829  
2023-04-01T18:03:57.024+02:00  INFO 37736 --- [        Timer-0] c.m.v2.resourcepool.BasicResourcePool    : Logging the stack trace by which the overdue resource was checked-out.  
java.lang.Exception: DEBUG ONLY: Overdue resource check-out stack trace.  
at com.mchange.v2.resourcepool.BasicResourcePool.checkoutResource(BasicResourcePool.java:506) ~[c3p0-0.9.1.2.jar:0.9.1.2]  
at com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.checkoutPooledConnection(C3P0PooledConnectionPool.java:525) ~[c3p0-0.9.1.2.jar:0.9.1.2]  
at com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource.getConnection(AbstractPoolBackedDataSource.java:128) ~[c3p0-0.9.1.2.jar:0.9.1.2]  
**at org.example.data.EmployeeDAOImpl.findAllEmployees(EmployeeDAOImpl.java:30) ~[classes/:na]**  
at jdk.internal.reflect.GeneratedMethodAccessor5.invoke(Unknown Source) ~[na:na]  
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]  
at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]  
at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:752) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:137) ~[spring-tx-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:752) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:703) ~[spring-aop-6.0.2.jar:6.0.2]  
at org.example.data.EmployeeDAOImpl$$SpringCGLIB$$0.findAllEmployees(<generated>) ~[classes/:na]  
at org.example.HelloController.index(HelloController.java:18) ~[classes/:na]  
at jdk.internal.reflect.GeneratedMethodAccessor4.invoke(Unknown Source) ~[na:na]  
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]  
at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]  
at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:207) ~[spring-web-6.0.2.jar:6.0.2]  
at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:152) ~[spring-web-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:884) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1080) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:973) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1003) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:895) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:705) ~[tomcat-embed-core-10.1.1.jar:6.0]  
at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:880) ~[spring-webmvc-6.0.2.jar:6.0.2]  
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:814) ~[tomcat-embed-core-10.1.1.jar:6.0]  
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:223) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.0.2.jar:6.0.2]  
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]  
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.0.2.jar:6.0.2]  
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]  
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.0.2.jar:6.0.2]  
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]  
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:119) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:400) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:861) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1739) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.1.jar:10.1.1]  
at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]  

Please note the bold line in the stacktrace:  
**at org.example.data.EmployeeDAOImpl.findAllEmployees(EmployeeDAOImpl.java:30) ~[classes/:na]**  

If you look at the line 30 in org.example.data.EmployeeDAOImpl.java you will see that is the line where we are (intentianally) putting a connection to the static collection (and never releasing it from there).  