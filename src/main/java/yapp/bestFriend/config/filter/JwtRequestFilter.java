package yapp.bestFriend.config.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.service.user.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static List<RequestMatcher> requestMatchers;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        setRequestMatchers();//필터 검사 대상 제외

        if (isError(request, response)) return;

        chain.doFilter(request, response);

    }

    private boolean isError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // anyMatch: 하나라도 일치하면 true 를 반환하고 아닐 경우에는 false 를 반환한다.
        if(requestMatchers.stream().anyMatch(req -> req.matches(request))) {
            return false;
        }

        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestTokenHeader == null) {
            setResponse(response, SC_UNAUTHORIZED, "Token not found");
            return true;
        }

        Long userId = null;
        String jwtToken = null;

        if (requestTokenHeader.startsWith(BEARER_PREFIX)) {
            jwtToken = requestTokenHeader.substring(BEARER_PREFIX.length());
            if (Strings.hasLength(jwtToken) && Strings.countOccurrencesOf(jwtToken, ".") == 2) {
                userId = getUserId(request, response, jwtToken);
                if (userId == null) return true;
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = getUserDetails(request, response, userId);
            if(userDetails == null) return true;
            if(jwtTokenUtil.validateToken(jwtToken, (yapp.bestFriend.service.user.UserDetails) userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        return false;
    }

    private UserDetails getUserDetails(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        try {
            return userDetailsService.loadUserByUsername(userId.toString());
        }catch (UsernameNotFoundException e) {
            setResponse(response, SC_UNAUTHORIZED, "User not found");
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 입력된 token 정보를 바탕으로 userId 정보를 꺼낸다.
     */
    private Long getUserId(HttpServletRequest request, HttpServletResponse response, String jwtToken) throws IOException {
        try {
            return jwtTokenUtil.getUserIdFromToken(jwtToken);
        }catch (ExpiredJwtException e) {
            e.printStackTrace();
            setResponse(response, SC_UNAUTHORIZED, "Access Token expired");
        }catch (JwtException e) {
            e.printStackTrace();
            setResponse(response, SC_UNAUTHORIZED, "Invalid Access Token");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, int errorCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode);
        response.getWriter().println("{ \"message\" : \"" + message
                + "\", \"status\" : " + errorCode
                + " }");
    }

    public void setRequestMatchers() {
        requestMatchers = Arrays.asList(new AntPathRequestMatcher("/api/oauth/**"),
                new AntPathRequestMatcher("/swagger-resources/**"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/api/token/**"),
                new AntPathRequestMatcher("/configuration/ui"),
                new AntPathRequestMatcher("/configuration/security"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/swagger/**"),
                new AntPathRequestMatcher("/v2/api-docs"),
                new AntPathRequestMatcher("/health-check")
        );
    }
}
