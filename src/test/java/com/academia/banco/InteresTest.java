package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Interés mensual")
class InteresTest {

    @ParameterizedTest(name = "saldo ${0} → interés ${1}")
    @CsvSource({
            "0.00,          0.00",
            "1000.00,       5.00",
            "50000.00,    500.00",
            "999.99,        0.00",
            "1234.56,       6.17",
            "49999.99,    250.00",
            "123456.78,  1234.57"
    })
    void calculaSegunLaTabla(BigDecimal saldo, BigDecimal esperado) {
        assertEquals(esperado, Interes.mensual(saldo));
    }

    @Test
    @DisplayName("un saldo negativo es un error")
    void unSaldoNegativoEsUnError() {
        assertThrows(IllegalArgumentException.class, () -> Interes.mensual(new BigDecimal("-0.01")));
    }
}
