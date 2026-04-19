# 🌟 Stars Gallery - Android Application

## 📱 Description du projet

**Stars Gallery** est une application Android permettant d’afficher une liste d’acteurs et d’actrices avec leurs images et leurs notes.
L’utilisateur peut rechercher une star, modifier sa note et partager l’application.

---

## 🚀 Fonctionnalités principales

* 🔍 **Recherche dynamique** des stars via SearchView
* ⭐ **Affichage des notes** avec RatingBar
* ✏️ **Modification de la note** via popup interactif
* 🔗 **Partage de l’application** via Intent (WhatsApp, Gmail, etc.)
* 🖼 **Chargement des images** avec Glide
* 📜 **Affichage fluide** avec RecyclerView

---

## 🧱 Architecture

L’application est structurée en plusieurs couches :

* **UI (Activities)** : SplashActivity, ListActivity
* **Adapter** : StarAdapter (RecyclerView + Filter + Popup)
* **Service** : StarService (gestion des données)
* **Model** : classe Star

---

## 🛠 Technologies utilisées

* Java
* Android SDK
* RecyclerView
* Glide
* SearchView
* AlertDialog
* CircleImageView

---



## 🎥 Vidéo démonstrative (compressée)

Une vidéo démonstrative du fonctionnement de l’application est incluse dans le projet.

### 📁 Emplacement

```
demo_stars.mp4
```

### 📌 Contenu de la vidéo

La vidéo montre :

* L’écran de démarrage (Splash Screen)
* L’affichage de la liste des stars
* La recherche dynamique
* La modification de la note via popup
* Le partage de l’application

### ⚙️ Compression de la vidéo

La vidéo a été compressée pour :

* Réduire la taille du projet
* Faciliter le partage et le dépôt

---

## 👩‍💻 Auteur

* Nom : *Madili Kenza*
* Projet académique Android

---

## 📌 Conclusion

Ce projet illustre :

* l’utilisation de RecyclerView
* l’intégration d’une recherche dynamique
* la manipulation des données
* l’interaction utilisateur avec popup

---

## 📎 Remarque

Assurez-vous que :

* les dépendances Gradle sont synchronisées
* les images sont bien placées dans `drawable`
* la vidéo est bien compressée pour éviter un projet trop lourd

---
