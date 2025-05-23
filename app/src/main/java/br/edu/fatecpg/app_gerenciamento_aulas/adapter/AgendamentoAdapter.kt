package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento

private class AgendamentoAdapter(
    private val items: List<Agendamento>,
    private val onAddMaterial: (Agendamento) -> Unit,
    private val onMoreOptions: (Agendamento, View) -> Unit
) : RecyclerView.Adapter<AgendamentoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agendamento, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ag = items[position]
        holder.tvInfo.text = "${ag.dataAula} ${ag.dataHora} - ${ag.professorId}"
        holder.btnAdd.setOnClickListener { onAddMaterial(ag) }
        holder.btnMore.setOnClickListener { onMoreOptions(ag, holder.btnMore) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInfo: TextView = view.findViewById(R.id.tvDataHoraDisciplina)
        val btnAdd: ImageButton = view.findViewById(R.id.btnAddMaterial)
        val btnMore: ImageButton = view.findViewById(R.id.btnEditarExcluir)
    }
}
