# Sprint 2 — Base de Datos y Catálogos (SQLite)

## 1. Información General
* **Objetivo:** Platos y mesas se registran y listan desde SQLite; autenticación de login real contra la base de datos.
* **Duración:** 60 minutos
* **Fechas:** Inicio: 05/10/2026 | Fin: 05/10/2026
* **Puntos Comprometidos:** 11 Pts
* **Historias de Usuario:** HU-04, HU-05, HU-06

---

## 2. Sprint Backlog

### HU-04 · Base de datos y login con SQLite
* **Descripción:** Como administrador, quiero que los usuarios se guarden y validen en la base de datos del celular, para no depender de credenciales escritas en el código.
* **Prioridad:** Alta | **Puntos:** 3 | **Prototipo:** P1-01
* **Criterios de Aceptación:**
  * **CA1:** Dado que instalo la app por primera vez, cuando se abre, entonces se crea `saborapp.db` con la tabla `usuario` y el usuario `admin / 1234` (rol ADMIN).
  * **CA2:** Dado que ingreso credenciales, cuando pulso «Ingresar», entonces se validan con una consulta parametrizada (`rawQuery` con `?`).
  * **CA3:** Dado que el login es correcto, cuando se abre el menú, entonces muestra el nombre y rol del usuario.
  * **CA4:** Dado que abro App Inspection → Database Inspector, cuando selecciono la BD, entonces veo la tabla `usuario` con sus registros.
* **Tareas Técnicas:**
  - [x] Crear `data/DBHelper.kt` (`SQLiteOpenHelper`) con `DB_NAME = "saborapp.db"` y `DB_VERSION = 1`.
  - [x] Crear la tabla `usuario` e insertar el admin en `onCreate`; activar `FOREIGN KEY` en `onConfigure`.
  - [x] Agregar `validarUsuario(usuario, clave)` y usarla en `LoginActivity`.
  - [x] Verificar la BD en Database Inspector.
* **Estado:** [x] Hecho

---

### HU-05 · Registrar y listar platos
* **Descripción:** Como administrador, quiero registrar platos con nombre, categoría, precio y disponibilidad, para tener la carta del restaurante en la app.
* **Prioridad:** Alta | **Puntos:** 5 | **Prototipo:** P1-03, P1-04
* **Criterios de Aceptación:**
  * **CA1:** Dado que el nombre o el precio están vacíos, cuando pulso «Guardar», entonces se marca el error y no se guarda.
  * **CA2:** Dado que escribo un precio menor o igual a 0, cuando pulso «Guardar», entonces aparece «Precio inválido».
  * **CA3:** Dado que elijo la categoría en un Spinner (Entradas, Fondos, Bebidas, Postres), cuando guardo, entonces el plato aparece en la lista ordenado por categoría y nombre.
  * **CA4:** Dado que cierro y abro la app, cuando entro a Platos, entonces los platos registrados siguen ahí.
* **Tareas Técnicas:**
  - [x] Crear `Plato.kt` (data class) y la tabla `plato`.
  - [x] Crear `PlatoDao` con `insertar()` y `listar()`.
  - [x] Diseñar `item_plato.xml` y `PlatoAdapter` (RecyclerView).
  - [x] Crear `PlatoFormActivity` con validaciones y Spinner de categorías.
* **Estado:** [x] Hecho

---

### HU-06 · Registrar y listar mesas
* **Descripción:** Como administrador, quiero registrar las mesas con su número y capacidad, para asignar los pedidos a cada mesa.
* **Prioridad:** Alta | **Puntos:** 3 | **Prototipo:** P1-05
* **Criterios de Aceptación:**
  * **CA1:** Dado que registro un número de mesa que ya existe, cuando pulso «Guardar», entonces aparece «La mesa ya existe».
  * **CA2:** Dado que la capacidad no está entre 1 y 12, cuando guardo, entonces se muestra «Capacidad inválida».
  * **CA3:** Dado que registro una mesa nueva, cuando vuelvo a la lista, entonces aparece con estado `LIBRE`.
* **Tareas Técnicas:**
  - [x] Crear la tabla `mesa` con `numero UNIQUE`.
  - [x] Crear `MesaDao` (`insertar`, `listar`) y capturar `SQLiteConstraintException`.
  - [x] Lista de mesas con RecyclerView en `GridLayoutManager` de 3 columnas.
* **Estado:** [x] Hecho

---

## 3. Bitácora Daily
| Fecha | ¿Qué hice? | ¿Qué haré hoy? | ¿Qué me bloquea? |
| :--- | :--- | :--- | :--- |
| 05/10/2026 10:00 | Creación de `DBHelper.kt` y tabla `usuario` | Implementar `PlatoDao` y `MesaDao` | Ninguno |
| 05/10/2026 10:30 | Creación de catálogo de Platos y Mesas en SQLite | Probar persistencia de datos y validaciones de errores | Ninguno |

---

## 4. Sprint Review
| Historia | ¿Cumple la DoD? | Evidencia (captura / commit) | Visto bueno PO |
| :--- | :---: | :--- | :---: |
| HU-04 | [x] Sí [ ] No | Login autenticado contra SQLite `saborapp.db` | [x] Sí |
| HU-05 | [x] Sí [ ] No | Formulario de Platos con Spinner y persistencia en BD | [x] Sí |
| HU-06 | [x] Sí [ ] No | Grilla de Mesas de 3 columnas con validación de capacidad y unicidad | [x] Sí |

---

## 5. Retrospectiva
* **¿Qué funcionó?:** La arquitectura SQLiteOpenHelper + DAO permitió aislar la lógica de acceso a datos de la interfaz de usuario.
* **¿Qué mejorar?:** Manejar transacciones para operaciones maestro-detalle en el siguiente sprint.
* **Acción para el próximo sprint:** Migrar la base de datos a `DB_VERSION = 2` agregando las tablas de `pedido` y `detalle_pedido` con transacciones para tomar pedidos y cerrar cuentas.
