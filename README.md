# Proyecto Kotlin

Este es un proyecto Android desarrollado en Kotlin que implementa Clean Architecture y el patrón MVVM, utilizando Hilt para la inyección de dependencias. La aplicación incluye funcionalidades de autenticación, manejo de tarjetas (cards) y edición de contenido, entre las que se destaca la captura y procesamiento de imágenes (conversión a Base64) para su integración en las cards.

## Características

- **Autenticación de usuarios:**  
  Implementación de login utilizando Firebase Authentication.

- **Gestión de tarjetas (Cards):**  
  - Agregar, editar y eliminar tarjetas.  
  - Cada tarjeta muestra información (marca, modelo) y una imagen capturada o seleccionada.
  
- **Captura y procesamiento de imágenes:**  
  - Captura de imagen mediante la cámara.  
  - Opción de seleccionar imagen desde la galería.  
  - Conversión de imagen a formato Base64 para almacenar y mostrar la imagen.

- **Arquitectura limpia (Clean Architecture):**  
  Separación de responsabilidades en capas: dominio, datos y presentación.

- **Inyección de dependencias con Hilt:**  
  Configuración centralizada de dependencias como repositorios y casos de uso.

## Arquitectura

El proyecto está estructurado siguiendo Clean Architecture y el patrón MVVM:

- **Dominio:**  
  - **Modelos:** Clases de datos (por ejemplo, `CardItem`).  
  - **Casos de uso:** Lógica de negocio (por ejemplo, `AddCardUseCase`, `UpdateCardUseCase`, `ConvertImageToBase64UseCase`).
  - **Repositorios (Interfaces):** Contratos para acceder a los datos.

- **Datos:**  
  - **Implementaciones de repositorios:** Clases concretas (por ejemplo, `CardRepositoryImpl`, `FirebaseAuthRepositoryImpl`) que cumplen con los contratos definidos en el dominio.

- **Presentación:**  
  - **ViewModels:** Se encargan de la lógica de presentación y comunicación con la capa de dominio (por ejemplo, `CardViewModel`).  
  - **Vistas:** Actividades, Fragments y diálogos (por ejemplo, `HomeFragment`, `EditCardDialogFragment`) que muestran la UI y delegan la lógica en los ViewModels.

- **Inyección de dependencias:**  
  Se usa Hilt para simplificar la provisión y administración de dependencias en todo el proyecto.

## Requisitos

- Android Studio Arctic Fox o superior.
- SDK de Android 31 (o superior).
- Kotlin 1.6 o superior.
- Dependencias de Hilt y otras librerías utilizadas en el proyecto (ver el archivo `build.gradle`).

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/PabloMartinezGonzalez-Linux/Proyecto_Kotlin.git
