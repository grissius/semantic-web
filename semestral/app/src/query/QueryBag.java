package query;

import org.apache.jena.query.*;

/**
 * Created by smolijar on 1/3/18.
 */
public class QueryBag {
    public static Query index() {
        return QueryFactory.create("PREFIX ex:  <http://example.com/#>\n" +
                "PREFIX lemon: <http://lemon-model.net/lemon#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?word ?freq ?pos\n" +
                "WHERE {\n" +
                "\t?wordUri lemon:lexicalForm ?word ;\n" +
                "          owl:sameAs/ex:wordFrequency ?freq;\n" +
                "                    owl:sameAs/ex:stopword ?stop;\n" +
                "                    owl:sameAs/lexinfo:partOfSpeech/rdfs:label ?pos .\n" +
                "  FILTER(!?stop)\n" +
                "}\n" +
                "ORDER BY DESC(?freq)");
    }
    public static Query word(String word) {
        return QueryFactory.create("PREFIX ex:  <http://example.com/#>\n" +
                "PREFIX lemon: <http://lemon-model.net/lemon#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX uby: <http://purl.org/olia/ubyCat.owl#>\n" +
                "\n" +
                "SELECT ?word ?freq ?pos ?sense ?hyp ?ethym\n" +
                "WHERE {\n" +
                "\t?wordUri lemon:lexicalForm ?word ;\n" +
                "          owl:sameAs*/ex:wordFrequency ?freq ;\n" +
                "                    owl:sameAs*/ex:stopword ?stop ;\n" +
                "                    owl:sameAs*/lexinfo:partOfSpeech/rdfs:label ?pos ;\n" +
                "                    owl:sameAs*/uby:hyphenation ?hyp ;\n" +
                "                    owl:sameAs*/lemon:sense/lemon:subsense ?sense ;\n" +
                "\t\t\t\t\towl:sameAs*/lemon:sense/ex:ethymology ?ethym .\n" +
                "  FILTER(STR(?word) = \"" + word + "\")\n" +
                "}\n" +
                "ORDER BY DESC(?freq)\n");
    }
    public static Query related(String word) {
        return QueryFactory.create("# Find all detailed information for a single given word.\n" +
                "# Traverse linked entities\n" +
                "PREFIX ex:  <http://example.com/#>\n" +
                "PREFIX lemon: <http://lemon-model.net/lemon#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX uby: <http://purl.org/olia/ubyCat.owl#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "\n" +
                "SELECT ?word ?another\n" +
                "WHERE {\n" +
                "\t?wordUri lemon:lexicalForm ?word ;\n" +
                "          lemon:sense/<http://www.w3.org/2000/01/rdf-schema#seeAlso>/lemon:lexicalForm ?another .\n" +
                "  FILTER(STR(?word) = \"" + word + "\")\n" +
                "}\n");
    }
    public static Query wiktionaryLink(String word) {
     return QueryFactory.create("PREFIX lemon: <http://lemon-model.net/lemon#>\n" +
             "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
             "\n" +
             "SELECT ?another\n" +
             "WHERE {\n" +
             "\t?wordUri lemon:lexicalForm ?word ;\n" +
             "          owl:sameAs ?another .\n" +
             "  FILTER(CONTAINS(STR(?another), \"wiktionary.org\") && STR(?word) = \""+ word +"\")\n" +
             "}\n");
    }
    public static ResultSet exec(Query query) {
        QueryExecution qexec = QueryExecutionFactory
                .createServiceRequest("http://localhost:3030/semestral/sparql", query);
        return qexec.execSelect();
    }
}
