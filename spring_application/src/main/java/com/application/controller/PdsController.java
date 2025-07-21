package com.application.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.application.command.PageMaker;
import com.application.command.PdsModifyCommand;
import com.application.command.PdsRegistCommand;
import com.application.dto.PdsVO;
import com.application.service.PdsService;
import com.josephoconnell.html.HTMLInputFilter;

@Controller
@RequestMapping("/pds")
public class PdsController {

	@Autowired
	private PdsService pdsService;
	
	
	@GetMapping("/main")
	public void main() {}
	
	@GetMapping("/list")
	public ModelAndView list(@ModelAttribute PageMaker pageMaker, ModelAndView mnv) throws Exception {
		String url="/pds/list";
		
		List<PdsVO> pdsList = pdsService.searchList(pageMaker);
		
		mnv.addObject("pdsList",pdsList);
		mnv.setViewName(url);
		return mnv;
	}
	
	@GetMapping("/registForm")
	public ModelAndView registForm(ModelAndView mnv) throws Exception {
		String url="/pds/regist";
		mnv.setViewName(url);
		return mnv;
	}
	
	@PostMapping(value = "/regist", produces = "text/plain;charset=utf-8")
	public ModelAndView regist(PdsRegistCommand regCommand, ModelAndView mnv) throws Exception {
		String url="/pds/regist_success";
		
		//DB
		PdsVO pds = regCommand.toPdsVO();
		pds.setTitle(HTMLInputFilter.htmlSpecialChars(pds.getTitle()));
		
		pdsService.regist(pds);
		
		mnv.setViewName(url);
		return mnv;
	}
	
	@GetMapping("/detail")
	public ModelAndView detail(int pno, HttpSession session, ModelAndView mnv) throws Exception {
		String url="/pds/detail";
		
		ServletContext ctx = session.getServletContext();
		
		String key = "pds:"+pno;
		
		PdsVO pds = null;
		if(ctx.getAttribute(key)!=null) {
			pds = pdsService.getPds(pno);
		}else {
			pds = pdsService.detail(pno);
			ctx.setAttribute(key, key);
		}
		
		mnv.addObject("pds",pds);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	@GetMapping("/modify")
	public void modifyForm(int pno, Model model) throws Exception {
		PdsVO pds = pdsService.getPds(pno);
		model.addAttribute("pds", pds);
	}
	
	@PostMapping("modify")
	public ModelAndView modify(PdsModifyCommand modCommand, ModelAndView mnv) throws Exception{
		String url = "pds/modify_success";
		
		// PdsVO setting
		PdsVO pds = modCommand.toPdsVO();
		
		// DB 저장
		pds.setTitle(HTMLInputFilter.htmlSpecialChars(pds.getTitle()));
		pdsService.modify(pds);
		
		mnv.setViewName(url);
		return mnv;
	}
	
	@GetMapping("/remove")
	public ModelAndView remove(int pno, ModelAndView mnv) throws Exception {
		String url = "/pds/renove_success";
		
		//DB 삭제
		pdsService.remove(pno);
		
		mnv.setViewName(url);
		return mnv;
	}
	
}
