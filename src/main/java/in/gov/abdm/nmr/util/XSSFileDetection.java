package in.gov.abdm.nmr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class XSSFileDetection {

	private static final Pattern scriptPattern =  Pattern.compile("<svg[^>]*>([\\s\\S]*?)alert\\s*\\(*\\)*|<script\\s*[^>*]*>([\\s\\S]*?)<*[^<]script\\s*>|javascript\\s*:*|onload(.*?)\\s*=|<script\\s*[^>*]*>", Pattern.CASE_INSENSITIVE);
	private static final Pattern urlPattern = Pattern.compile("(https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]", Pattern.CASE_INSENSITIVE);

    public static boolean isMaliciousCodeInFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && StringUtils.isNotBlank(multipartFile.getOriginalFilename())) {
            InputStream stream = multipartFile.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String contents = "";
            while ((contents = reader.readLine()) != null) {
                Matcher matcher = scriptPattern.matcher(contents);
                if (matcher.find()) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        }
        return false;
    }
    public static boolean isUrlInString(String content) {
    	Matcher matcher = urlPattern.matcher(content);
        if (matcher.find()) {
        	return true;
        }
        return false;
    }
	public static boolean isMaliciousCodeInFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
	        String contents = scanner.nextLine();
	        Matcher matcher = scriptPattern.matcher(contents);
	        if (matcher.find()) {
	        		scanner.close();
	                return true;
	            } 
        	}
        scanner.close();
        return false;
	}
}
