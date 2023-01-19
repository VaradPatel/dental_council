package in.gov.abdm.nmr.util;

import java.util.HashMap;
import java.util.Map;

public class TemplatedStringBuilder {

    private final static String TEMPLATE_START_TOKEN = "{#";
    private final static String TEMPLATE_CLOSE_TOKEN = "#}";

    private final String template;
    private final Map<String, String> parameters = new HashMap<>();

    public TemplatedStringBuilder(String template) {
        if (template == null) throw new NullPointerException();
        this.template = template;
    }

    public TemplatedStringBuilder replace(String key, String value){
        parameters.put(key, value);
        return this;
    }

    public String finish(){

        StringBuilder result = new StringBuilder();

        int startIndex = 0;

        while (startIndex < template.length()){

            int openIndex  = template.indexOf(TEMPLATE_START_TOKEN, startIndex);

            if (openIndex < 0){
                result.append(template.substring(startIndex));
                break;
            }

            int closeIndex = template.indexOf(TEMPLATE_CLOSE_TOKEN, openIndex);

            if(closeIndex < 0){
                result.append(template.substring(startIndex));
                break;
            }

            String key = template.substring(openIndex + TEMPLATE_START_TOKEN.length(), closeIndex);

            if (!parameters.containsKey(key)) throw new RuntimeException("missing value for key: " + key);

            result.append(template.substring(startIndex, openIndex));
            result.append(parameters.get(key));

            startIndex = closeIndex + TEMPLATE_CLOSE_TOKEN.length();
        }

        return result.toString();
    }
}
