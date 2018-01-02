#!/bin/sh
tarql hunspell.sparql ../../source/out.hunspell.csv > ../output/out.hunspell.ttl
tarql stopword.sparql ../../source/out.stopword.csv > ../output/out.stopword.ttl
tarql freq.sparql ../../source/out.freq.csv > ../output/out.freq.ttl
