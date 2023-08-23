package com.example.controller;

import com.example.entity.Account;
import com.example.jparepository.AccountRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RegistrationController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/dangky")
    public String listAccounts(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "/dangky"; // Thay thế bằng tên của view template
    }

    @PostMapping("/dangky")
    public String registerPost(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/dangky";
        }
        // Kiểm tra trùng tên đăng nhập
        Account existingUser = accountRepository.findByUserName(account.getUserName());
        if (existingUser != null) {
            result.rejectValue("userName", "error.account", "Username is already taken");
            return "/dangky";
        }

        // Kiểm tra trùng email
        existingUser = accountRepository.findByEmail(account.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", "error.account", "Email is already registered");
            return "/dangky";
        }

        account.setActive(true);
        accountRepository.save(account);
        model.addAttribute("messages", "Register success. ");
        model.addAttribute("accounts", accountRepository.findAll());
        return "/dangky";
    }
}
