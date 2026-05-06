grammar SimpleRegExp;
@header { package week7.harjutused; }

// __Regulaaravaldise põhjal__
// peab ära tundma täpselt samad sõnad nagu see regulaaravaldis: (a|b)*c

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : ('a'|'b')*'c' EOF;  // siit peab grammatika algama
