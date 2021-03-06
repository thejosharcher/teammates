package teammates.common.util;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;

public final class HttpRequestHelper {

    private HttpRequestHelper() {
        // utility class
    }

    /**
     * Returns the first value for the key in the parameter map, or null if key not found.
     *
     * @param paramMap A parameter map (e.g., the kind found in HttpServletRequests)
     */
    public static String getValueFromParamMap(Map<String, String[]> paramMap, String key) {
        String[] values = paramMap.get(key);
        return values == null ? null : values[0];
    }

    /**
     * Returns all values for the key in the parameter map, or null if key not found.
     *
     * @param paramMap A parameter map (e.g., the kind found in HttpServletRequests)
     */
    public static String[] getValuesFromParamMap(Map<String, String[]> paramMap, String key) {
        String[] values = paramMap.get(key);
        return values == null ? null : values;
    }

    /**
     * Returns a HashMap object containing all the parameters key-value pairs from a URLFetchRequest object.
     */
    public static HashMap<String, String> getParamMap(URLFetchRequest request) {
        String requestBody = request.getPayload().toStringUtf8();
        String[] params = requestBody.split("&");
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for (String param : params) {
            String[] pair = param.split("=");
            String name = pair[0];
            String value = pair[1];
            try {
                String decodedValue = URLDecoder.decode(value, "UTF8");

                hashMap.put(name, decodedValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return hashMap;
    }

    /**
     * Returns the first value for the key in the request's parameter map, or null if key not found.
     *
     * @param req An HttpServletRequest which contains the parameters map
     */
    @SuppressWarnings("unchecked")
    public static String getValueFromRequestParameterMap(HttpServletRequest req, String key) {
        return getValueFromParamMap(req.getParameterMap(), key);
    }

    /**
     * Returns all values for the key in the request's parameter map, or null if key not found.
     *
     * @param req An HttpServletRequest which contains the parameters map
     */
    @SuppressWarnings("unchecked")
    public static String[] getValuesFromRequestParameterMap(HttpServletRequest req, String key) {
        return getValuesFromParamMap(req.getParameterMap(), key);
    }

    //TODO: rename to a better name
    public static String printRequestParameters(HttpServletRequest request) {
        StringBuilder requestParameters = new StringBuilder();
        requestParameters.append('{');
        for (Enumeration<?> f = request.getParameterNames(); f.hasMoreElements();) {
            String param = f.nextElement().toString();
            requestParameters.append(param).append("::");
            String[] parameterValues = request.getParameterValues(param);
            for (String parameterValue : parameterValues) {
                requestParameters.append(parameterValue).append("//");
            }
            requestParameters.setLength(requestParameters.length() - 2);
            requestParameters.append(", ");
        }
        if (!"{".equals(requestParameters.toString())) {
            requestParameters.setLength(requestParameters.length() - 2);
        }
        return requestParameters.append("}").toString();
    }

    /**
     * Returns the URL used for the HTTP request but without the domain, e.g. "/page/studentHome?user=james"
     */
    public static String getRequestedUrl(HttpServletRequest req) {
        String link = req.getRequestURI();
        String query = req.getQueryString();

        if (query != null && !query.trim().isEmpty()) {
            return link + "?" + query;
        }
        return link;
    }

}
