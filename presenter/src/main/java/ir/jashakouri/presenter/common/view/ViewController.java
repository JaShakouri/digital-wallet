package ir.jashakouri.presenter.common.view;

import ir.jashakouri.data.dto.response.payment.SepResTokenCallback;
import ir.jashakouri.data.enums.VerifyState;
import ir.jashakouri.domain.services.common.ViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;

/**
 * @author jash
 * @created 26/10/2022 - 19:06
 * @project digital-wallet-backend
 */
@Controller
@AllArgsConstructor
@Slf4j(topic = "LOG_ViewController")
public class ViewController {

    private final ViewService viewService;

    @GetMapping(path = {"", "/", "/api", "/swagger", "/swg"})
    public ModelAndView index() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }

    @PostMapping("/payment")
    public String payment(HttpServletRequest requestServlet) {
        var verifyResult = SepResTokenCallback.fromMap(requestServlet.getParameterMap());
        if (verifyResult != null && verifyResult.getState() == VerifyState.OK
                && verifyResult.getRefNum() != null) {
            var verifyRequest = viewService.verifyTransaction(verifyResult.getRefNum());

            if (verifyRequest != null && verifyRequest.getResultCode() == 0)
                if (viewService.depositWalletWithTransactionToken(verifyResult.getToken()))
                    return "payment";

        }

        return "unSuccess";
    }

}
