package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class DepositoTest {

    @Test
    void unaCuentaNuevaEmpiezaEnCero() {
        // Arrange (preparar)
        CuentaBancaria cuenta = new CuentaBancaria("Ana");

        // Act (actuar): aquí no hay acción, solo leemos el saldo

        // Assert (verificar)
        assertEquals(new BigDecimal("0.00"), cuenta.getSaldo());
    }

    @Test
    void depositarSumaAlSaldo() {
        // Arrange
        CuentaBancaria cuenta = new CuentaBancaria("Ana");

        // Act
        cuenta.depositar(new BigDecimal("100.00"));
        cuenta.depositar(new BigDecimal("50.00"));

        // Assert: esperado primero, real después
        assertEquals(new BigDecimal("150.00"), cuenta.getSaldo());
    }
}
