import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
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
        Model freqs = QueryBag.execConstruct(QueryBag.normalizedFreq());
        response.getWriter().println("<pre>");
        StmtIterator freqIter = freqs.listStatements();
        response.getWriter().println();
        response.getWriter().println("</pre>");
        ResultSet results = QueryBag.execSelect(QueryBag.index());
        response.getWriter().println("<table>");
        response.getWriter().println("<tr><th>Word</th><th>Part of speech</th><th>Frequency</th><th>Relative Frequency</th></tr>");
        while (results.hasNext() && freqIter.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Statement stmt = freqIter.nextStatement();
            response.getWriter().println("<tr>");
            response.getWriter().println(String.format("<td><a href=\"/word?word=%s\">%s</a></td>", soln.get("word").asLiteral().getString(), soln.get("word").asLiteral().getString()));
            response.getWriter().println(String.format("<td>%s</td>", soln.get("pos").asLiteral().getString()));
            response.getWriter().println(String.format("<td>%s</td>", soln.get("freq").asLiteral().getInt()));
            response.getWriter().println(String.format("<td>%s</td>", stmt.getObject().asLiteral().getDouble()));
            response.getWriter().println("<tr/>");
        }
        response.getWriter().println("<table/>");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    }
}
