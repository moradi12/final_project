package Openconnection.example.demo.Controllers;

import Openconnection.example.demo.Service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


//@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class LoginController {
    private final LoginService loginService;
 }
