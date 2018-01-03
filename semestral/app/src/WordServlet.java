import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
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
@WebServlet("/word")
public class WordServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultSet results = QueryBag.execSelect(QueryBag.word(request.getParameter("word")));
        response.getWriter().println("<a href=\"/index\">back to index</a>");
        while (results.hasNext()) {
            response.getWriter().println("<table>");
            QuerySolution soln = results.nextSolution();
            response.getWriter().println("<h1>Word: " + soln.get("word").asLiteral().getString() + "</h1>");
            response.getWriter().println("<h2>Meta</h2>");
            response.getWriter().println(String.format("<tr><th>Word</th><td>%s</td></tr>", soln.get("word").asLiteral().getString()));
            response.getWriter().println(String.format("<tr><th>PartOfSpeech</th><td>%s</td></tr>", soln.get("pos").asLiteral().getString()));
            response.getWriter().println(String.format("<tr><th>Hyphenation</th><td>%s</td></tr>", soln.get("hyp").asLiteral().getString()));
            response.getWriter().println(String.format("<tr><th>Frequency</th><td>%s</td></tr>", soln.get("freq").asLiteral().getInt()));
            response.getWriter().println(String.format("<tr><th>Meaning</th><td>%s</td></tr>", soln.get("sense").asLiteral().getString()));
            response.getWriter().println(String.format("<tr><th>Ethymology</th><td>%s</td></tr>", soln.contains("ethym") ? soln.get("ethym").asLiteral().getString(): ""));
            response.getWriter().println("<table/>");
        }
        response.getWriter().println("<h2>Links</h2>");
        results = QueryBag.execSelect(QueryBag.wiktionaryLink(request.getParameter("word")));
        while (results.hasNext()) {
            response.getWriter().println("<h3>Wiktionary</h3>");
            QuerySolution soln = results.nextSolution();
            response.getWriter().println("<a href=\"" + (soln.contains("another") ? soln.get("another").asResource().getURI() : "#") + "\">Wiktionary link</a>");
        }
        results = QueryBag.execSelect(QueryBag.related(request.getParameter("word")));
        response.getWriter().println("<h3>Related</h3>");
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            response.getWriter().println("<li><a href=\"/word?word=" + soln.getLiteral("another").getString() +  "\">" + soln.getLiteral("another").getString() + "</a></li>");
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    }
}
