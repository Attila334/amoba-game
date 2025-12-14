# Amőba (Gomoku) Játék - Progtech Beadandó

Ez a projekt egy Java nyelven írt, konzolos felületű Amőba (Gomoku) játék implementációja. A projekt a Programozási Technológiák tárgy követelményei alapján készült, Maven build rendszert, egységteszteket és adatbázis alapú pontszámmentést használ.

## 🚀 Funkciók

- **Játékmenet:** Ember (X) a Számítógép (O) ellen.
- **Dinamikus pálya:** NxM méretű tábla kezelése (alapértelmezetten 10x10).
- **Mesterséges Intelligencia:** A gép véletlenszerű, érvényes lépéseket tesz.
- **Perzisztencia:** A nyertesek nevét és győzelmeinek számát egy H2 adatbázisban tárolja (`highscore.mv.db`).
- **Nyerés ellenőrzése:** A játék figyeli a vízszintes, függőleges és átlós találatokat (4 egyforma jel).
- **Naplózás:** Logback használata a futás közbeni események rögzítésére.

## 🛠️ Technológiák

A projekt a következő technológiákat és könyvtárakat használja:
- **Java 21**
- **Maven** (Build és függőségkezelés)
- **JUnit 5** (Egységtesztek)
- **Mockito** (Mockolás a tesztekhez)
- **H2 Database** (Beágyazott adatbázis a High Score-hoz)
- **Logback / SLF4J** (Naplózás)

## 📦 Telepítés és Futtatás

A projekt futtatásához szükséges a Java 21 JDK és a Maven telepítése.

### 1. Fordítás és Tesztelés
A projekt fordítása, a tesztek futtatása és a checkstyle ellenőrzés az alábbi Maven paranccsal végezhető el:

```bash
mvn clean install
