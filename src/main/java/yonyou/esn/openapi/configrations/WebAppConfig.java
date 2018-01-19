package yonyou.esn.openapi.configrations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import yonyou.esn.openapi.interceptor.DefaultInterceptor;

/**
 * Created by mantantan on 2017/12/31.
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    DefaultInterceptor localDefaultInterceptor() {
        return new DefaultInterceptor();
    }

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1.DefaultInterceptor通配所有路由
        registry.addInterceptor(localDefaultInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }


    /**
     * 后端支持跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.ACCESS_CONTROL_MAX_AGE)
                .allowCredentials(false).maxAge(1800);
    }
}
