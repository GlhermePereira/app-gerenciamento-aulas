package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Horario

class HorarioAdapter(
    private val horarios: MutableList<Horario>,
    private val onEditar: (Horario) -> Unit,
    private val onExcluir: (Horario) -> Unit,
    private val onAdicionarMaterial: (Horario) -> Unit
) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDataHora = itemView.findViewById<TextView>(R.id.tvDataHora)
        val tvDisciplina = itemView.findViewById<TextView>(R.id.tvDisciplina)
        val btnExcluir = itemView.findViewById<ImageButton>(R.id.btnExcluir)
        val btnEditar = itemView.findViewById<ImageButton>(R.id.btnEditar)
        val btnAdicionarMaterial = itemView.findViewById<ImageButton>(R.id.btnAdicionarMaterial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]

        holder.tvDataHora.text = "${horario.data} - ${horario.hora}"
        holder.tvDisciplina.text = horario.disciplina

        holder.btnEditar.setOnClickListener {
            onEditar(horario)
        }

        holder.btnExcluir.setOnClickListener {
            val context = holder.itemView.context
            HorarioDao.excluir(horario.id) { sucesso ->
                if (sucesso) {
                    Toast.makeText(context, "Horário excluído", Toast.LENGTH_SHORT).show()
                    horarios.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Toast.makeText(context, "Erro ao excluir", Toast.LENGTH_LONG).show()
                }
            }
        }

        holder.btnAdicionarMaterial.setOnClickListener {
            onAdicionarMaterial(horario)
        }
    }
    fun atualizarLista(novosHorarios: List<Horario>) {
        horarios.clear()
        horarios.addAll(novosHorarios)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = horarios.size
}
