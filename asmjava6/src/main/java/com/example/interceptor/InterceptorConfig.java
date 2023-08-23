package com.example.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.service.BrandService;
import com.example.service.DistributorService;
import com.example.service.ReportService;
import com.example.service.impl.BrandServiceImpl;
import com.example.service.impl.DistributorServiceImpl;
import com.example.service.impl.ReportServiceImpl;



@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	Goballinterceptor goballinterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println(goballinterceptor);
		registry.addInterceptor(goballinterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/rest/**", "/admin/**", "/assets/**");
	}

	@Bean
	public BrandService brandService() {
		return new BrandServiceImpl();
	}
	 @Bean
    public DistributorService distributorService() {
        return new DistributorServiceImpl();
    }


	@Bean
    public ReportService reportService() {
        return new ReportServiceImpl();
    }
}
