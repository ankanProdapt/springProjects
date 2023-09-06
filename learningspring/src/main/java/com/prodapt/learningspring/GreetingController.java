package com.prodapt.learningspring;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.UserRepository;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class GreetingController {
  
  @Autowired
  private ApplicationContext ctx;
  @Autowired
  private RequestMappingHandlerMapping mappings;
  
  @Autowired
  private UserRepository userRepository;
  
  @GetMapping("/greeting")
  public String greeting(
      @RequestParam(name="name", required=false, defaultValue="World") String name, 
      @RequestParam(name="age", required=false, defaultValue="18") String age, 
      Model model) {
    model.addAttribute("name", name);
    model.addAttribute("age", age);
    return "greeting";
  }
  
  @GetMapping("/configs")
  public void getAllBeans(ServletResponse resp) throws IOException {
    String[] beans = ctx.getBeanDefinitionNames();
    var out = resp.getWriter();
    for (var bean : beans) {
      out.write(bean + "\n");
    }
    out.close();
  }
  
  @GetMapping("/endpoints")
  public ModelAndView getAllMappings() {
    var requestMappings = mappings.getHandlerMethods();
    var modelAndView = new ModelAndView("mappings");
    modelAndView.addObject("mappings", requestMappings);
    return modelAndView;
  }
  
  @GetMapping("/testing")
  public void testJpa(HttpServletResponse resp) throws IOException {
    Optional<User> u = userRepository.findByName("Aruvi");
    resp.getWriter().println(u.get());
  }

}
