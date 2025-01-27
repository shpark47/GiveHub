package kh.GiveHub.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/temp/**")
			.addResourceLocations("file:///C:/GiveHub/temp/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///c:/GiveHub/upload/");
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/");
	}
}
