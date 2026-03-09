package fr.cmp.kyrios.config;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.webmvc.autoconfigure.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class ErrorViewConfig {

    @Bean
    public ErrorViewResolver custom404ErrorViewResolver() {
        return new ErrorViewResolver() {
            @Override
            public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
                    Map<String, Object> model) {
                if (status != null && status.value() == 404) {
                    Map<String, Object> viewModel = new HashMap<>(model);
                    viewModel.put("currentPage", "");
                    viewModel.put("pageTitle", "Page non trouvee");
                    viewModel.put("pageHeader", "Erreur 404");
                    viewModel.put("pageDescription", "La page demandee est introuvable.");
                    viewModel.put("pageCss", "error");
                    viewModel.put("contentPage", "error404.jsp");
                    viewModel.put("requestedPath", extractRequestedPath(request));
                    return new ModelAndView("layout", viewModel);
                }

                return null;
            }
        };
    }

    private String extractRequestedPath(HttpServletRequest request) {
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        return path == null ? "" : path.toString();
    }
}
