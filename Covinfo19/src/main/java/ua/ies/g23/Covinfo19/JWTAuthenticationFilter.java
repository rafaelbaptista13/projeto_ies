
package ua.ies.g23.Covinfo19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ua.ies.g23.Covinfo19.pacientes_med_hosp.model.Medico;
import ua.ies.g23.Covinfo19.SecurityConstants;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/api/v1/public/login"); 
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Medico creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Medico.class);
            
                    System.out.println("A");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getNome(),
                            creds.getCodigo_acesso(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((Medico) auth.getPrincipal()).getNome())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        String body = ((Medico) auth.getPrincipal()).getNome() + " " + token;

        res.getWriter().write(body);
        res.getWriter().flush();
    }
}
