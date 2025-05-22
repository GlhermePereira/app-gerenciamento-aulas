package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class HorarioAdapter(private val lista: List<Horario>) :
    RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    class HorarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataHora: TextView = view.findViewById(R.id.tvDataHora)
        val disciplina: TextView = view.findViewById(R.id.tvDisciplina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = lista[position]
        holder.dataHora.text = "${horario.data} ${horario.hora}"
        holder.disciplina.text = horario.disciplina
    }

    override fun getItemCount(): Int = lista.size
}
