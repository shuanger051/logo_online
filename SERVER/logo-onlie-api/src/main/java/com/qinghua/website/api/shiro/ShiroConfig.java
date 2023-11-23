package com.qinghua.website.api.shiro;

import com.qinghua.website.api.filter.ShiroAuthorizationFilter;
import com.qinghua.website.api.filter.ShiroLoginFilter;
import com.qinghua.website.api.listener.ShiroSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 自定义shiro配置
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.timeout}")
    private int redisTimeout;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //安全管理器配置
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义用户拦截器
        LinkedHashMap<String, Filter> map = new LinkedHashMap<>();
        map.put("authc", new ShiroLoginFilter());
        map.put("roles", new ShiroAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(map);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/sys/user/login", "anon");
        filterChainDefinitionMap.put("/sys/user/logout","anon");
        filterChainDefinitionMap.put("/crypto/getPublicKey","anon");
        filterChainDefinitionMap.put("/crypto/encrypt","anon");

        filterChainDefinitionMap.put("/test/**","anon");

        //放开图片验证码
        filterChainDefinitionMap.put("/sys/img-code/kaptcha","anon");

        //放开所有图片访问权限
        //filterChainDefinitionMap.put("/savePath/**","anon");

        //放开/app下面所有接口，给token拦截校验
        filterChainDefinitionMap.put("/app/**","anon");

        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "authc");

        //filterChainDefinitions 配置过滤规则，从上到下的顺序匹配
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    /**
     * 配置全局事务管理
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        //设置自定义realm
        defaultSecurityManager.setRealm(myRealm());
        //配置redis缓存
        defaultSecurityManager.setCacheManager(cacheManager());
        //配置自定义session管理，使用redis
        defaultSecurityManager.setSessionManager(sessionManager());
        return defaultSecurityManager;
    }

    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        myRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        myRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        myRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        myRealm.setAuthorizationCacheName("authorizationCache");
        //配置自定义密码比较器
        //shiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return myRealm;
    }

    /**
     * 配置会话管理器,设定会话超时及保存
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setCacheManager(new MemoryConstrainedCacheManager());// 加入缓存管理器
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(sessionListener());     //配置监听
        manager.setSessionListeners(listeners);

        manager.setSessionIdCookie(sessionIdCookie());
        manager.setSessionDAO(sessionDAO());// 设置SessionDao
        manager.setCacheManager(cacheManager());

        manager.setGlobalSessionTimeout(1800000);// 设置全局session超时时间,单位毫秒,30分钟
        manager.setDeleteInvalidSessions(true);// 删除过期的session
        //是否开启定时调度器进行检测过期session 默认为true
        manager.setSessionValidationSchedulerEnabled(true);


        return manager;
    }

    /**
     * shiro缓存管理器;
     * 需要添加到securityManager中
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 配置session监听
     *
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener() {
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    /**
     * 配置会话ID生成器
     *
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用redis缓存
        enterpriseCacheSessionDAO.setCacheManager(cacheManager());
        //设置session缓存的名字 默认为 shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     *
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。设为true后，只能通过http访问，javascript无法访问，防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置Redis服务器
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost);
        redisManager.setPort(redisPort);
        redisManager.setPassword(redisPassword);
        redisManager.setTimeout(1800000); // 过期时间设置为30分钟
        return redisManager;
    }

    /**
     * 支持shiro注解权限控制
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

}
