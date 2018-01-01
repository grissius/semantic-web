import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by smolijar on 12/21/17.
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Query query = QueryFactory.create("SELECT ?s WHERE {?s a <http://rdfs.org/ns/void#Dataset>}");
        try (QueryExecution qexec = QueryExecutionFactory
                .createServiceRequest("http://linked.opendata.cz/sparql", query)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode x = soln.get("s"); // Get a result variable by name.
                response.getWriter().println(String.format("<li>%s</li>", x.toString()));
            }
        }
        response.setContentType("text/html");
    }
}
