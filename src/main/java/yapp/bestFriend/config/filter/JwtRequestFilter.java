package yapp.bestFriend.config.filter;

import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.service.user.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        Long userId = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_PREFIX)) {
            jwtToken = requestTokenHeader.substring(BEARER_PREFIX.length());
            if (Strings.hasLength(jwtToken) && Strings.countOccurrencesOf(jwtToken, ".") == 2) {
                try {
                    userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
                } catch (Throwable e) {

                }
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());
            if (jwtTokenUtil.validateToken(jwtToken, (yapp.bestFriend.service.user.UserDetails) userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
