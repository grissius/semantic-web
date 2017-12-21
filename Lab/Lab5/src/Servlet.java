import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QuerySolution;
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
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Query query = QueryFactory.create("SELECT ?s WHERE {?s a <http://rdfs.org/ns/void#Dataset>}");
        QueryExecution qexec = QueryExecutionFactory
                .createServiceRequest("http://linked.opendata.cz/sparql", query);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution() ;
                RDFNode x = soln.get("s") ; // Get a result variable by name.
                response.getWriter().println(x.toString());
            }
        } finally {
            qexec.close() ;
        }
    }
}
