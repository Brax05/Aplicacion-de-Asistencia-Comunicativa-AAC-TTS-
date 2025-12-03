Namisok - Aplicación de Asistencia Comunicativa (AAC/TTS)

"Más allá del código y la tecnología, este proyecto busca dar voz a quienes muchas veces no pueden expresarse con facilidad."

Resumen del Proyecto

Namisok es una aplicación móvil diseñada para facilitar la interacción con el entorno a personas con dificultades de habla o comunicación (como afasia, ELA, autismo no verbal o situaciones post-operatorias).

La aplicación combina un sistema robusto de Texto-a-Voz (TTS) con un tablero de comunicación aumentativa y una función de frases rápidas personalizables. Su diseño se centra en la rapidez, la intuición y la capacidad de funcionar completamente offline.

Objetivos y Público Objetivo

Público Objetivo

Personas con dificultades de comunicación verbal, ya sea temporal o permanente, que requieren una herramienta ágil para expresar sus necesidades, pensamientos y sentimientos.

Objetivos Clave

Principal: Proveer una salida de voz clara y rápida.

Accesibilidad: Interfaz intuitiva basada en símbolos y texto.

Personalización: Permitir al usuario adaptar las frases y voces a su identidad.

Independencia: Funcionalidad 100% offline mediante base de datos local.

Funcionalidades Principales

F-01: Tablero de Comunicación (AAC)

Ideal para usuarios que no pueden o no desean escribir en el momento.

Cuadrícula Visual: Categorías claras (ej. "Comida", "Sentimientos", "Ayuda").

Interacción Táctil: Al tocar un pictograma (Imagen + Texto), la app pronuncia la palabra inmediatamente.

Gestión: Los iconos y etiquetas se gestionan localmente.

F-02: Frases Rápidas (Favoritos)

Para necesidades recurrentes y expresiones cotidianas.

Lista Personalizada: Guardar frases como "Hola, mi nombre es..." o "¿Puedes ayudarme?".

CRUD Completo: Crear, Leer, Actualizar y Borrar frases.

Acceso Rápido: Reproducción de audio con un solo toque.

F-03: Texto a Voz Directo (TTS)

Para comunicación espontánea y específica.

Campo de Texto: Área grande para escribir mensajes nuevos.

Botón "Hablar": Lectura inmediata del texto ingresado utilizando el motor TTS nativo.

F-04: Ajustes de Voz

Adaptabilidad a las preferencias del usuario.

Configuración: Control de velocidad, tono (pitch) y volumen.

Género: Selección de voces masculinas o femeninas (según disponibilidad del dispositivo).

Stack Tecnológico

El proyecto está construido priorizando el rendimiento y la estabilidad en dispositivos Android, utilizando Android Studio como entorno de desarrollo.

Componente

Tecnología / Paquete

Descripción

Entorno de Desarrollo (IDE)

Android Studio

IDE principal utilizado para el desarrollo, compilación y depuración.

Framework

Flutter

SDK de UI para construir la aplicación nativa.

Lenguaje

Dart

Lenguaje de programación optimizado para UI.


flutter_tts

Motor de síntesis de voz utilizando servicios nativos de Android.

Plataforma

Android

Sistema operativo objetivo principal.

Instalación y Ejecución

Para correr este proyecto localmente, asegúrate de tener instalado el SDK de Flutter y Android Studio configurado.

Clonar el repositorio:

git clone [https://github.com/tu-usuario/namisok.git](https://github.com/tu-usuario/namisok.git)
cd namisok



Instalar dependencias:

flutter pub get



Ejecutar en un dispositivo o emulador:

flutter run



Nota: Asegúrate de que el dispositivo Android tenga un motor TTS instalado (como Google TTS) para que la voz funcione correctamente.

Estructura del Proyecto (a corregir )

lib/
├── main.dart           # Punto de entrada
├── models/             # Modelos de datos (Frase, Pictograma)
├── screens/            # Pantallas (Home, TTS, Favoritos, Config)
├── services/           # Lógica de DB (SQLite) y TTS
├── widgets/            # Componentes reutilizables UI
└── utils/              # Constantes y temas



Conclusión

Namisok nace de una necesidad humana: la de comunicarse. Cada frase rápida y cada palabra generada por la app representa una oportunidad para conectar, pedir ayuda o simplemente decir "gracias".

Este proyecto demuestra que la tecnología, cuando se diseña desde la empatía y la accesibilidad, puede transformar profundamente la forma en que las personas se comunican con el mundo.

Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE.md para más detalles.
