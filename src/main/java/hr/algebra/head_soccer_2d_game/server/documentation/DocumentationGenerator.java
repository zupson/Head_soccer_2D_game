package hr.algebra.head_soccer_2d_game.server.documentation;

import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentationGenerator {
    private static final String OUTPUT_PATH = "documentation/business_logic_doc.html";

    public static void generateDocumentation(List<Class<?>> classes) {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            out.println("<html><head><title>Business Logic Documentation</title></head><body>");
            out.println("<h1>Business Logic Documentation</h1>");
            for (Class<?> c : classes) {
                writeClassDocumentation(c, out);
            }
            out.println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeClassDocumentation(Class<?> c, PrintWriter out) {
        if (!c.isAnnotationPresent(BusinessLogic.class))
            return;

        BusinessLogic annotation = c.getAnnotation(BusinessLogic.class);
        out.println("<h2>" + c.getSimpleName() + "</h2>");
        out.println("<p><b>Description:</b> " + annotation.description() + "</p>");
        out.println("<h3>Methods:</h3><ul>");

        for (Method m : c.getDeclaredMethods()) {
            out.println("<li><b>" + m.getName() + "</b>");
            out.println(" | Returns: " + m.getReturnType().getSimpleName());
            out.println(" | Params: [" + formatParams(m) + "]</li>");
        }
        out.println("</ul><hr>");
    }

    private static String formatParams(Method m) {
        return Arrays.stream(m.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }
}