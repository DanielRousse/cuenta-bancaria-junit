package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cierre de cuenta (hecho con TDD)")
class CierreDeCuentaTest {

    private CuentaBancaria cuenta;

    @BeforeEach
    void prepararCuenta() {
        cuenta = new CuentaBancaria("Ana");
    }

    @Test
    @DisplayName("1. una cuenta nueva está abierta")
    void cuentaNuevaAbierta() {
        assertFalse(cuenta.estaCerrada());
    }

    @Test
    @DisplayName("2. una cuenta sin saldo se puede cerrar")
    void cerrarSinSaldo() {
        cuenta.cerrar();

        assertTrue(cuenta.estaCerrada());
    }

    @Test
    @DisplayName("3. una cuenta con saldo NO se puede cerrar")
    void cerrarConSaldoFalla() {
        cuenta.depositar(new BigDecimal("0.01"));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> cuenta.cerrar());
        assertAll(
                () -> assertEquals("No puedes cerrar una cuenta con saldo: $0.01", e.getMessage()),
                () -> assertFalse(cuenta.estaCerrada()));
    }

    @Test
    @DisplayName("4. en una cuenta cerrada no se puede depositar")
    void noSePuedeDepositarEnCuentaCerrada() {
        cuenta.cerrar();

        assertThrows(CuentaCerradaException.class, () -> cuenta.depositar(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("5. de una cuenta cerrada no se puede retirar")
    void noSePuedeRetirarDeCuentaCerrada() {
        cuenta.cerrar();

        assertThrows(CuentaCerradaException.class, () -> cuenta.retirar(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("6. no se puede transferir HACIA una cuenta cerrada, y el origen no cambia")
    void noSePuedeTransferirHaciaCuentaCerrada() {
        cuenta.cerrar();
        CuentaBancaria otra = new CuentaBancaria("Beto");
        otra.depositar(new BigDecimal("500.00"));

        assertThrows(CuentaCerradaException.class, () -> otra.transferir(new BigDecimal("100.00"), cuenta));
        assertEquals(new BigDecimal("500.00"), otra.getSaldo());
    }

    @Test
    @DisplayName("7. una cuenta cerrada no se puede volver a cerrar")
    void cuentaCerradaNoSePuedeVolverACerrar() {
        cuenta.cerrar();

        assertThrows(CuentaCerradaException.class, () -> cuenta.cerrar());
    }
}
