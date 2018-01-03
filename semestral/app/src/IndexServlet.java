import org.apache.jena.query.*;
import query.QueryBag;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by smolijar on 12/21/17.
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<h1>WordStock</h1>");
        ResultSet results = QueryBag.exec(QueryBag.index());
        response.getWriter().println("<table>");
        response.getWriter().println("<tr><th>Word</th><th>Part of speech</th><th>Frequency</th></tr>");
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            response.getWriter().println("<tr>");
            response.getWriter().println(String.format("<td><a href=\"/word?word=%s\">%s</a></td>", soln.get("word").asLiteral().getString(), soln.get("word").asLiteral().getString()));
            response.getWriter().println(String.format("<td>%s</td>", soln.get("pos").asLiteral().getString()));
            response.getWriter().println(String.format("<td>%s</td>", soln.get("freq").asLiteral().getInt()));
            response.getWriter().println("<tr/>");
        }
        response.getWriter().println("<table/>");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    }
}
