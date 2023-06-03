package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberDto;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository repository;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/memberForm")
    public String memberForm() {
        return "memberForm";
    }

    @GetMapping("/editForm")
    public String editForm() {
        return "editForm";
    }

    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null)
            session.invalidate();
        return "redirect:home";
    }

    @PostMapping("/member")
    public String addMember(@ModelAttribute Member member, HttpServletRequest req, Model model) {

        System.out.println(member);

        try {
            Member mem = memberService.findMemberById(member.getId());
            if (mem != null) {
                model.addAttribute("message", "이미 가입되어 있습니다.");
                model.addAttribute("member", member);
                return "memberForm";
            }

            member.setMoney(50000);
            memberService.join(member);
            HttpSession session = req.getSession(true);
            session.setAttribute("login", member);
            return "redirect:home";
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public String loginMember(@RequestParam String id, @RequestParam String password, HttpServletRequest req, Model model) {

        Member member = memberService.findMemberById(id);
        if (member == null) {
            model.addAttribute("message", "가입되어 있지 않습니다.");
            model.addAttribute("member", new MemberDto(id, password));
            return "loginForm";
        }

        if (!member.getPassword().equals(password)) {
            model.addAttribute("message", "비밀번호가 다릅니다.");
            model.addAttribute("member", new MemberDto(id, password));
            return "loginForm";
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("login", member);
        return "redirect:home";
    }

}
