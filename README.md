# Cuenta bancaria — pruebas unitarias con JUnit 5

**Autor:** Jonathan Daniel Reyes Gordillo

## Cómo correr las pruebas

```bash
./probar.sh final
```

## Qué prueba cada clase

| Clase de prueba | Qué prueba | Cuántas pruebas |
|---|---|---|
| DepositoTest | Depósitos en cuenta nueva y acumulación de saldo | 2 |
| RetiroTest | Reglas de retiro: resta de saldo, montos inválidos/cero, saldo insuficiente con verificación atómica `assertAll`, valores frontera ($5,000.00 y $5,000.01) y rechazo de fracciones de centavo | 8 |
| TransferenciaTest | Transferencias exitosas (saldo de origen y destino, sin cobro de comisión ni conteo como retiro, registro en historial) y transferencias rechazadas (saldo insuficiente, a la misma cuenta, cuenta nula, monto cero) con `@BeforeEach` y clases `@Nested` | 7 |
| InteresTest | Cálculo de interés mensual según tabla por tramos de saldo ($0.00, tramo 0.5%, tramo 1%, valores frontera de $999.99 y $49,999.99 con redondeo `HALF_UP`) y rechazo de saldo negativo con `@ParameterizedTest` y `@CsvSource` | 8 |
| ComisionTest | Cobro de comisión por retiro según su número de operación (primeros 3 gratis, a partir del 4.º cobran $10.00) y rechazo de números de retiro inválidos (0 y negativos) con `@CsvSource` y `@ValueSource` | 6 |
| QuejasTest | Reproducción y resolución de las 5 quejas de clientes: 3.er retiro gratis, retiro por el límite exacto de $5,000.00, transferencia fallida no altera saldo de origen, registro del movimiento de comisión en el historial e inmutabilidad del historial frente a modificaciones externas | 5 |
| CierreDeCuentaTest | Regla 8 desarrollada mediante TDD (ciclos Rojo-Verde-Limpiar): cuenta nueva abierta, cierre exitoso con saldo cero, rechazo de cierre con saldo positivo, cuenta cerrada no permite depósitos, retiros ni recibir transferencias, e imposibilidad de cerrar una cuenta ya cerrada | 7 |

## Boleto de salida

1. **¿Por qué una prueba que nunca viste en rojo no es confiable? Da un ejemplo de hoy.**
   Porque una prueba que nunca ha fallado podría estar evaluando condiciones triviales o vacías, teniendo aserciones incorrectas, o estar estructurada de tal manera que pase sin importar si el código bajo prueba realmente funciona (un «verde mentiroso»). Si no comprobamos primero que la prueba falla cuando el comportamiento esperado no se cumple o cuando el código tiene un defecto, no podemos tener certeza de que realmente vigile la funcionalidad. Un ejemplo claro ocurrió en la **MP-1**: la prueba `depositarSumaAlSaldo` falló inicialmente en rojo porque el método `depositar` asignaba el monto en lugar de sumarlo (`saldo = monto`); al corregir la implementación a `saldo = saldo.add(m)` y verla pasar a verde, tuvimos la certeza absoluta de que la prueba sí verificaba la acumulación del saldo. De igual forma, en la **MP-5** primero reprodujimos las 5 quejas en rojo con el código defectuoso antes de aplicar los arreglos.

2. **¿Qué error se escondía en un valor frontera, y qué prueba lo atrapó?**
   En la Regla 3 ("Un retiro no puede pasar de $5,000.00 por operación"), el código del compañero introdujo el error usando `if (m.compareTo(LIMITE_POR_RETIRO) >= 0)` con `>=`, lo que provocaba que un retiro de exactamente $5,000.00 fuera rechazado con `LimiteExcedidoException` a pesar de estar permitido. Este error en el valor frontera exacto fue atrapado por `RetiroTest.retirarExactamenteElLimiteSePermite` y reproducido en `QuejasTest.retiroExactoDelLimite`. Otro error de frontera similar ocurrió en el cobro de comisión: el código usaba `numeroDeRetiro < RETIROS_GRATIS` en vez de `<=`, cobrando comisión en el 3.er retiro (atrapado por `ComisionTest` en el caso `retiro 3` y `QuejasTest.tercerRetiroGratis`).

3. **¿Qué cambió en tu forma de programar al escribir la prueba ANTES del código (MP-6)?**
   Cambió radicalmente la perspectiva: en vez de diseñar la implementación interna imaginando cómo debería funcionar para luego ver cómo probarla, en TDD comienzas pensando como cliente/consumidor de la clase. Diseñas la interfaz pública, los nombres de métodos, los tipos de parámetros y las excepciones directamente en la prueba. Al verla fallar (incluso desde el error de compilación), te enfocas en escribir estrictamente el código mínimo necesario para ponerla en verde, sin sobreingeniería. Posteriormente, la fase de refactorización («Limpiar») te permite extraer lógica común (como `validarAbierta()`) con total seguridad de que nada se rompió, sabiendo que la suite completa respalda cada cambio.
