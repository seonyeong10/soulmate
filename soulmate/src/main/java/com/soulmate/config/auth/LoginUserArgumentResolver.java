package com.soulmate.config.auth;

import com.soulmate.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * HandlerMethodArgumentResolver 인터페이스를 구현한 클래스
 *  - 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가 지정한 값으로 해당 메소드의 파라미터로 넘길 수 있다.
 *  - 항상 WebMvcConfigurer의 addArgumentResolvers()를 통해 추가해야 한다.
 */

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession session;

    /**
     * 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단한다.
     * : 파라미터에 @LoginUser 어노테이션이 붙어 있고, 파라미터 타입이 SessionUser.class인 경우 true를 반환한다.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * 파라미터에 전달할 객체를 생성한다.
     * : 세션에서 객체를 가져온다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return session.getAttribute("user");
    }
}
