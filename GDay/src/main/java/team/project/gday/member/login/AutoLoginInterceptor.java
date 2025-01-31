package team.project.gday.member.login;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import team.project.gday.member.model.service.LoginService;
import team.project.gday.member.model.vo.AutoLogin;
import team.project.gday.member.model.vo.Member;
import team.project.gday.member.model.vo.ProfileImg;

public class AutoLoginInterceptor extends HandlerInterceptorAdapter{
	@Inject
	private LoginService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		
		HttpSession session = request.getSession();
		Cookie loginCookie = WebUtils.getCookie(request, "loginSessionId");
		if (loginCookie != null) {
			String sessionId = loginCookie.getValue();
			AutoLogin userSession = service.getCookie(sessionId);
			if (userSession != null) {
				// 멤버 가져오기
				Member member = service.getMember(userSession.getMemberNo());
				if( member != null) {
					// 프사 가져오기
					ProfileImg picture = service.getProfile(member.getMemberNo());
					session.setAttribute("picture", picture);
				}
				session.setAttribute("loginMember", member);
			}
		}
		return true;
	}
	   
}
