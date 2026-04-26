# 📱 Lab Android — Gestion des Étudiants avec XAMPP & Web Services PHP

> Application Android connectée à un serveur **XAMPP local** via des **Web Services PHP**.  
> Utilise **Volley** pour les requêtes HTTP et **Gson** pour le parsing JSON.

---

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Démonstration vidéo](#démonstration-vidéo)
- [Fonctionnalités](#fonctionnalités)
- [Architecture du projet](#architecture-du-projet)
- [Prérequis](#prérequis)
- [Installation & Configuration](#installation--configuration)
- [Structure des fichiers](#structure-des-fichiers)
- [Base de données MySQL](#base-de-données-mysql)
- [Web Services PHP](#web-services-php)
- [Configuration réseau](#configuration-réseau)
- [Utilisation](#utilisation)
- [Dépannage](#dépannage)

---

## Aperçu

Cette application Android permet d'**ajouter des étudiants** dans une base de données **MySQL** hébergée sur **XAMPP**. L'application envoie les données via une requête HTTP **POST** à un script PHP, qui insère l'étudiant en base et retourne la liste complète en **JSON**, affichée dans le **Logcat**.

### Stack technique

| Côté Android | Côté Serveur |
|---|---|
| Java + Android SDK | PHP 8 |
| Volley (requêtes HTTP POST) | Apache (XAMPP) |
| Gson (parsing JSON) | MySQL (XAMPP) |
| Spinner + RadioButton (UI) | PDO (connexion BDD) |

---

## 🎬 Démonstration vidéo

> La vidéo ci-dessous présente le fonctionnement complet du lab.




https://github.com/user-attachments/assets/b4b6c6c0-32b8-4efe-99ca-e30d8c3f8ba4



## ✅ Fonctionnalités

- ➕ **Ajouter** un étudiant (nom, prénom, ville, sexe) via requête HTTP POST
- 📋 **Afficher** dans Logcat la liste complète retournée par le serveur
- 🌐 Communication avec serveur PHP local via **Volley**
- 🔄 **Réponse JSON** parsée avec **Gson** après chaque ajout
- 🎨 Interface avec **Spinner** (villes) et **RadioButton** (sexe)

---

## 🏗️ Architecture du projet

```
┌─────────────────────────────────────────────────────────┐
│                   APPLICATION ANDROID                    │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │               AddEtudiant.java                   │  │
│  │   Nom | Prénom | Ville (Spinner) | Sexe (Radio)  │  │
│  │                  Bouton Ajouter                   │  │
│  └───────────────────────┬──────────────────────────┘  │
│                          │                              │
│  ┌───────────────────────▼──────────────────────────┐  │
│  │           Volley — StringRequest POST            │  │
│  │         params : nom, prenom, ville, sexe        │  │
│  └───────────────────────┬──────────────────────────┘  │
│                          │ réponse JSON                 │
│  ┌───────────────────────▼──────────────────────────┐  │
│  │        Gson — TypeToken<Collection<Etudiant>>    │  │
│  │              Log.d("ETUDIANT", ...)              │  │
│  └──────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP POST
┌──────────────────────────┼──────────────────────────────┐
│                    SERVEUR XAMPP                         │
│                          │                              │
│  ┌───────────────────────▼──────────────────────────┐  │
│  │          ws/createEtudiant.php                   │  │
│  │   reçoit POST → insère → retourne findAllApi()   │  │
│  └───────────────────────┬──────────────────────────┘  │
│                          │                              │
│  ┌───────────────────────▼──────────────────────────┐  │
│  │        service/EtudiantService.php (PDO)          │  │
│  └───────────────────────┬──────────────────────────┘  │
│                          │                              │
│  ┌───────────────────────▼──────────────────────────┐  │
│  │         MySQL — Base : school1                   │  │
│  │              Table : Etudiant                    │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
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
| Émulateur | Android 8.0+ |

---

## 🚀 Installation & Configuration

### Étape 1 — Démarrer XAMPP

1. Ouvrir le **XAMPP Control Panel**
2. Cliquer **Start** sur **Apache** et **MySQL**
3. Vérifier que les deux services sont affichés en vert

### Étape 2 — Créer la base de données

Ouvrir **phpMyAdmin** (`http://localhost/phpmyadmin`) et exécuter :

```sql
CREATE DATABASE school1;

USE school1;

CREATE TABLE Etudiant (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nom    VARCHAR(50),
    prenom VARCHAR(50),
    ville  VARCHAR(50),
    sexe   VARCHAR(10)
);

-- Données de test optionnelles
INSERT INTO Etudiant (nom, prenom, ville, sexe) VALUES
    ('Lachgar', 'Mohamed', 'Rabat',     'homme'),
    ('Safi',    'Amine',   'Marrakech', 'homme');
```

### Étape 3 — Déployer les fichiers PHP

Copier le dossier `projet/` dans :

```
C:\xampp\htdocs\projet\
```

Structure attendue :

```
C:\xampp\htdocs\projet\
├── classes/
│   └── Etudiant.php
├── connexion/
│   └── Connexion.php
├── dao/
│   └── IDao.php
├── service/
│   └── EtudiantService.php
└── ws/
    ├── createEtudiant.php
    └── loadEtudiant.php
```

### Étape 4 — Tester les Web Services

**Test GET** — ouvrir dans le navigateur :

```
http://localhost/projet/ws/loadEtudiant.php
```

Résultat attendu :

```json
[
  {"id":"1","nom":"Lachgar","prenom":"Mohamed","ville":"Rabat","sexe":"homme"},
  {"id":"2","nom":"Safi","prenom":"Amine","ville":"Marrakech","sexe":"homme"}
]
```

**Test POST** — via Advanced REST Client ou Postman :

```
URL    : http://localhost/projet/ws/createEtudiant.php
Method : POST
Type   : x-www-form-urlencoded
Body   : nom=Dupont | prenom=Sara | ville=Casablanca | sexe=femme
```

### Étape 5 — Vérifier l'URL dans Android

Ouvrir `AddEtudiant.java` et confirmer la constante :

```java
// Émulateur Android uniquement
// 10.0.2.2 = alias de localhost du PC depuis l'émulateur
private static final String insertUrl =
    "http://10.0.2.2/projet/ws/createEtudiant.php";
```

### Étape 6 — Lancer l'application

```
Android Studio → Run ▶ (sur émulateur)
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
│   ├── AddEtudiant.java        ← Activité principale
│   └── beans/
│       └── Etudiant.java       ← Modèle de données
│
└── res/
    ├── layout/
    │   └── activity_add_etudiant.xml
    ├── drawable/
    │   └── card_background.xml
    ├── values/
    │   ├── styles.xml
    │   └── arrays.xml          ← Liste des villes pour le Spinner
    └── xml/
        └── network_security_config.xml
```

### PHP (XAMPP)

```
C:\xampp\htdocs\projet\
├── classes/
│   └── Etudiant.php            ← Modèle PHP
├── connexion/
│   └── Connexion.php           ← Connexion PDO à MySQL
├── dao/
│   └── IDao.php                ← Interface CRUD
├── service/
│   └── EtudiantService.php     ← Logique métier + requêtes SQL
└── ws/
    ├── createEtudiant.php      ← Web service POST ajout
    └── loadEtudiant.php        ← Web service GET liste
```

---

## 🗄️ Base de données MySQL

**Nom :** `school1`  
**Table :** `Etudiant`

| Colonne | Type | Contrainte |
|---------|------|-----------|
| `id` | INT | PRIMARY KEY AUTO_INCREMENT |
| `nom` | VARCHAR(50) | — |
| `prenom` | VARCHAR(50) | — |
| `ville` | VARCHAR(50) | — |
| `sexe` | VARCHAR(10) | — |

---

## 🌐 Web Services PHP

| Fichier | Méthode HTTP | Description | Réponse |
|---------|-------------|-------------|---------|
| `loadEtudiant.php` | GET | Liste tous les étudiants | JSON array |
| `createEtudiant.php` | POST | Ajoute un étudiant | JSON array mis à jour |

### Paramètres POST de `createEtudiant.php`

| Paramètre | Type | Exemple |
|-----------|------|---------|
| `nom` | string | `Benali` |
| `prenom` | string | `Yassine` |
| `ville` | string | `Casablanca` |
| `sexe` | string | `homme` |

### Exemple de réponse JSON

```json
[
  {"id":"1","nom":"Lachgar","prenom":"Mohamed","ville":"Rabat","sexe":"homme"},
  {"id":"2","nom":"Benali","prenom":"Yassine","ville":"Casablanca","sexe":"homme"}
]
```

---

## 🔒 Configuration réseau

### `res/xml/network_security_config.xml`

Nécessaire pour autoriser les connexions HTTP (Android 9+) :

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

### `AndroidManifest.xml` — attributs requis

```xml
<uses-permission android:name="android.permission.INTERNET" />

<application
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="true"
    android:exported="true"
    ...>
```

### `build.gradle` — dépendances

```groovy
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.android.volley:volley:1.2.1'
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

---

## 📖 Utilisation

### Ajouter un étudiant

1. Remplir le champ **Nom**
2. Remplir le champ **Prénom**
3. Sélectionner une **Ville** dans le spinner déroulant
4. Choisir le **Sexe** via les boutons radio (Homme / Femme)
5. Appuyer sur **Ajouter l'étudiant**

### Lire la réponse dans Logcat

Après l'ajout, ouvrir **Logcat** dans Android Studio et filtrer par le tag `ETUDIANT` :

```
D/RESPONSE: [{"id":"1","nom":"Lachgar","prenom":"Mohamed","ville":"Rabat","sexe":"homme"},
             {"id":"2","nom":"Benali","prenom":"Yassine","ville":"Casablanca","sexe":"homme"}]

D/ETUDIANT: Etudiant{id=1, nom='Lachgar', prenom='Mohamed', ville='Rabat', sexe='homme'}
D/ETUDIANT: Etudiant{id=2, nom='Benali', prenom='Yassine', ville='Casablanca', sexe='homme'}
```

---

## 🛠️ Dépannage

| Problème | Cause probable | Solution |
|----------|---------------|----------|
| `Erreur réseau : null` | XAMPP non démarré | Démarrer Apache + MySQL |
| `Erreur réseau : null` | Mauvaise URL | Vérifier `insertUrl` dans `AddEtudiant.java` |
| `CleartextTraffic exception` | HTTP bloqué Android 9+ | Vérifier `network_security_config.xml` |
| `404 Not Found` | Mauvais chemin PHP | Vérifier que les fichiers sont dans `htdocs/projet/ws/` |
| JSON vide `[]` | Table vide | Insérer des données via phpMyAdmin |
| App crash au lancement | `exported` manquant | Ajouter `android:exported="true"` dans Manifest |
| `Connection refused` | Pare-feu Windows | Autoriser Apache dans le pare-feu Windows |
| Aucune réponse Logcat | Filtrage incorrect | Filtrer par `RESPONSE` ou `ETUDIANT` dans Logcat |

---

## 👨‍💻 Auteur

> Réalisé dans le cadre d'un TP Android — FST  
> Module : Développement Mobile  
> Technologies : Android · Java · PHP · MySQL · XAMPP · Volley · Gson

---

## 📄 Licence

Ce projet est réalisé à des fins pédagogiques.
