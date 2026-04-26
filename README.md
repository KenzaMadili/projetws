# 📱 Lab Android — Gestion des Étudiants avec XAMPP & Web Services PHP



> Extension du Lab 15 — Implémentation d'un **RecyclerView** connecté à MySQL via PHP,
> avec modification et suppression dynamiques depuis l'application Android.

---

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Démonstration vidéo](#démonstration-vidéo)
- [Fonctionnalités](#fonctionnalités)
- [Architecture du projet](#architecture-du-projet)
- [Prérequis](#prérequis)
- [Installation & Configuration](#installation--configuration)
- [Structure des fichiers](#structure-des-fichiers)
- [Web Services PHP](#web-services-php)
- [Configuration réseau](#configuration-réseau)
- [Utilisation](#utilisation)
- [Dépannage](#dépannage)

---

## Aperçu

Ce challenge étend le Lab 15 en ajoutant une interface complète de gestion des étudiants :
affichage dans un **RecyclerView**, **popup de modification**, **alerte de confirmation**
avant suppression, et **actualisation dynamique** après chaque opération.

### Ce qui a été ajouté par rapport au Lab 2

| Lab 15 | Ce lab |
|-------|-----------|
| Formulaire d'ajout uniquement | Formulaire d'ajout + liste complète |
| Résultat visible dans Logcat | Résultat visible dans l'interface |
| 2 web services PHP | 4 web services PHP |
| 1 activité | 2 activités + 1 adapter |

---

## 🎬 Démonstration vidéo

> La vidéo ci-dessous présente le fonctionnement complet du challenge.



https://github.com/user-attachments/assets/3def0b20-e98a-473d-ba02-ea2cc3aaf322



---

## ✅ Fonctionnalités

- 📋 **Lister** tous les étudiants dans un RecyclerView avec avatar (initiales)
- ➕ **Ajouter** via FAB (Floating Action Button) — standard Material Design
- ✏️ **Modifier** via dialog pré-rempli au clic sur un élément
- 🗑️ **Supprimer** avec alerte de confirmation avant exécution
- 🔄 **Actualisation automatique** de la liste après chaque opération
- 🎨 Interface moderne : cards, avatars colorés, FAB vert

---

## 🏗️ Architecture du projet

```
┌─────────────────────────────────────────────────────────────┐
│                     APPLICATION ANDROID                      │
│                                                             │
│  ┌───────────────────────┐    ┌──────────────────────────┐ │
│  │  ListEtudiantActivity │    │      AddEtudiant          │ │
│  │  ┌─────────────────┐  │    │  Nom | Prénom             │ │
│  │  │  RecyclerView   │  │    │  Ville | Sexe             │ │
│  │  │  ┌───────────┐  │  │    │  Bouton Ajouter           │ │
│  │  │  │item_card  │  │  │    └──────────────────────────┘ │
│  │  │  │avatar|nom │  │  │                                 │
│  │  │  │ville·sexe │  │  │                                 │
│  │  │  └───────────┘  │  │                                 │
│  │  └─────────────────┘  │                                 │
│  │  FAB (+)              │                                 │
│  │  ┌─────────────────┐  │                                 │
│  │  │ AlertDialog     │  │                                 │
│  │  │ Modifier /      │  │                                 │
│  │  │ Supprimer       │  │                                 │
│  │  └─────────────────┘  │                                 │
│  └──────────┬────────────┘                                 │
│             │  Volley POST/GET                              │
│  ┌──────────▼────────────────────────────────────────────┐ │
│  │              EtudiantAdapter (RecyclerView)            │ │
│  │     onItemClick → showOptionsDialog()                  │ │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────────────────┘
                       │ HTTP POST / GET
┌──────────────────────┼──────────────────────────────────────┐
│                   SERVEUR XAMPP                              │
│                      │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │   loadEtudiant.php   │   createEtudiant.php           │  │
│  │   updateEtudiant.php │   deleteEtudiant.php           │  │
│  └───────────────────┬──────────────────────────────────┘  │
│                      │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │           EtudiantService.php (PDO)                   │  │
│  └───────────────────┬──────────────────────────────────┘  │
│                      │                                      │
│  ┌───────────────────▼──────────────────────────────────┐  │
│  │          MySQL — Base : school1 — Table : Etudiant    │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔧 Prérequis

### Côté serveur

| Outil | Version |
|-------|---------|
| XAMPP | 8.x ou supérieur |
| Apache | Inclus dans XAMPP |
| MySQL | Inclus dans XAMPP |
| PHP | 8.0 ou supérieur |

### Côté Android

| Outil | Version |
|-------|---------|
| Android Studio | Hedgehog ou supérieur |
| JDK | 11 ou supérieur |
| Android SDK | API 26 minimum |
| Appareil / Émulateur | Android 8.0+ |

---

## 🚀 Installation & Configuration

### Étape 1 — Démarrer XAMPP

1. Ouvrir le **XAMPP Control Panel**
2. Démarrer **Apache** et **MySQL**
3. Vérifier que les deux services sont en vert

### Étape 2 — Déposer les fichiers PHP

Copier les 2 nouveaux fichiers dans :

```
C:\xampp\htdocs\projet\ws\
├── createEtudiant.php      ← existant (Lab 2)
├── loadEtudiant.php        ← existant (Lab 2)
├── updateEtudiant.php      ← 🆕 nouveau (challenge)
└── deleteEtudiant.php      ← 🆕 nouveau (challenge)
```

### Étape 3 — Tester les nouveaux web services

**Test update via Postman / Advanced REST Client :**
```
URL    : http://localhost/projet/ws/updateEtudiant.php
Method : POST
Body   : id=1&nom=NouveauNom&prenom=NouveauPrenom&ville=Rabat&sexe=homme
```

**Test delete :**
```
URL    : http://localhost/projet/ws/deleteEtudiant.php
Method : POST
Body   : id=1
```

Les deux doivent retourner la liste JSON mise à jour.

### Étape 4 — Configurer l'URL selon votre connexion

Modifier `BASE_URL` dans `ListEtudiantActivity.java` et `AddEtudiant.java` :

```java
// Émulateur Android
private static final String BASE_URL = "http://10.0.2.2/projet/ws/";

// Vrai téléphone via WiFi (même réseau que le PC)
private static final String BASE_URL = "http://192.168.1.XX/projet/ws/";

// Vrai téléphone via câble USB (ADB reverse)
// → Exécuter d'abord dans cmd : adb reverse tcp:80 tcp:80
private static final String BASE_URL = "http://127.0.0.1/projet/ws/";

// Vrai téléphone en 4G (via ngrok)
private static final String BASE_URL = "https://abc123.ngrok.io/projet/ws/";
```

### Étape 5 — Ajouter la dépendance Material

Dans `build.gradle (Module: app)` :

```groovy
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.recyclerview:recyclerview:1.3.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'com.android.volley:volley:1.2.1'
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

### Étape 6 — Lancer l'application

```
Android Studio → Run ▶
```

---

## 📁 Structure des fichiers

### Android

```
app/
├── manifests/
│   └── AndroidManifest.xml
│
├── java/com/example/projetws/
│   ├── ListEtudiantActivity.java   ← 🆕 Activité principale (liste)
│   ├── AddEtudiant.java            ← existant (Lab 2)
│   ├── EtudiantAdapter.java        ← 🆕 Adapter RecyclerView
│   └── beans/
│       └── Etudiant.java           ← mis à jour (+ setters)
│
└── res/
    ├── layout/
    │   ├── activity_list_etudiant.xml  ← 🆕 Liste + FAB
    │   ├── activity_add_etudiant.xml   ← existant
    │   ├── item_etudiant.xml           ← 🆕 Carte RecyclerView
    │   └── dialog_etudiant.xml         ← 🆕 Popup modification
    ├── drawable/
    │   ├── card_background.xml
    │   └── avatar_background.xml       ← 🆕 Cercle avatar
    ├── values/
    │   ├── styles.xml
    │   └── arrays.xml
    └── xml/
        └── network_security_config.xml
```

### PHP (XAMPP)

```
C:\xampp\htdocs\projet\ws\
├── loadEtudiant.php        ← GET  — liste tous les étudiants
├── createEtudiant.php      ← POST — ajoute un étudiant
├── updateEtudiant.php      ← POST — modifie un étudiant  🆕
└── deleteEtudiant.php      ← POST — supprime un étudiant 🆕
```

---

## 🌐 Web Services PHP

| Fichier | Méthode | Description | Réponse |
|---------|---------|-------------|---------|
| `loadEtudiant.php` | GET | Liste tous les étudiants | JSON array |
| `createEtudiant.php` | POST | Ajoute un étudiant | JSON array mis à jour |
| `updateEtudiant.php` | POST | Modifie un étudiant | JSON array mis à jour |
| `deleteEtudiant.php` | POST | Supprime un étudiant | JSON array mis à jour |

### Paramètres POST

**`updateEtudiant.php`**

| Paramètre | Type | Exemple |
|-----------|------|---------|
| `id` | int | `2` |
| `nom` | string | `Benali` |
| `prenom` | string | `Yassine` |
| `ville` | string | `Casablanca` |
| `sexe` | string | `homme` |

**`deleteEtudiant.php`**

| Paramètre | Type | Exemple |
|-----------|------|---------|
| `id` | int | `2` |

---

## 🔒 Configuration réseau

### Connexion selon l'environnement

| Environnement | URL à utiliser | Commande préalable |
|---------------|---------------|-------------------|
| Émulateur | `http://10.0.2.2/projet/ws/` | Aucune |
| Téléphone WiFi | `http://192.168.1.XX/projet/ws/` | `ipconfig` pour trouver l'IP |
| Téléphone USB | `http://127.0.0.1/projet/ws/` | `adb reverse tcp:80 tcp:80` |
| Téléphone 4G | `https://xxx.ngrok.io/projet/ws/` | `ngrok http 80` |

### `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

---

## 📖 Utilisation

### Consulter la liste

Au lancement, la liste se charge automatiquement. Chaque carte affiche les **initiales**
de l'étudiant dans un avatar vert, son **nom complet** et sa **ville · sexe**.

### Ajouter un étudiant

1. Appuyer sur le **bouton FAB vert (+)** en bas à droite
2. Remplir le formulaire : Nom, Prénom, Ville, Sexe
3. Appuyer sur **Ajouter l'étudiant**
4. Retour automatique à la liste — nouvel étudiant visible

### Modifier un étudiant

1. Appuyer sur la **carte de l'étudiant**
2. Choisir **Modifier** dans le popup
3. Le dialog s'ouvre **pré-rempli** avec les données actuelles
4. Modifier les champs souhaités
5. Appuyer sur **Enregistrer** → liste actualisée instantanément

### Supprimer un étudiant

1. Appuyer sur la **carte de l'étudiant**
2. Choisir **Supprimer** dans le popup
3. Lire l'alerte de confirmation : *"Voulez-vous vraiment supprimer X ?"*
4. Appuyer sur **Supprimer** pour confirmer
5. L'étudiant disparaît de la liste instantanément

---

## 🛠️ Dépannage

| Problème | Cause probable | Solution |
|----------|---------------|----------|
| `Erreur chargement : null` | XAMPP non démarré | Démarrer Apache + MySQL |
| `Erreur chargement : null` | Câble USB sans ADB reverse | Exécuter `adb reverse tcp:80 tcp:80` |
| `Erreur chargement : null` | Téléphone en 4G | Utiliser WiFi ou ngrok |
| `CleartextTraffic exception` | HTTP bloqué Android 9+ | Vérifier `network_security_config.xml` |
| Liste vide sans erreur | Table MySQL vide | Ajouter des données via phpMyAdmin |
| `404 Not Found` | Fichiers PHP mal placés | Vérifier `htdocs/projet/ws/` |
| FAB non affiché | Material non importé | Ajouter dépendance Material dans `build.gradle` |
| Dialog vide à l'ouverture | Setters manquants dans `Etudiant.java` | Ajouter `setNom()`, `setPrenom()`, etc. |

---

## 👨‍💻 Auteur

> Réalisé par : MADILI Kenza 
> Module : Développement Mobile  
> Technologies : Android · Java · PHP · MySQL · XAMPP · Volley · Gson · RecyclerView · Material Design

---

## 📄 Licence

Ce projet est réalisé à des fins pédagogiques.
