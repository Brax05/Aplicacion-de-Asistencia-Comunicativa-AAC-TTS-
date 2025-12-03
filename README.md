# Namisok - Aplicación de Asistencia Comunicativa (AAC/TTS)

> **"Más allá del código y la tecnología, este proyecto busca dar voz a quienes muchas veces no pueden expresarse con facilidad."**

---

## Resumen del Proyecto

**Namisok** es una aplicación móvil diseñada para facilitar la comunicación de personas con dificultades de habla o expresión (afasia, ELA, autismo no verbal, situaciones post-operatorias, entre otras).

Combina un sistema robusto de **Texto-a-Voz (TTS)** con un **tablero de comunicación aumentativa (AAC)** y un módulo de **frases rápidas personalizables**.  
La aplicación está pensada para ser:

- rápida  
- intuitiva  
- completamente **offline**

---

##  Objetivos y Público Objetivo

###  Público Objetivo
Personas con dificultades de comunicación verbal, temporales o permanentes, que necesitan una herramienta ágil para expresar sus ideas, necesidades y emociones.

###  Objetivos Clave
- **Principal:** Proveer una salida de voz clara y rápida.  
- **Accesibilidad:** Interfaz intuitiva basada en símbolos y texto.  
- **Personalización:** Adaptación completa de frases, íconos y opciones de voz.  
- **Independencia:** Funcionalidad **100% offline** con base de datos local.

---

##  Funcionalidades Principales

### **F-01: Tablero de Comunicación (AAC)**
Función ideal para quienes no pueden escribir en el momento.

- **Cuadrícula Visual:** Categorías como *Comida*, *Sentimientos*, *Ayuda*, etc.
- **Interacción Táctil:** Tocar un pictograma reproduce inmediatamente su audio.
- **Gestión Local:** Imágenes, textos y categorías almacenadas en el dispositivo.

---

### **F-02: Frases Rápidas (Favoritos)**
Para expresiones frecuentes o urgentes.

- **Lista Personalizada:** Ejemplo: “Hola, mi nombre es…”.
- **CRUD Completo:** Crear, leer, editar y eliminar frases.
- **Acceso Rápido:** Un toque = reproducción de audio.

---

### **F-03: Texto a Voz Directo (TTS)**
Para comunicación espontánea.

- **Campo de texto grande** para escribir mensajes.
- **Botón “Hablar”** con lectura inmediata mediante TTS nativo.

---

### **F-04: Ajustes de Voz**
Pensado para la identidad del usuario.

- Control de **velocidad**, **tono (pitch)** y **volumen**.
- Opciones de **voz masculina / femenina** dependiendo del dispositivo.

---

## Stack Tecnológico

El proyecto está construido para Android usando Flutter.

| **Componente**            | **Tecnología / Paquete** | **Descripción** |
|---------------------------|---------------------------|------------------|
| IDE                       | Android Studio            | Desarrollo, compilación y depuración |
| Framework                 | Flutter                   | SDK UI multiplataforma |
| Lenguaje                  | Dart                      | Lenguaje principal del proyecto |
| TTS                       | flutter_tts               | Motor de síntesis de voz (Android nativo) |
| Plataforma                | Android                    | Plataforma objetivo |

---

##  Instalación y Ejecución

Asegúrate de tener **Flutter** y **Android Studio** instalados.

### 1️ Clonar el repositorio
git clone ----------
cd namisok


Conclusión

Namisok nace desde la empatía y la necesidad humana fundamental de comunicarse.
Cada frase, cada pictograma y cada palabra pronunciada representa una oportunidad para:

pedir ayuda

expresar sentimientos

conectar con otros

La tecnología, cuando se diseña con accesibilidad y humanidad, puede transformar vidas.
