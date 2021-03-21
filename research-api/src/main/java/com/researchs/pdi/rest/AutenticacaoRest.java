package com.researchs.pdi.rest;

import com.researchs.pdi.config.JwtFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/login")
public class AutenticacaoRest {

    @ApiOperation("Use qualquer usuário e senha. A API vai criar este usuário para você poder testar as Entrevistas")
    @RequestMapping(method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest login) {
        Perfil perfil = new Perfil(
                                    login.usuario,
                                    login.senha);
        String token = Jwts.builder()
            .setSubject(login.usuario)
            .claim(JwtFilter.CLAIMS_GET, perfil)
            .setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS256, JwtFilter.SIGNING_KEY)
            .compact();

        return new LoginResponse(token, perfil);
    }

    private static class LoginRequest {
        private String usuario;
        private String senha;

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    public static class Perfil {

        private String login;
        private String senha;

        public Perfil(String login, String senha) {
            this.login = login;
            this.senha = senha;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    private static class LoginResponse {
        private String token;
        private Perfil perfil;

        LoginResponse(String token, Perfil perfil) {
            this.token = token;
            this.perfil = perfil;
        }

        public String getToken() {
            return token;
        }

        public Perfil getPerfil() {
            return perfil;
        }
    }
}
