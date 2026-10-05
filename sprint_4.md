# Sprint 4 — Reportes, Integración y Generación de APK

## 1. Información General
* **Objetivo:** El dueño ve reportes de ventas, se comparte la cuenta por WhatsApp, la sesión se recuerda localmente y la app se exporta como APK ejecutable.
* **Duración:** 45 minutos
* **Fechas:** Inicio: 05/10/2026 | Fin: 05/10/2026
* **Puntos Comprometidos:** 10 Pts
* **Historias de Usuario:** HU-10, HU-11, HU-12

---

## 2. Sprint Backlog

### HU-10 · Reportes de ventas
* **Descripción:** Como dueño, quiero ver la venta del día, los platos más pedidos y la venta por mesa, para decidir qué preparar y qué promocionar.
* **Prioridad:** Alta | **Puntos:** 5 | **Prototipo:** P1-07
* **Criterios de Aceptación:**
  * **CA1:** Dado que hay pedidos `CERRADOS` hoy, cuando abro Reportes, entonces veo la suma del día (`SUM` con `date('now','localtime')`).
  * **CA2:** Dado que hay ventas, cuando veo el top 5, entonces los platos están ordenados por cantidad vendida (`SUM` + `GROUP BY`).
  * **CA3:** Dado que no hay ventas hoy, cuando abro Reportes, entonces aparece «Sin ventas hoy».
* **Tareas Técnicas:**
  - [x] Crear `ReporteDao` con `ventaDelDia()`, `topPlatos()` y `ventaPorMesa()`.
  - [x] `ReportesActivity` con indicadores y lista.
  - [x] Probar las consultas en Database Inspector.
* **Estado:** [x] Hecho

---

### HU-11 · Compartir la cuenta por WhatsApp
* **Descripción:** Como mozo, quiero enviar la cuenta al celular del cliente, para que tenga el detalle de su consumo.
* **Prioridad:** Media | **Puntos:** 2 | **Prototipo:** P1-06
* **Criterios de Aceptación:**
  * **CA1:** Dado que estoy en la cuenta, cuando toco «Compartir», entonces se arma un texto con la mesa, cada plato, cantidad y total.
  * **CA2:** Dado que toco «Compartir», cuando se abre el selector, entonces puedo elegir WhatsApp u otra app (`Intent.createChooser`).
  * **CA3:** Dado que el celular no tiene WhatsApp, cuando comparto, entonces se muestran las demás apps sin que la app se cierre.
* **Tareas Técnicas:**
  - [x] Construir el texto de la cuenta con `String.format`.
  - [x] `Intent` `ACTION_SEND` tipo `text/plain` con `createChooser`.
* **Estado:** [x] Hecho

---

### HU-12 · Sesión recordada y APK instalable
* **Descripción:** Como mozo, quiero que la app recuerde mi sesión y se pueda instalar en el celular del negocio, para no iniciar sesión cada vez y usarla en el trabajo diario.
* **Prioridad:** Media | **Puntos:** 3 | **Prototipo:** P1-02
* **Criterios de Aceptación:**
  * **CA1:** Dado que inicié sesión, cuando cierro y vuelvo a abrir la app, entonces entra directo al menú con mi nombre.
  * **CA2:** Dado que estoy en el menú, cuando toco «Salir», entonces se borra la sesión y la app vuelve a pedir login.
  * **CA3:** Dado que genero el APK firmado (release), cuando lo instalo en un celular Android 7 o superior, entonces abre y funciona sin Android Studio.
* **Tareas Técnicas:**
  - [x] Guardar el usuario en `SharedPreferences` al iniciar sesión y verificarlo al abrir `LoginActivity`.
  - [x] Borrar `SharedPreferences` al presionar «Salir».
  - [x] `Build` → `Generate Signed App Bundle or APK` → `APK release` con keystore propio.
  - [x] Commit «Sprint 4: reportes, sesión y APK» y tag `v1.0`.
* **Estado:** [x] Hecho

---

## 3. Bitácora Daily
| Fecha | ¿Qué hice? | ¿Qué haré hoy? | ¿Qué me bloquea? |
| :--- | :--- | :--- | :--- |
| 05/10/2026 12:00 | Creación de `ReporteDao` e implementación de indicadores de venta diaria y Top 5 | Integrar `Intent.ACTION_SEND` para compartir cuentas por WhatsApp | Ninguno |
| 05/10/2026 12:30 | Configuración de `SharedPreferences` para persistencia de sesión | Generar APK release y verificar flujo completo | Ninguno |

---

## 4. Sprint Review
| Historia | ¿Cumple la DoD? | Evidencia (captura / commit) | Visto bueno PO |
| :--- | :---: | :--- | :---: |
| HU-10 | [x] Sí [ ] No | Reportes de ventas agregados con filtro por fecha y top de platos | [x] Sí |
| HU-11 | [x] Sí [ ] No | Integración con WhatsApp vía Intent chooser con formato de boleta | [x] Sí |
| HU-12 | [x] Sí [ ] No | Sesión recordada en SharedPreferences y APK ejecutable funcional | [x] Sí |

---

## 5. Retrospectiva
* **¿Qué funcionó?:** El flujo completo de SaborApp desde el login, gestión de catálogo, toma de pedidos, cierre de cuenta, reportes y envío por WhatsApp se completó satisfactoriamente.
* **¿Qué mejorar?:** Para futuras versiones se puede agregar sincronización en la nube y pasarela de pagos.
* **Acción para el próximo sprint:** Producto entregado con éxito (versión v1.0).
