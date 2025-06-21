package jimenezj.tripwise.utils;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Utility class to load HTML templates from resources
@Component
public class HtmlTemplateLoader {

    // Loads an HTML template from the resources directory
    public String loadTemplate(String pathInResources) {
        // Validate the path
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(pathInResources)) {
            if (is == null)
                throw new IllegalArgumentException("template not found: " + pathInResources);
            // Read the content of the template file and return it as a String
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading HTML template", e);
        }
    }
}