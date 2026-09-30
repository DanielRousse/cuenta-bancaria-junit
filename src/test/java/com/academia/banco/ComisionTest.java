package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Comisión por retiro")
class ComisionTest {

    @ParameterizedTest(name = "retiro {0} → comisión ${1}")
    @CsvSource({
            "1,     0.00",
            "3,     0.00",
            "4,    10.00",
            "100,  10.00"
    })
    void calculaSegunElNumeroDeRetiro(int numero, BigDecimal esperada) {
        assertEquals(esperada, CuentaBancaria.comisionDelRetiro(numero));
    }

    @ParameterizedTest(name = "retiro {0} inválido se rechaza")
    @ValueSource(ints = {0, -1})
    void numerosDeRetiroInvalidosSeRechazan(int numero) {
        assertThrows(IllegalArgumentException.class, () -> CuentaBancaria.comisionDelRetiro(numero));
    }
}
