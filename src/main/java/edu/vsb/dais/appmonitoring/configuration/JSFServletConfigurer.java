package edu.vsb.dais.appmonitoring.configuration;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasekric on 8. 5. 2015.
 */
@Configuration
@EnableWebMvc
public class JSFServletConfigurer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        //servletContext.setInitParameter("primefaces.THEME", "excite-bike");
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "0");
        servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
    }

    @Bean
    public ServletRegistrationBean restServlet() {
        FacesServlet servlet = new FacesServlet();

        ServletRegistrationBean registration = new ServletRegistrationBean(servlet, "*.xhtml");
        registration.setName("JSFServletJAVA");
        registration.addUrlMappings("*.xhtml");
        registration.setLoadOnStartup(1);
        Map<String, String> initParams = new HashMap<>();
        initParams.put("transformWsdlLocations", "true");
        registration.setInitParameters(initParams);
        return registration;
    }


}
