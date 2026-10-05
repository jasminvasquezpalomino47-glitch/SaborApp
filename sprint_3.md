# Sprint 3 — Mantenimiento de Platos y Gestión de Pedidos

## 1. Información General
* **Objetivo:** El mozo toma pedidos por mesa y cierra la cuenta con transacciones de base de datos; el admin edita, elimina y busca platos.
* **Duración:** 45 minutos
* **Fechas:** Inicio: 05/10/2026 | Fin: 05/10/2026
* **Puntos Comprometidos:** 16 Pts
* **Historias de Usuario:** HU-07, HU-08, HU-09

---

## 2. Sprint Backlog

### HU-07 · Editar, eliminar y buscar platos
* **Descripción:** Como administrador, quiero corregir, eliminar y buscar platos, para mantener la carta actualizada.
* **Prioridad:** Alta | **Puntos:** 3 | **Prototipo:** P1-03, P1-04
* **Criterios de Aceptación:**
  * **CA1:** Dado que toco un plato de la lista, cuando se abre el formulario, entonces muestra sus datos y el botón dice «Actualizar».
  * **CA2:** Dado que pulso «Eliminar», cuando confirmo en el diálogo, entonces el plato se borra; si tiene pedidos, aparece «No se puede eliminar: tiene pedidos».
  * **CA3:** Dado que escribo en el buscador, cuando cambia el texto, entonces la lista se filtra por nombre (`LIKE`).
  * **CA4:** Dado que marco un plato como no disponible, cuando tomo un pedido, entonces ese plato no aparece para elegir.
* **Tareas Técnicas:**
  - [x] Agregar `obtener()`, `actualizar()`, `eliminar()` y `listar(filtro)` en `PlatoDao`.
  - [x] Modo edición en `PlatoFormActivity` con `putExtra("id")`.
  - [x] `AlertDialog` de confirmación y captura de `SQLiteConstraintException`.
  - [x] `doAfterTextChanged` en el buscador.
* **Estado:** [x] Hecho

---

### HU-08 · Tomar pedido por mesa
* **Descripción:** Como mozo, quiero elegir una mesa y agregarle platos con su cantidad, para registrar el pedido sin papel.
* **Prioridad:** Alta | **Puntos:** 8 | **Prototipo:** P1-05
* **Criterios de Aceptación:**
  * **CA1:** Dado que abro Pedidos, cuando veo la grilla de mesas, entonces las `OCUPADAS` se distinguen por color de las `LIBRES`.
  * **CA2:** Dado que toco una mesa `LIBRE` y agrego el primer plato, cuando guardo, entonces se crea el pedido `ABIERTO` y la mesa pasa a `OCUPADA`.
  * **CA3:** Dado que agrego un plato con cantidad mayor a 0, cuando lo confirmo, entonces aparece con su subtotal y el total se recalcula.
  * **CA4:** Dado que se guarda el pedido, cuando ocurre un error a mitad, entonces no queda nada guardado (transacción).
* **Tareas Técnicas:**
  - [x] Migrar `DBHelper` a `DB_VERSION = 2` con tablas `pedido` y `detalle_pedido` (`onUpgrade`).
  - [x] Crear `PedidoDao.agregarPlato()` con `beginTransaction` / `setTransactionSuccessful` / `endTransaction`.
  - [x] `PedidoActivity`: grilla de mesas + Spinner de platos + cantidad + lista del pedido.
  - [x] Guardar `precio_unit` al momento del pedido.
* **Estado:** [x] Hecho

---

### HU-09 · Cerrar la cuenta de una mesa
* **Descripción:** Como mozo, quiero cerrar la cuenta de una mesa, para cobrar al cliente y liberar la mesa.
* **Prioridad:** Alta | **Puntos:** 5 | **Prototipo:** P1-06
* **Criterios de Aceptación:**
  * **CA1:** Dado que la mesa tiene un pedido `ABIERTO`, cuando toco «Ver cuenta», entonces veo cada plato, su cantidad, subtotal y el total.
  * **CA2:** Dado que confirmo «Cerrar cuenta», cuando termina, entonces el pedido pasa a `CERRADO`, se guarda el total y la mesa vuelve a `LIBRE` en una sola transacción.
  * **CA3:** Dado que la mesa está `LIBRE`, cuando intento cerrar cuenta, entonces el botón está deshabilitado.
* **Tareas Técnicas:**
  - [x] Consulta `JOIN` entre `detalle_pedido` + `plato` para la cuenta.
  - [x] `PedidoDao.cerrarCuenta(idPedido)` con transacción.
  - [x] `CuentaActivity` con lista, total y botón Cerrar cuenta.
* **Estado:** [x] Hecho

---

## 3. Bitácora Daily
| Fecha | ¿Qué hice? | ¿Qué haré hoy? | ¿Qué me bloquea? |
| :--- | :--- | :--- | :--- |
| 05/10/2026 11:00 | Migración de DBHelper a DB_VERSION = 2 con tablas `pedido` y `detalle_pedido` | Implementar `PedidoDao` con transacciones SQLite | Ninguno |
| 05/10/2026 11:30 | Creación de `PedidoActivity` y `CuentaActivity` | Probar cierre de cuenta e inhabilitación para mesas libres | Ninguno |

---

## 4. Sprint Review
| Historia | ¿Cumple la DoD? | Evidencia (captura / commit) | Visto bueno PO |
| :--- | :---: | :--- | :---: |
| HU-07 | [x] Sí [ ] No | Búsqueda `LIKE` en vivo, edición y eliminación de platos con validación de restricción | [x] Sí |
| HU-08 | [x] Sí [ ] No | Registro de pedidos por mesa con transacciones atómicas SQLite | [x] Sí |
| HU-09 | [x] Sí [ ] No | Vista de cuenta detallada y cierre de mesa pasando estado a LIBRE | [x] Sí |

---

## 5. Retrospectiva
* **¿Qué funcionó?:** El uso de transacciones atómicas (`beginTransaction` y `setTransactionSuccessful`) evitó inconsistencias en la base de datos durante el registro de detalles de pedido.
* **¿Qué mejorar?:** Agregar exportación o reporte de ventas acumuladas por fecha para el dueño del negocio.
* **Acción para el próximo sprint:** Implementar `ReporteDao` en el Sprint 4 con indicadores de ventas del día, top de platos más vendidos e integración con WhatsApp para compartir cuentas.
