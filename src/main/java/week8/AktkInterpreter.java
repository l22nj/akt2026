package week8;

import week7.AktkAst;
import week7.ast.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AktkInterpreter {
    private final Environment<Object> env = new Environment<>();

    public static void run(String program) {
        var ast = AktkAst.createAst(program);
        var interpreter = new AktkInterpreter();
        interpreter.run(ast);
    }

    private void run(Statement node) {
        switch (node) {
            case Assignment assignment -> {
                var value = eval(assignment.getExpression());
                env.assign(assignment.getVariableName(), value);
            }
            case Block block -> {
                for (Statement statement : block.statements()) {
                    run(statement);
                }
            }
            case FunctionDefinition functionDefinition -> {
            }
            case IfStatement ifStatement -> {
            }
            case ReturnStatement returnStatement -> {
            }
            case VariableDeclaration variableDeclaration -> {
                // TODO: käsitle olukorda kus initializer on puudu!
                var value = eval(variableDeclaration.getInitializer());
                env.declareAssign(variableDeclaration.variableName(), value);
            }
            case WhileStatement whileStatement -> {
            }
            case ExpressionStatement expressionStatement -> {
                eval(expressionStatement.expression());
            }
        }
    }
    private Object eval(Expression expression) {
        return switch (expression) {
            case FunctionCall functionCall -> evalFunctionCall(functionCall);
            case IntegerLiteral integerLiteral -> integerLiteral.value();
            case StringLiteral stringLiteral -> stringLiteral.value();
            case Variable variable -> env.get(variable.getName());
        };
    }
    private Object evalFunctionCall(FunctionCall functionCall) {
        var name = functionCall.getFunctionName();
        var args = new ArrayList<Object>();
        for (var argument : functionCall.getArguments()) {
            args.add(eval(argument));
        }
        if (functionCall.isComparisonOperation()) {
            throw new UnsupportedOperationException();
        }
        else if (functionCall.isArithmeticOperation()) {
            return evalArithmeticOperation(name, args);
        }
        else {
            return evalBuiltInFunctionCall(name, args);
        }
    }
    private Object evalBuiltInFunctionCall(String name, ArrayList<Object> args) {
        try {
            var paramTypes = new Class<?>[args.size()];
            for (int i = 0; i < args.size(); i++) {
                paramTypes[i] = args.get(i).getClass();
            }
            Method method = AktkInterpreterBuiltins.class.getDeclaredMethod(name, paramTypes);
            return method.invoke(null, args.toArray());
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
    private Object evalArithmeticOperation(String name, List<Object> args) {
        switch (args.size()) {
            case 1 -> {
                throw new RuntimeException();
            }
            case 2 -> {
                var l = args.get(0);
                var r = args.get(1);
                if (l instanceof Integer left && r instanceof Integer right) {
                    return switch (name) {
                        case "+" -> left + right;
                        case "-" -> left - right;
                        case "*" -> left * right;
                        case "/" -> left / right;
                        case "%" -> left % right;
                        default -> throw new RuntimeException();
                    };
                } else if (l instanceof String left && r instanceof String right) {
                    if (name.equals("+")) {
                        return left + right;
                    }
                } else {
                    throw new RuntimeException("valet sorti muutujad aritmeetikaks");
                }
            }
            default -> throw new RuntimeException();
        }
        return null;
    }
}
