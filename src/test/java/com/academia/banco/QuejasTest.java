package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Quejas de clientes (cada una reproducida con una prueba)")
class QuejasTest {

    private CuentaBancaria cuenta;

    @BeforeEach
    void prepararCuenta() {
        cuenta = new CuentaBancaria("Ana");
        cuenta.depositar(new BigDecimal("10000.00"));
    }

    @Test
    @DisplayName("Queja 1: el 3.er retiro no cobra comisión")
    void tercerRetiroGratis() {
        cuenta.retirar(new BigDecimal("100.00"));
        cuenta.retirar(new BigDecimal("100.00"));
        cuenta.retirar(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("9700.00"), cuenta.getSaldo());
    }

    @Test
    @DisplayName("Queja 2: se pueden retirar exactamente $5,000.00")
    void retiroExactoDelLimite() {
        cuenta.retirar(new BigDecimal("5000.00"));
        assertEquals(new BigDecimal("5000.00"), cuenta.getSaldo());
    }

    @Test
    @DisplayName("Queja 3: una transferencia rechazada no toca el saldo de origen")
    void transferenciaRechazadaNoCambiaElSaldo() {
        CuentaBancaria destino = new CuentaBancaria("Beto");
        assertThrows(SaldoInsuficienteException.class, () -> cuenta.transferir(new BigDecimal("10000.01"), destino));
        assertAll(
                () -> assertEquals(new BigDecimal("10000.00"), cuenta.getSaldo()),
                () -> assertEquals(new BigDecimal("0.00"), destino.getSaldo()));
    }

    @Test
    @DisplayName("Queja 4: la comisión aparece en el historial")
    void laComisionApareceEnElHistorial() {
        for (int i = 0; i < 4; i++) {
            cuenta.retirar(new BigDecimal("100.00"));
        }
        assertTrue(cuenta.getMovimientos().contains(new Movimiento(TipoMovimiento.COMISION, new BigDecimal("10.00"))));
    }

    @Test
    @DisplayName("Queja 5: nadie puede borrar movimientos desde fuera")
    void elHistorialNoSePuedeModificar() {
        assertThrows(UnsupportedOperationException.class, () -> cuenta.getMovimientos().clear());
    }
}
