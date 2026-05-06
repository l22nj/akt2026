grammar Imp;
@header { package toylangs.imp; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)


prog : (assign ',')* plusExpr;

//5. Omistamine koosneb muutujanimest, võrdusmärgist (=) ja avaldisest.

assign: Variable '=' plusExpr;

// whitespace?

//1. Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number.
//2. Muutujateks on üksikud ladina väike- ja suurtähed.

Literal: '0' | [1-9][0-9]*;
Variable: [a-zA-Z];

//3. Operaatorid on järgmised: liitmine (binaarne +), jagamine (binaarne /) ja vastandarvu võtmine (unaarne -).
//      Nende prioriteedid ja assotsiatiivsused on standardsed.

plusExpr
    : left=plusExpr '+' right=divExpr #BinaryPlus
    | divExpr                         #SimplePlus
    ;

divExpr
    : left=divExpr '/' right=unaryExpr #BinaryDiv
    | unaryExpr                          #SimpleDiv
    ;

unaryExpr
    : '-' unaryExpr #UnaryMinus
    | simpleExpr    #SimpleMinus
    ;

//4. Avaldise ümber võivad olla sulud.

simpleExpr
    : '(' plusExpr ')' #Paren
    | Literal          #Literal
    | Variable         #Variable
    ;


//6. Programm koosneb omistamistest (mida võib olla ka null tükki) ja lõppavaldisest.
//      Omistamised on omavahel ja lõppavaldisest eraldatud komadega (,).

// --programmi alguses

//7. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

WS: [ \t\r\n]+ -> skip;