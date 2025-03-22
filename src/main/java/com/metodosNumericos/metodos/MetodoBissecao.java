package com.metodosNumericos.metodos;

import org.mariuszgromada.math.mxparser.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoBissecao {

    public static class IteracaoInfo {
        public int iteracao;
        public double valorX;
        public double valorFx;
        public double erro;

        public IteracaoInfo(int iteracao, double valorX, double valorFx, double erro) {
            this.iteracao = iteracao;
            this.valorX = valorX;
            this.valorFx = valorFx;
            this.erro = erro;
        }
    }

    public static class ResultadoBissecao {
        public double raiz;
        public double erro;
        public int iteracoes;
        public List<IteracaoInfo> historico;

        public ResultadoBissecao(double raiz, double erro, int iteracoes, List<IteracaoInfo> historico) {
            this.raiz = raiz;
            this.erro = erro;
            this.iteracoes = iteracoes;
            this.historico = historico;
        }

        public double[] toArray() {
            return new double[]{raiz, erro, iteracoes};
        }
    }

    // Implementação principal do método da bisseção que registra o histórico completo
    public static ResultadoBissecao calcularComHistorico(String expressao, double a, double b, double erroMax, int iterMax) {
        String funcao = expressao.replace("=0", "").trim();
        Function f = new Function("f(x) = " + funcao);

        double fa = avaliarFuncao(f, a);
        double fb = avaliarFuncao(f, b);

        List<IteracaoInfo> historico = new ArrayList<>();

        // Verificação do Teorema de Bolzano
        if (fa * fb >= 0) {
            return new ResultadoBissecao(Double.NaN, 0, 0, historico);
        }

        double c = 0;
        double fc;
        double erro;
        int iter = 0;
        double cAnterior;

        do {
            cAnterior = c;

            c = (a + b) / 2;
            fc = avaliarFuncao(f, c);
            erro = Math.abs(b - a) / 2;

            double erroIteracao = iter == 0 ? erro : Math.abs(c - cAnterior);
            historico.add(new IteracaoInfo(iter + 1, c, fc, erroIteracao));

            if (fa * fc < 0) {
                b = c;
            } else {
                a = c;
                fa = fc;
            }

            iter++;

            if (Math.abs(fc) < 1e-10) break;
            if (erro < erroMax) break;

        } while (iter < iterMax);

        return new ResultadoBissecao(c, erro, iter, historico);
    }

    private static double avaliarFuncao(Function f, double x) {
        Argument arg = new Argument("x = " + x);
        Expression e = new Expression("f(x)", f, arg);
        return e.calculate();
    }

    public static double[] calcular(String expressao, double a, double b, double erroMax, int iterMax) {
        ResultadoBissecao resultado = calcularComHistorico(expressao, a, b, erroMax, iterMax);
        return resultado.toArray();
    }

    public static double[] calcularPorErro(String expressao, double a, double b, double erroMax) {
        int iterMax = 1000;
        return calcular(expressao, a, b, erroMax, iterMax);
    }

    public static double[] calcularPorIteracoes(String expressao, double a, double b, int iterMax) {
        double erroMax = 1e-10;
        return calcular(expressao, a, b, erroMax, iterMax);
    }
}