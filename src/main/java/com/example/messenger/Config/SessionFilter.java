package com.example.messenger.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        chain.doFilter(req, res);

        removeHttpOnlyFlag(res);
    }

    private void removeHttpOnlyFlag(HttpServletResponse res) {
        String setCookieHeaderName = "set-cookie";
        String setCookieHeader = res.getHeader(setCookieHeaderName);
        System.out.println(setCookieHeader);

        if (setCookieHeader != null) {
            setCookieHeader = setCookieHeader.replace("; HttpOnly", "");
            res.setHeader(setCookieHeaderName, setCookieHeader);
            res.addHeader("sesssion",setCookieHeader);
        }
    }
}
