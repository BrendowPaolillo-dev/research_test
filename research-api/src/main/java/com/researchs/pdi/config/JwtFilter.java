package com.researchs.pdi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JwtFilter extends GenericFilterBean {

    public static final String SIGNING_KEY = "pdi@Db1*()";
    public static final String CLAIMS_GET = "USUARIO";
    public static final String PERFIL = "perfil";

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (request.getMethod().equals("OPTIONS")) {
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, Accept, Content-Type, X-PINGOTHER");
            response.addHeader("Access-Control-Max-Age", "1728000");
            chain.doFilter(req, res);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(7); // The part after "Bearer "
        try {
            final Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token).getBody();
            HashMap dadosUsuario = (HashMap ) claims.get(CLAIMS_GET);
            request.setAttribute(PERFIL, dadosUsuario);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }
        chain.doFilter(req, res);
    }
}