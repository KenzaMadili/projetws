package com.example.projetws;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetws.beans.Etudiant;
import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Etudiant etudiant);
    }

    private List<Etudiant> liste;
    private OnItemClickListener listener;

    public EtudiantAdapter(List<Etudiant> liste, OnItemClickListener listener) {
        this.liste    = liste;
        this.listener = listener;
    }

    public void setListe(List<Etudiant> liste) {
        this.liste = liste;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etudiant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Etudiant e = liste.get(position);

        // Initiales pour l'avatar
        String initiales = "";
        if (e.getNom() != null && !e.getNom().isEmpty())
            initiales += e.getNom().charAt(0);
        if (e.getPrenom() != null && !e.getPrenom().isEmpty())
            initiales += e.getPrenom().charAt(0);

        holder.tvAvatar.setText(initiales.toUpperCase());
        holder.tvNomPrenom.setText(e.getNom() + " " + e.getPrenom());
        holder.tvVilleSexe.setText(e.getVille() + " · " + e.getSexe());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(e));
    }

    @Override
    public int getItemCount() { return liste == null ? 0 : liste.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvNomPrenom, tvVilleSexe;
        ViewHolder(View v) {
            super(v);
            tvAvatar     = v.findViewById(R.id.tvAvatar);
            tvNomPrenom  = v.findViewById(R.id.tvNomPrenom);
            tvVilleSexe  = v.findViewById(R.id.tvVilleSexe);
        }
    }
}