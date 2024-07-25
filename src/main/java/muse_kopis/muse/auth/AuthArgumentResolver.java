package muse_kopis.muse.auth;

import lombok.RequiredArgsConstructor;
import muse_kopis.muse.auth.jwt.JwtService;
import muse_kopis.muse.common.UnAuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuth = parameter.hasParameterAnnotation(Auth.class);
        boolean equals = parameter.getParameterType().equals(String.class);
        return hasAuth && equals;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String accessToken = extractAccessToken(webRequest);
        return jwtService.extractMemberId(accessToken);
    }

    private String extractAccessToken(NativeWebRequest webRequest) {
        String bearerToken = webRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
           return bearerToken.substring(7);
        }
        throw new UnAuthorizationException("로그인 없이 접근 할 수 없습니다.");
    }
}
