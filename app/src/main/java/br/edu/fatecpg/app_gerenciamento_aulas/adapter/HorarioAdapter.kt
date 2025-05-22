package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class HorarioAdapter(private val lista: List<Horario>) :
    RecyclerView.Adapter<HorarioAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtData: TextView = itemView.findViewById(R.id.etData)
        val txtHora: TextView = itemView.findViewById(R.id.etHora)
        val txtDisciplina: TextView = itemView.findViewById(R.id.etDisciplina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val horario = lista[position]
        holder.txtData.text = "Data: ${horario.data}"
        holder.txtHora.text = "Hora: ${horario.hora}"
        holder.txtDisciplina.text = "Disciplina: ${horario.disciplina}"
    }
}
