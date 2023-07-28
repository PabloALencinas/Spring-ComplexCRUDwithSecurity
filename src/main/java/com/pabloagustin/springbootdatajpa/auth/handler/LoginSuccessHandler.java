package com.pabloagustin.springbootdatajpa.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;

// ANOTAMOS CON @Component PARA PODER INYECTAR DENTRO DEL FILTRO, IMPORTANTE!!!
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Session flash map manager, administrador de map para msj flash
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();

        flashMap.put("success", "Ha iniciado sesion con exito!");

        flashMapManager.saveOutputFlashMap(flashMap, request, response);


        super.onAuthenticationSuccess(request, response, authentication);
    }
}
