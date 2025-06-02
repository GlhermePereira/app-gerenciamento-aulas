package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class AgendamentoAdapter(
    private val horariosDisponiveis: List<Horario>,
    private val onAgendar: (Horario) -> Unit
) : RecyclerView.Adapter<AgendamentoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agendamento, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = horariosDisponiveis.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val horario = horariosDisponiveis[position]
        holder.tvDataHora.text = "${horario.data} - ${horario.hora}"
        holder.tvDisciplina.text = horario.disciplina
        holder.btnAgendar.setOnClickListener { onAgendar(horario) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDataHora: TextView = view.findViewById(R.id.tvDataHora)
        val tvDisciplina: TextView = view.findViewById(R.id.tvDisciplina)
        val btnAgendar: ImageButton = view.findViewById(R.id.btnAgendar)
    }
}
