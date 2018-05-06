package com.quadrosepaineisapi.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> arg1) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}
	
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter arg1, MediaType arg2,
			Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest request, 
					ServerHttpResponse response) {
		
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();
		
		String refreshToken = body.getRefreshToken().getValue();
		addRefreshTokenOnCookie(refreshToken, req, res);
		
		DefaultOAuth2AccessToken defaultToken = (DefaultOAuth2AccessToken) body;
		
		removeRefreshTokenFromBody(defaultToken);
		
		return body;
	}

	private void removeRefreshTokenFromBody(DefaultOAuth2AccessToken defaultToken) {
		defaultToken.setRefreshToken(null);
	}

	private void addRefreshTokenOnCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(false); //TODO: mudar para https em produção
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(3600 * 24);
		res.addCookie(refreshTokenCookie);
	}

	

}
