package com.academia.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Interés mensual según el saldo (tabla del banco):
 *
 *   saldo menor a $1,000.00                 →  0 %
 *   de $1,000.00 hasta menos de $50,000.00  →  0.5 %
 *   de $50,000.00 en adelante               →  1 %
 *
 * El resultado se redondea a centavos «como en la escuela» (HALF_UP: 0.005 sube a 0.01).
 * Un saldo nulo o negativo es un error.
 */
public final class Interes {

    private static final BigDecimal MIL = new BigDecimal("1000.00");
    private static final BigDecimal CINCUENTA_MIL = new BigDecimal("50000.00");

    private Interes() {
    }

    public static BigDecimal mensual(BigDecimal saldo) {
        if (saldo == null || saldo.signum() < 0) {
            throw new IllegalArgumentException("El saldo no puede ser nulo ni negativo");
        }
        BigDecimal tasa;
        if (saldo.compareTo(MIL) < 0) {
            tasa = BigDecimal.ZERO;
        } else if (saldo.compareTo(CINCUENTA_MIL) < 0) {
            tasa = new BigDecimal("0.005");
        } else {
            tasa = new BigDecimal("0.01");
        }
        return saldo.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }
}
