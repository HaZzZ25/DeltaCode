# DeltaCode - FarmaPlus

Proyecto Final. Sistema de Punto de Venta e Inventario para una Farmacia FarmaPlus.

> **Nota importante:** El código fuente del sistema se encuentra en la rama `master`.

## Características Principales

* **Punto de Venta (POS):** Búsqueda de productos por nombre o SKU, gestión dinámica del carrito de compras y cálculo automático del total aplicando descuentos.
* **Gestión de Inventario:** Módulo para registrar, actualizar y eliminar productos. Control de stock, precios, marcas y carga de imágenes.
* **Clientes y Membresías:** Registro de clientes y asignación de niveles de membresía (Bronce, Plata, Oro) que otorgan descuentos automáticos (5%, 10% y 15%).
* **Reportes (Dashboard):** Panel interactivo con el total de ventas del día, conteo de clientes fidelizados y una gráfica de barras con los ingresos por hora.
* **Control de Accesos:** Sistema de Login con autenticación de usuarios y restricción de módulos según el rol (Ej. Vendedor no tiene acceso a Reportes ni Inventario).

##  Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Interfaz Gráfica:** JavaFX 21 (FXML + CSS)
* **Base de Datos:** MariaDB
* **Gestor de Dependencias:** Maven
* **Iconografía:** FontAwesomeFX 8.2

##  Requisitos y Configuración

Para poder ejecutar el proyecto localmente, necesitas tener instalado:
* **JDK 21**
* **Apache Maven**
* **MariaDB** (Corriendo en el puerto local `3306`)

### Credenciales de Base de Datos
El sistema está configurado para conectarse a una base de datos local con los siguientes datos:
* **Base de datos:** `farmaplus_db`
* **Usuario:** `admin`
* **Contraseña:** `admin123`

*(Revisa la rama `master` para ver el código completo y los scripts de la base de datos).*
