package com.xiilab.data_learnway.global.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

	/*
	 * 사용자 언어 환경을 설정해주기 위한 bean 설정
	 */
	//    @Bean
	//    public SessionLocaleResolver localeResolver() {
	//        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	//        //sessionLocaleResolver.setDefaultLocale(Locale.KOREAN);
	//        sessionLocaleResolver.setDefaultLocale(Locale.KOREA);
	//        Locale.setDefault(Locale.KOREA);
	//        return sessionLocaleResolver;
	//    }
	/*
	 * 사용자 언어 환경을 설정해주기 위한 bean 설정
	 */
	@Bean
	public LocaleResolver localeResolver() {

		CookieLocaleResolver resolver = new CookieLocaleResolver("lang");
		resolver.setDefaultLocale(Locale.KOREA); // default Locale
		//resolver.setCookieName("lang");
		//resolver.setCookiePath("/data_learnway/api");
		resolver.setCookieHttpOnly(true); //
		//        resolver.setCookieMaxAge(01); //쿠키 유효 시간, 01로 해두면 브라우저를 닫을 때 없어짐
		//        resolver.setCookiePath("/"); //Path를 지정하면 해당하는 Path와 그 하위 Path 에서만 참조
		//        resolver.setCookieDomain("dataStudio"); //쿠키 도메인
		//        resolver.setCookieSecure(false); //쿠키 보안 여부

		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
