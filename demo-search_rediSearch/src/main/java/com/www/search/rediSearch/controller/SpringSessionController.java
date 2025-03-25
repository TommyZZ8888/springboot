package com.www.search.rediSearch.controller;

import com.www.search.rediSearch.service.RedisService;
import com.www.search.rediSearch.service.SampleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.SearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map.Entry;

@Controller
public class SpringSessionController {
	@Autowired
	RedisService redisService;
	
	@Autowired
	SampleDataService sampleDataService;
	
	private String prettyPrintSearchResult(SearchResult result) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Total result: ");
		sb.append(String.valueOf(result.getTotalResults()));
		sb.append("\n");
		
		ListIterator<Document> it = result.getDocuments().listIterator();
		while(it.hasNext()) {
			Document doc = it.next();

			sb.append("\n");
			sb.append("    Key: ");
			sb.append(doc.getId());
			sb.append(", score: ");
			sb.append(doc.getScore());
			sb.append("\n");
			
			for(Entry<String, Object> e: doc.getProperties()) {
				sb.append("        ");
				sb.append(e.getKey());
				sb.append(": ");
				sb.append(e.getValue());
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		model.addAttribute("queryResults", session.getAttribute("queryResults"));
		
		model.addAttribute("cif", session.getAttribute("cif"));
		model.addAttribute("fullText", session.getAttribute("fullText"));
		model.addAttribute("name", session.getAttribute("name"));
		model.addAttribute("number", session.getAttribute("number"));
		
		if(session.getAttribute("regStart") == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);
			session.setAttribute("regStart", new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime()));
		}
		if(session.getAttribute("regEnd") == null) {
			session.setAttribute("regEnd", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		}
		
		model.addAttribute("regStart", session.getAttribute("regStart"));
		model.addAttribute("regEnd", session.getAttribute("regEnd"));
		
		return "index";
	}
	
	@PostMapping("/populateData")
	public String populateData(HttpServletRequest request) {
		try {
			sampleDataService.PopulateSampleData(Long.valueOf(request.getParameter("numentries")));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/progress")
	public SseEmitter handle() {
        SseEmitter emitter = new SseEmitter();
        sampleDataService.addSseEmitter(emitter);
        return emitter;
    }

	@PostMapping("/searchByCif")
	public String searchByCif(HttpServletRequest request) {
		request.getSession().setAttribute("cif", request.getParameter("cif"));
		String cif = request.getParameter("cif").trim();
		if(cif.length() == 0) {
			request.getSession().setAttribute("queryResults", "");
		}
		else {
			request.getSession().setAttribute("queryResults", 
					prettyPrintSearchResult(redisService.searchByCifWithQueryBuilders(cif)));
		}
		
		return "redirect:/";
	}

	@PostMapping("/searchFullText")
	public String searchFullText(HttpServletRequest request) {
		request.getSession().setAttribute("fullText", request.getParameter("fullText"));
		String name = request.getParameter("fullText").trim();
		if(name.length() == 0) {
			request.getSession().setAttribute("queryResults", "");
		}
		else {
			request.getSession().setAttribute("queryResults", 
					prettyPrintSearchResult(redisService.searchFullText(name)));
		}
		
		return "redirect:/";
	}

	@PostMapping("/searchLastNameOnly")
	public String searchLastNameOnly(HttpServletRequest request) {
		request.getSession().setAttribute("name", request.getParameter("name"));
		String name = request.getParameter("name").trim();
		if(name.length() == 0) {
			request.getSession().setAttribute("queryResults", "");
		}
		else {
			request.getSession().setAttribute("queryResults", 
					prettyPrintSearchResult(redisService.searchLastNameOnlyWithQueryBuilders(name)));
		}
		
		return "redirect:/";
	}

	@PostMapping("/searchCardNumber")
	public String searchCardNumber(HttpServletRequest request) {
		request.getSession().setAttribute("number", request.getParameter("number"));
		String number = request.getParameter("number").trim();
		if(number.length() == 0) {
			request.getSession().setAttribute("queryResults", "");
		}
		else {
			request.getSession().setAttribute("queryResults", 
					prettyPrintSearchResult(redisService.searchCardNumberWithQueryBuilders(number)));
		}
		
		return "redirect:/";
	}

	@PostMapping("/searchRegistrationDate")
	public String searchRegistrationDate(HttpServletRequest request) {
		request.getSession().setAttribute("regStart", request.getParameter("regStart"));
		request.getSession().setAttribute("regEnd", request.getParameter("regEnd"));
		String regStart = request.getParameter("regStart").trim();
		String regEnd = request.getParameter("regEnd").trim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			request.getSession().setAttribute("queryResults", 
					prettyPrintSearchResult(redisService.searchRegistrationBetweenDateWithQueryBuilders(sdf.parse(regStart), sdf.parse(regEnd))));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return "redirect:/";
	}
}