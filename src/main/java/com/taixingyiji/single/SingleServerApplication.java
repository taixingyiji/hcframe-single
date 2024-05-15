package com.taixingyiji.single;

import com.taixingyiji.base.module.datasource.config.DataSourceConfiguration;
import net.unicon.cas.client.configuration.CasClientConfigurationProperties;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lhc
 */
@MapperScan({"com.taixingyiji.**.dao"})
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSwagger2
@ServletComponentScan
@EnableCaching
@ComponentScan(basePackages = {"com.taixingyiji.**"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DataSourceConfiguration.class}))
@Import(CasClientConfigurationProperties.class)
//@EnableCasClient
public class SingleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleServerApplication.class, args);
    }

    @Bean
    public WebMvcRegistrations mvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
                return new ExtendedRequestMappingHandlerAdapter();
            }
        };
    }

    private static class ExtendedRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

        @Nonnull
        @Override
        protected InitBinderDataBinderFactory createDataBinderFactory(@Nonnull List<InvocableHandlerMethod> methods) {
            return new ServletRequestDataBinderFactory(methods, getWebBindingInitializer()) {

                @Nonnull
                @Override
                protected ServletRequestDataBinder createBinderInstance(
                        Object target, @Nonnull String name,@Nonnull NativeWebRequest request) throws Exception {
                    ServletRequestDataBinder binder = super.createBinderInstance(target, name, request);
                    String[] fields = binder.getDisallowedFields();
                    List<String> fieldList = new ArrayList<>(fields != null ? Arrays.asList(fields) : Collections.emptyList());
                    fieldList.addAll(Arrays.asList("class.*", "Class.*", "*.class.*", "*.Class.*"));
                    binder.setDisallowedFields(fieldList.toArray(new String[]{}));
                    return binder;
                }
            };
        }
    }
}
