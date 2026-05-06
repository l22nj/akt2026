grammar Aktk;
@header { package week7; }

program
    : statements EOF
    ;

statements
    : statement (';' statement)*
    ;

statement
    : /*varDeclaration
    | assignment
    | ifStatement
    | whileStatement
    | funDefinition
    | returnStatement
    |*/ exprStatement
    | block           /*loogelistes sulgudes lausete jada*/
    ;

block
    : '{' statements '}'
    ;

exprStatement
    : expr
    ;

expr
    : simpleExpr
    ;

// Siia vahele operaatorid
//plusExpr
//    : left = plusExpr op='+' right=mulExpr
//    | left=plusExpr op='-' right=mulExpr
//    | mulExpr
//    ;
// AST veel vaja gteha

simpleExpr
    : IntLiteral    #IntLiteral
    | StrLiteral    #StrLiteral
    | Variable      #Variable
    ;

IntLiteral: [0-9] | [1-9][0-9]+;
StrLiteral: '"' ~["\n]* '"';
Variable: [A-Za-z][A-Za-z0-9_]*;