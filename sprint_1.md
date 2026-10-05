# Sprint 1 — App Navegable

## 1. Información General
* **Objetivo:** App navegable con login validado, menú principal y pantallas del restaurante (sin persistencia de datos).
* **Duración:** 30 minutos
* **Fechas:** Inicio: 05/10/2026 | Fin: 05/10/2026
* **Puntos Comprometidos:** 5 Pts
* **Historias de Usuario:** HU-01, HU-02, HU-03

---

## 2. Sprint Backlog

### HU-01 · Pantalla de inicio de sesión
* **Descripción:** Como mozo, quiero ingresar con usuario y contraseña, para que solo el personal autorizado use la app.
* **Prioridad:** Alta | **Puntos:** 2 | **Prototipo:** P1-01
* **Criterios de Aceptación:**
  * **CA1:** Dado que los campos están vacíos, cuando pulso «Ingresar», entonces se muestra un mensaje de error debajo de cada campo vacío.
  * **CA2:** Dado que escribo `admin / 1234`, cuando pulso «Ingresar», entonces se abre el menú principal y el login se cierra (atrás no regresa al login).
  * **CA3:** Dado que escribo credenciales incorrectas, cuando pulso «Ingresar», entonces aparece el Toast «Credenciales incorrectas».
  * **CA4:** Dado que escribo la contraseña, cuando la veo en pantalla, entonces aparece oculta y puedo mostrarla con el ícono de ojo.
* **Tareas Técnicas:**
  - [x] Crear el proyecto SaborApp (Empty Views Activity, Kotlin, API 26) con paquete `com.vasquez.saborapp`.
  - [x] Activar `viewBinding` en `build.gradle.kts` (Module :app).
  - [x] Diseñar `activity_login.xml` con `TextInputLayout` (usuario, contraseña con `password_toggle`) y botón.
  - [x] Programar la validación en `LoginActivity.kt` y declararla como LAUNCHER en `AndroidManifest.xml`.
* **Estado:** [x] Hecho

---

### HU-02 · Menú principal y navegación
* **Descripción:** Como mozo, quiero un menú con Platos, Mesas, Pedidos, Reportes, para llegar rápido a cada función de la app.
* **Prioridad:** Alta | **Puntos:** 2 | **Prototipo:** P1-02
* **Criterios de Aceptación:**
  * **CA1:** Dado que inicié sesión, cuando se abre el menú, entonces veo las opciones Platos, Mesas, Pedidos, Reportes y el botón «Salir».
  * **CA2:** Dado que estoy en el menú, cuando toco una opción, entonces se abre su pantalla y con «atrás» vuelvo al menú.
  * **CA3:** Dado que estoy en el menú, cuando toco «Salir», entonces regreso al login.
  * **CA4:** Dado que inicio sesión como MOZO, cuando veo el menú, entonces la opción Reportes no aparece (solo para ADMIN).
* **Tareas Técnicas:**
  - [x] Renombrar `MainActivity` a `MenuActivity` y crear una Activity por opción (`PlatosActivity`, `MesasActivity`, `PedidoActivity`, `ReportesActivity`).
  - [x] Diseñar `activity_menu.xml` con cuatro botones Material con ícono.
  - [x] Programar los Intents de navegación en `MenuActivity.kt`.
* **Estado:** [x] Hecho

---

### HU-03 · Identidad visual del negocio
* **Descripción:** Como dueño del negocio, quiero que la app tenga el nombre, colores e ícono de mi empresa, para que se vea profesional ante mis clientes y trabajadores.
* **Prioridad:** Media (Should) | **Puntos:** 1 | **Prototipo:** P1-02
* **Criterios de Aceptación:**
  * **CA1:** Dado que instalo la app, cuando veo el launcher, entonces aparece el nombre «SaborApp» con un ícono propio.
  * **CA2:** Dado que abro cualquier pantalla, cuando la observo, entonces usa la paleta cálida (naranja y rojo) definida en `colors.xml` / `themes.xml`.
  * **CA3:** Dado que reviso el código, cuando busco textos fijos en los layouts, entonces todos están en `strings.xml`.
* **Tareas Técnicas:**
  - [x] Definir la paleta cálida (naranja y rojo) en `colors.xml` y aplicarla en `themes.xml`.
  - [x] Crear el ícono con Image Asset (`res` → `New` → `Image Asset`).
  - [x] Pasar los textos de los layouts a `strings.xml`.
* **Estado:** [x] Hecho

---

## 3. Bitácora Daily
| Fecha-Hora | ¿Qué hice? | ¿Qué haré hoy? | ¿Qué me bloquea? |
| :--- | :--- | :--- | :--- |
| 05/10/2026 09:00 | Creación de layouts e interfaces de Login y Menú | Implementar validaciones e Intents | Ninguno |
| 05/10/2026 09:15 | Programación de LoginActivity y MenuActivity | Configurar navegación a Platos, Mesas, Pedidos, Reportes | Ninguno |

---

## 4. Sprint Review
| Historia | ¿Cumple la DoD? | Evidencia (captura / commit) | Visto bueno PO |
| :--- | :---: | :--- | :---: |
| HU-01 | [x] Sí [ ] No | LoginActivity con validaciones de campos y credenciales | [x] Sí |
| HU-02 | [x] Sí [ ] No | MenuActivity con navegación e inhabilitación de Reportes para MOZO | [x] Sí |
| HU-03 | [x] Sí [ ] No | Colores cálidos, strings.xml y launcher | [x] Sí |

---

## 5. Retrospectiva
* **¿Qué funcionó?:** La navegación fluida mediante Intents y el manejo de visibilidad según el rol del usuario (`ADMIN` vs `MOZO`).
* **¿Qué mejorar?:** Asegurar la persistencia de datos en la base de datos local SQLite para los próximos sprints.
* **Acción para el próximo sprint:** Implementar `DBHelper` y tablas SQLite para login real, catálogo de platos y mesas.
