# 🎮 **LoLApp — League of Legends Companion App**

> 🧠 Tu enciclopedia personal de League of Legends para Android  
> Consulta campeones, habilidades, objetos, runas y más.  
> Desarrollada 100% en **Kotlin** con arquitectura moderna y diseño limpio.

---

## 🏗️ **Resumen general**

**LoLApp** es una aplicación Android creada para ofrecer información actualizada de *League of Legends* utilizando la API oficial de **Riot Data Dragon**.  
Incluye una base de datos local para acceder **sin conexión**, interfaz moderna y navegación fluida entre secciones.

---

## 🧩 **Características principales**

✅ Catálogo completo de campeones con imágenes y títulos.  
✅ Fichas detalladas: historia, estadísticas, habilidades y skins.  
✅ Sección de objetos con sus descripciones y costos.  
✅ Runas actualizadas con nombres, iconos y efectos.  
✅ Descarga de skins directamente al dispositivo.  
✅ Cache y almacenamiento offline con Room.  
✅ Navegación rápida mediante **Navigation Drawer** y **RecyclerViews**.

---

## ⚙️ **Tecnologías utilizadas**

| Tecnología | Descripción |
|------------|-------------|
| 🧠 **Kotlin** | Lenguaje principal del proyecto |
| 🧩 **Android Jetpack** | ViewBinding, Room, RecyclerView |
| 🌐 **Retrofit + Gson** | Comunicación con API REST de Riot |
| 🖼️ **Picasso** | Carga y cache de imágenes |
| 🧵 **Coroutines** | Manejo de procesos asíncronos |
| 🛠️ **Material Design 3** | Interfaz moderna y adaptable |

**Badges:**
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-2E8B57?style=for-the-badge)
![Room](https://img.shields.io/badge/Room-1976D2?style=for-the-badge)
![License: MIT](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 🖥️ Guía de uso

📜 **Inicio:** muestra todos los campeones disponibles.  
🔍 **Búsqueda:** filtra campeones en tiempo real.  
👑 **Detalles:** accede a estadísticas, habilidades y skins del campeón seleccionado.  
🛒 **Objetos:** consulta precios, atributos y descripciones.  
🔮 **Runas:** visualiza efectos y estilos.  
⚙️ **Ajustes:** cambia tema o idioma de la aplicación.

---

## 💾 Base de datos local (Room)

| Entidad | Descripción |
|---------|-------------|
| `ChampionEntity` | Datos básicos de campeones |
| `ChampionDetailEntity` | Estadísticas completas |
| `ChampionSpellsEntity` | Habilidades del campeón |
| `SkinEntity` | Skins y recursos visuales |
| `ItemEntity` | Objetos con atributos y costos |
| `RuneEntity` | Runas y efectos |

---

## 📡 Integración con API (Riot Data Dragon)

La app obtiene datos actualizados directamente desde los endpoints de Riot:

| Tipo | Endpoint |
|------|----------|
| Campeones | `cdn/15.21.1/data/es_ES/champion.json` |
| Detalles | `cdn/15.21.1/data/es_ES/champion/{champion}.json` |
| Objetos | `cdn/15.21.1/data/es_ES/item.json` |
| Runas | `cdn/15.19.1/data/es_ES/runesReforged.json` |

---

## 🧱 Arquitectura

**Clean Architecture + MVVM-like**

- 🧩 **Repository:** capa central que decide entre API o base local.  
- 🔁 **Mappers:** transforman objetos entre capas.  
- 🗃️ **Room:** persistencia de datos y cache offline.  
- ⚡ **Coroutines:** asincronía sin bloquear la interfaz.  
- 🎨 **Adapters:** renderizado dinámico en listas con RecyclerView.

---

## 🛡️ Licencia

Este proyecto está bajo la licencia **MIT**.  
Puedes usar, modificar y distribuir el código libremente, siempre que se mantenga el crédito original.

---

## 🔗 Recursos y documentación

- [Riot Developer Portal](https://developer.riotgames.com/)  
- [Retrofit Documentation](https://square.github.io/retrofit/)  
- [Picasso](https://square.github.io/picasso/)  
- [Android Room](https://developer.android.com/training/data-storage/room)  
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)  

---

## 💬 Autor

👨‍💻 **Tu Nombre**  
📧 *mario.cavero2002@gmail.com*  
🌍 [GitHub](https://github.com/MarioCaveroPerez)


