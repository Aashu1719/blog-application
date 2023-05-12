package com.blog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService usedetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	// this method call every time when apis
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1 get token

		String requestToken = request.getHeader("Authorization");

		// Bearer 23243kjlj
		System.out.println(requestToken);

		String username = null;

		String token = null;

		if (requestToken != null && requestToken.startsWith("Bearer")) {
			token = requestToken.substring(7);
			try {
				username = jwtTokenHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get jwt token");
			} catch (ExpiredJwtException e) {
				System.out.println("jwt token has expired");
			} catch (MalformedJwtException e) {
				System.out.println("invalid jwt ");
			}

		} else {
			System.out.println("JWT token does not begin with Bearer");
		}

		// once we get the token , now validate
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.usedetailsService.loadUserByUsername(username);

			if (jwtTokenHelper.validateToken(token, userDetails)) {
				// everything good
				// here we are doing authentication
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				System.out.println("invalid jwt token");
			}

		} else {
			System.out.println("username is null or contex is not null");

		}

		filterChain.doFilter(request, response);

	}

}
