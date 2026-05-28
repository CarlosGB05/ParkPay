```markdown
# 🅿 ParkPay - Reserva de Estacionamiento en Parkings

Aplicación Android para la gestión y reserva de estacionamientos en Parking.
Esta aplicación nos permite registrarnos, consultar los Parkings disponibles en un ratio indicado y
realizar reservas de las mismas.

## 📱 Pantallas de la aplicación

* **Arranque** → Pantalla de inicio
* **Login** → Inicio Sesión con correo y contraseña
* **Registro** → Creación de nuevo usuario
* **Menú** → Menú con varias opciones
* **Parking** → Indicar un lugar y ratio para buscar los Parkings y poder reservarlos
* **Mis Reservas** → Listado de todas las reservas realizadas del usuario
* **Usuario** → Ver toda la información del usuario

## 🅿 Precio/Hora por Reservas (€/h)

| Calificación | Precio/hora |
| :--- | :--- |
| 0 - 1.9 | 1.5 € |
| 2 - 3.9 | 3.5 € |
| 4 - 4.4 | 4.5 € |
| 4.5 - 5 | 6 € |

## 📁 Estructura del proyecto

```text
app/src/main/java/
└── com.example.parkpay/
    ├── Adapter_Parking.java
    ├── Adapter_Reservas.java
    ├── Buscar_Parking.java
    ├── Editar_Usuario.java
    ├── Finalizar_Reserva.java
    ├── Info_Parking.java
    ├── Info_Reserva.java
    ├── Info_Usuario.java
    ├── Lista_Parking.java
    ├── Lista_Reservas.java
    ├── Login.java
    ├── Inicio.java
    ├── Menu_Inicial.java
    ├── Pagar_Parking.java
    ├── Registro.java
    └── Reservar_Calendario.java
    ├── models/
    │   ├── Usuario.java
    │   ├── Parking.java
    │   └── Reserva.java
    ├── dao/
    │   ├── UsuarioDAO.java
    │   └── ReservaDAO.java
    └── conexionBBDD/
        └── ConexionBBDD.java

## 🛠️ Tecnologías utilizadas

* **Java** → Lenguaje principal
* **Android Studio** → Entorno de desarrollo
* **PostgreSQL** → Base de datos
* **JDBC (Driver PostgreSQL)** → Conexión directa a la base de datos
