package com.shareadda.api.ShareAdda.User.Controller;

import com.shareadda.api.ShareAdda.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/get")
public class UserController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private CompanyAndSymbol companyAndSymbolRepository;
//    @GetMapping
//    public JSONArray  getMapping() throws IOException,ParseException {
//       // userRepository.save(new User());
//        //scrapteWebsite();
//
//        return newsList();
//       }

}
