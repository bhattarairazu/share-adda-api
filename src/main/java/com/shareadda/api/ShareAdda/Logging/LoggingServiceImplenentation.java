package com.shareadda.api.ShareAdda.Logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingServiceImplenentation implements LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImplenentation.class);
    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
    try{
        StringBuilder stringBuilder = new StringBuilder();
        Map<String,String> parameters = buildParamters(httpServletRequest);
        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("userAuthId=[").append(httpServletRequest.getRemoteUser())
                .append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("query=[").append(httpServletRequest.getQueryString())
                .append("] ");
        stringBuilder.append("address=[").append(httpServletRequest.getRemoteAddr())
                .append("] ");
        stringBuilder.append("requestFrom =[").append(getRequestSource(httpServletRequest)).append("]");


        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("body=[" + body.toString() + "]");
        }

        logger.info(stringBuilder.toString());
    }catch (Exception e){

    }
    }

    private String getRequestSource(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Mobile-Agent");
        if(header == null){
            return "WEB_APP";
        } else {
            String[] values = header.split(",");
            return values[1];
        }
    }

    private Map<String, String> buildParamters(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parametersNames = httpServletRequest.getParameterNames();
        while (parametersNames.hasMoreElements()) {
            String key = parametersNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) throws IllegalAccessException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RESPONSE ");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("userAuthId=[").append(httpServletRequest.getRemoteUser())
                    .append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("statusCode=[").append(httpServletResponse.getStatus())
                    .append("] ");
            stringBuilder.append("address=[").append(httpServletRequest.getRemoteAddr())
                    .append("] ");
            stringBuilder.append("requestFrom =[").append(getRequestSource(httpServletRequest)).append("] ");
            if (body != null) {
                stringBuilder.append("body=[" + body.toString() + "]");
            }

            logger.info(stringBuilder.toString());
        } catch (Exception e) {
            logger.error("logging got exception. {}", e.getMessage());
        }
    }
}
