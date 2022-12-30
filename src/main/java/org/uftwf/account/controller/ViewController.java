package org.uftwf.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.uft.plunkit.ChangeOfStatus;
import org.uftwf.account.service.MySqlService;
import org.uftwf.account.service.UserService;
import org.uftwf.account.util.MySqlConnectionFactory;
import org.uftwf.ssoclient.KeycloakHttp;
import org.uftwf.ssoclient.SSOClient;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xyang on 4/27/17.
 */
@Controller
public class ViewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);
    @Autowired
    UserService userService;
    KeycloakHttp keycloakHttp = new KeycloakHttp();
    SSOClient client = new SSOClient("uft", true);

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getAccount(HttpServletRequest request) {

        if (!MySqlConnectionFactory.canConnect()) {
            return "error";
        } else {
            if (MySqlService.getInstance().isBatchRunning()) {
                return "batchprocess";
            }
            LOGGER.info("getAccount(): return Account page" + "\r\n");
            String ssoId = keycloakHttp.getSSOId(request);
            userService.setUserId(ssoId);
            String memberId = keycloakHttp.getAttribute(request, "member_id");
            if (memberId == null) {
                memberId = MySqlService.getInstance().getMemberIdBySSOId(ssoId);
            }
            if (memberId != null && memberId.length() > 0) {
                userService.setMemberId(memberId);
                if (MySqlService.getInstance().existingMember(memberId)) {
                    MySqlService.getInstance().updateEmailStatus(memberId);
                }
            }
            Boolean isCCP = client.hasUserGroup(ssoId, "CCP_group");
            userService.setCCP(isCCP);
            return "index";
        }
    }

    @RequestMapping(value = "/index/", method = RequestMethod.GET)
    public ModelAndView getIndexSlash(HttpServletResponse response) {
        LOGGER.info("getIndexSlash(): direct to login page" + "\r\n");
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getDefault(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("getDefault: direct to login page" + "\r\n");
        try {
            response.sendRedirect("index");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(HttpServletResponse response) {
        return "error";
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.cleanUserService();
            String ssoId = keycloakHttp.getSSOId(request);
            client.logout(ssoId);
            InitialContext ctx = new InitialContext();
            String url = (String) ctx.lookup("java:global/uft/url");
            HttpSession session = request.getSession(false);

            request.logout();
            response.sendRedirect(url);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public void verify(HttpServletResponse response) {
        LOGGER.info("verify(): direct to verify application" + "\r\n");
        try {
            InitialContext ctx = new InitialContext();
            String url = (String) ctx.lookup("java:global/verify/url");
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public void home(HttpServletResponse response) {
        LOGGER.info("home(): direct to uft main application" + "\r\n");
        try {
            InitialContext ctx = new InitialContext();
            String url = (String) ctx.lookup("java:global/uft/url");
            userService.cleanUserService();
            response.sendRedirect(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/uftHeader")
    public String getUftHeader() {
        return "uftHeader";
    }

    @RequestMapping(value = "/uftFooter")
    public String getUftFooter() {
        return "uftFooter";
    }

    @RequestMapping(value = "/version")
    public String getVersion() {
        return "version";
    }
}
