package com.spring.wefit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.wefit.command.UserVO;
import com.spring.wefit.user.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService service;
	
	
	@PostMapping("/join")
	public String join(UserVO vo,RedirectAttributes ra) {
		System.out.println("회원가입 컨트롤러 요청"+vo.toString());
		service.join(vo);
		service.mailSendWithUserKey(vo);
		ra.addFlashAttribute("msg","메일함을 확인해주세요");
		return "redirect:/";
	}
	
	@GetMapping("/auth/{nick}/{code}")
	public String auth(@PathVariable String nick, @PathVariable String code) {
		System.out.println("GET: 인증요청");
		System.out.println(nick);
		System.out.println(code);
		service.authUser(nick,code);
		
		return "redirect:/";
	}
	
	
	@PostMapping("/emailChk")
	@ResponseBody
	public String emailChk(@RequestBody String email) {
		System.out.println(service.emailCheck(email));
		if((service.emailCheck(email) == 0)) {
			return "success";
		}
		return "duplicate";
	}
	
	@PostMapping("/nickChk")
	@ResponseBody
	public String nickChk(@RequestBody String nick) {
		if(service.nickCheck(nick) == 0) {
			return "success";
		}
		return "duplicate";
		
	}
	
	@PostMapping("/login")
	public String login(UserVO vo, Model model, RedirectAttributes ra) {
		System.out.println("로그인 요청 : "+vo.toString());
		UserVO login = service.login(vo.getMEmail(), vo.getMPasswd());
		if(login != null) {
			if(login.getMEmailYN().equals("Y")) {
				model.addAttribute("user", login);
				model.addAttribute("msg", login.getMNick()+"님 환영합니다.");
				return "/home";			
			} else {
				ra.addFlashAttribute("msg","이메일 인증이 필요합니다.");
				return "redirect:/";
			}
		}
		ra.addFlashAttribute("msg", "이메일 또는 비밀번호가 틀렸습니다.");
		return "redirect:/";
		
	}
	
	
}
