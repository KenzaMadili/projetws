package com.example.projetws;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEtudiantActivity extends AppCompatActivity {

    private static final String BASE_URL    = "http://192.168.1.101/monProjet/ws/";
    private static final String LOAD_URL    = BASE_URL + "loadEtudiant.php";
    private static final String UPDATE_URL  = BASE_URL + "updateEtudiant.php";
    private static final String DELETE_URL  = BASE_URL + "deleteEtudiant.php";

    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private List<Etudiant> liste = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);

        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EtudiantAdapter(liste, this::showOptionsDialog);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAjouter).setOnClickListener(v ->
                startActivity(new Intent(this, AddEtudiant.class)));

        chargerListe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerListe();
    }

    // ── Charger la liste ──────────────────────────────────────────────────
    private void chargerListe() {
        StringRequest request = new StringRequest(Request.Method.GET, LOAD_URL,
                response -> {
                    Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    liste.clear();
                    liste.addAll(etudiants);
                    adapter.setListe(liste);
                },
                error -> Toast.makeText(this,
                        "Erreur chargement : " + error.getMessage(),
                        Toast.LENGTH_SHORT).show());
        requestQueue.add(request);
    }

    // ── Popup choix : Modifier / Supprimer ───────────────────────────────
    private void showOptionsDialog(Etudiant e) {
        new AlertDialog.Builder(this)
                .setTitle(e.getNom() + " " + e.getPrenom())
                .setItems(new String[]{"Modifier", "Supprimer"}, (dialog, which) -> {
                    if (which == 0) showModifierDialog(e);
                    else            showConfirmSupprimer(e);
                })
                .show();
    }

    // ── Dialog Modifier ───────────────────────────────────────────────────
    private void showModifierDialog(Etudiant e) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_etudiant, null);

        EditText etNom    = view.findViewById(R.id.dialogNom);
        EditText etPrenom = view.findViewById(R.id.dialogPrenom);
        Spinner  spVille  = view.findViewById(R.id.dialogVille);
        RadioButton rbH   = view.findViewById(R.id.dialogHomme);
        RadioButton rbF   = view.findViewById(R.id.dialogFemme);

        // Pré-remplir les champs
        etNom.setText(e.getNom());
        etPrenom.setText(e.getPrenom());

        // Pré-sélectionner la ville
        String[] villes = getResources().getStringArray(R.array.villes);
        for (int i = 0; i < villes.length; i++) {
            if (villes[i].equals(e.getVille())) {
                spVille.setSelection(i);
                break;
            }
        }

        // Pré-sélectionner le sexe
        if ("homme".equals(e.getSexe())) rbH.setChecked(true);
        else                              rbF.setChecked(true);

        new AlertDialog.Builder(this)
                .setTitle("Modifier l'étudiant")
                .setView(view)
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    e.setNom(etNom.getText().toString().trim());
                    e.setPrenom(etPrenom.getText().toString().trim());
                    e.setVille(spVille.getSelectedItem().toString());
                    e.setSexe(rbH.isChecked() ? "homme" : "femme");
                    modifierEtudiant(e);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // ── Confirmation Suppression ──────────────────────────────────────────
    private void showConfirmSupprimer(Etudiant e) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer la suppression")
                .setMessage("Voulez-vous vraiment supprimer "
                        + e.getNom() + " " + e.getPrenom() + " ?")
                .setPositiveButton("Supprimer", (dialog, which) -> supprimerEtudiant(e))
                .setNegativeButton("Annuler", null)
                .show();
    }

    // ── Requête Modifier ──────────────────────────────────────────────────
    private void modifierEtudiant(Etudiant e) {
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    Toast.makeText(this, "Étudiant modifié !", Toast.LENGTH_SHORT).show();
                    chargerListe();
                },
                error -> Toast.makeText(this,
                        "Erreur : " + error.getMessage(),
                        Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",     String.valueOf(e.getId()));
                params.put("nom",    e.getNom());
                params.put("prenom", e.getPrenom());
                params.put("ville",  e.getVille());
                params.put("sexe",   e.getSexe());
                return params;
            }
        };
        requestQueue.add(request);
    }

    // ── Requête Supprimer ─────────────────────────────────────────────────
    private void supprimerEtudiant(Etudiant e) {
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL,
                response -> {
                    Toast.makeText(this, "Étudiant supprimé !", Toast.LENGTH_SHORT).show();
                    chargerListe();
                },
                error -> Toast.makeText(this,
                        "Erreur : " + error.getMessage(),
                        Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(e.getId()));
                return params;
            }
        };
        requestQueue.add(request);
    }
}