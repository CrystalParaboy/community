package com.nowcoder.community.community.Controller;

import com.nowcoder.community.community.dto.Access_tokenDTO;
import com.nowcoder.community.community.dto.GithubUser;
import com.nowcoder.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("J2EE_STORE_OF_NON_SERIALIZABLE_OBJECT_INTO_SESSION")
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        Access_tokenDTO access_tokenDTO = new Access_tokenDTO();
        access_tokenDTO.setClient_id(clientId);
        access_tokenDTO.setCode(code);
        access_tokenDTO.setRedirect_uri(redirectUri);
        access_tokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(access_tokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if (user != null) {
            //登陆成功，写cookie和session
            request.getSession().setAttribute("user", user);
            //跳转到index
            return "redirect:/";
        } else {
            return "redirect:/";
            //登录失败
        }
    }
}
