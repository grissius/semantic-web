# Word stock

## Datasets

1. Hunspell Czech dictionary
2. Czech Wiktionary XML dump containing additional word information (part of speech, ethymology, ...)
3. Word frequencies gathered from OpenSubtitles
4. List of stop words for Czech language

![](https://i.imgur.com/dKfwdBn.png)

## Triplification

* Tarql
* 4 mapping SPARQL scripts
* owl, rdfs, rdf, dcterms, xsd, **lemon** (lexical vocabulary), **lexinfo** (NLP), **olia/uby** (hyphenation).
* DCAT-AP

### Custom properties

#### Stopword

```sparql
  ex:stopword
    a rdf:Property, owl:ObjectProperty ;
    rdfs:comment "Indicates if given word is a stopword."@en ;
    rdfs:domain lemon:Word ;
    rdfs:label "Stopword"@en ;
    rdfs:range xsd:boolean .
```

#### Word frequency

```sparql
ex:wordFrequency
    a rdf:Property, owl:ObjectProperty ;
    rdfs:comment "Number of occurences for given word in corpus of available subtitles for given language at OpenSubtitles.com"@en ;
    rdfs:domain lemon:Word ;
    rdfs:label "Word frequency"@en ;
    rdfs:subPropertyOf dcterms:Frequency ;
    rdfs:range xsd:integer .
```

## Linking

* Silk
* Internal trivial + tokenization from definition
* External Wiktionary
* VoID

## Querying

* 8 SPARQL queries
* Apache Jena Fuseki
* e.g.

```sparql
# Find all detailed information for a single given word.
# Traverse linked entities
PREFIX ex:  <http://example.com/#>
PREFIX lemon: <http://lemon-model.net/lemon#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX uby: <http://purl.org/olia/ubyCat.owl#>

SELECT ?word ?freq ?pos ?sense ?hyp ?ethym
WHERE {
	?wordUri lemon:lexicalForm ?word ;
          owl:sameAs*/ex:wordFrequency ?freq ;
          owl:sameAs*/ex:stopword ?stop ;
          owl:sameAs*/lexinfo:partOfSpeech/rdfs:label ?pos .
  OPTIONAL {
    ?wordUri owl:sameAs*/uby:hyphenation ?hyp ;
          owl:sameAs*/lemon:sense/lemon:subsense ?sense ;
          owl:sameAs*/lemon:sense/ex:ethymology ?ethym.
  }
  FILTER(STR(?word) = "pravda")
}
ORDER BY DESC(?freq)
```

## Servlet Application

* Simple index, detail
* 2 servlets
* Used prepared queries
* localhost Fuseki endpoint
* Apache Jena API

### Index

![](https://i.imgur.com/44ClPXP.png)

### Detail

![](https://i.imgur.com/gs2QSe3.png)
