package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {
    @GetMapping("/client/logout1")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Xoá cookie bằng cách đặt thời gian hết hạn của nó trong quá khứ
        Cookie cookie = new Cookie("username", "");
        cookie.setMaxAge(0); // Đặt thời gian hết hạn là 0 để xoá cookie
        cookie.setPath("/"); // Đảm bảo path của cookie khớp với path ban đầu khi tạo cookie
        response.addCookie(cookie);
        // Chuyển hướng đến trang đăng xuất hoặc trang khác
        return "redirect:/client/index"; // Thay đổi URL mục tiêu nếu cần
    }
}
