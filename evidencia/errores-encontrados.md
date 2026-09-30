# Errores encontrados en la versión del compañero

Mi suite (MP-1 a MP-4) atrapó sola **3** de los 5 errores:
- `ComisionTest.calculaSegunElNumeroDeRetiro` (retiro 3)
- `RetiroTest.retirarExactamenteElLimiteSePermite`
- `TransferenciaTest.sinSaldoSuficienteNingunaDeLasDosCuentasCambia`

| Queja | Prueba que la reproduce | Qué estaba mal en el código (la línea y por qué) | ¿Mi suite ya lo atrapaba? |
|---|---|---|---|
| 1 | tercerRetiroGratis | En `comisionDelRetiro`, la condición era `numeroDeRetiro < RETIROS_GRATIS` en vez de `<= RETIROS_GRATIS`, cobrando comisión de $10.00 desde el 3.er retiro en vez del 4.º. | sí (`ComisionTest`) |
| 2 | retiroExactoDelLimite | En `retirar`, la validación usaba `m.compareTo(LIMITE_POR_RETIRO) >= 0` en lugar de `>`, rechazando indebidamente un retiro de exactamente $5,000.00. | sí (`RetiroTest`) |
| 3 | transferenciaRechazadaNoCambiaElSaldo | En `transferir`, se restaba `saldo = saldo.subtract(m);` antes de validar el saldo suficiente, modificando el saldo de la cuenta de origen a pesar de fallar. | sí (`TransferenciaTest`) |
| 4 | laComisionApareceEnElHistorial | En `retirar`, no se agregaba el movimiento `TipoMovimiento.COMISION` a la lista `movimientos` cuando se cobraba comisión. | no |
| 5 | elHistorialNoSePuedeModificar | En `getMovimientos()`, se retornaba directamente la lista mutable `movimientos` en lugar de una vista inmutable `List.copyOf(movimientos)`, permitiendo alterar el historial externamente. | no |
