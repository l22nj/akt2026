grammar Estolog;
@header { package proovieksam; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : (definitsioon ';')* avaldis
    ;

definitsioon
    : Muutuja ':=' avaldis
    ;

avaldis
    : Arv       #Literaal
    | Muutuja   #Muutuja
    | '(' avaldis ')' #Sulud
    | left=avaldis op=ningOp right=avaldis   #BinOp
//    | left=avaldisPealeVordust op=vordusOp right=avaldisPealeVordust #VordusOp
//    | kuiLause #KuiLause
    ;

vordusOp: '=';

avaldisPealeVordust
    : Arv
    | Muutuja
    | '(' avaldis ')'
    | left=avaldis op=ningOp right=avaldis
    ;

ningOp
    : 'NING'
    | 'VOI'
    | 'JA'
    ;

// Operaatorid tuleks teha kihiliselt, läbilaskega. See on palju ohutum.
// Võrduse saab kätte viies võrdusExpr otse edasi sulgude-expressioniks,
// mitte mõlemale poole võrdusExpr ise.

// Siin kõige kõrgema prioriteediga peaks olema kuiExpr

Muutuja: [a-zA-Z]+;
Arv: [01];

WS: [ \t\r\n]+ -> skip;
