# Éditeur de corpus – README

## 1. Nom et prénom

**Nom** : [Ton nom ici]  
**Prénom** : [Ton prénom ici]

---

## 2. Instructions d’installation et de lancement

### ✅ Prérequis

- JDK version 17 minimum (conformément au document “Using java” sur Moodle)
- Système : Linux (ou tout système Unix-like)

### 📁 Structure du projet

Le fichier principal est :  
`src/App.java`

Les fichiers FXML sont situés dans :  
`resources/view/`

### 🔧 Compilation

Depuis la **racine du projet**, exécute les commandes suivantes dans un terminal :

```bash
cd src
javac -d ../out --module-path /chemin/vers/javafx/lib --add-modules javafx.controls,javafx.fxml $(find . -name "*.java")
