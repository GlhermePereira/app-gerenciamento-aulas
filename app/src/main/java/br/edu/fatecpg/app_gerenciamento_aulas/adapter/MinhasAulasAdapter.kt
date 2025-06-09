package br.edu.fatecpg.app_gerenciamento_aulas.adapter

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.app_gerenciamento_aulas.R
import br.edu.fatecpg.app_gerenciamento_aulas.dao.AgendamentoDao
import br.edu.fatecpg.app_gerenciamento_aulas.dao.HorarioDao
import br.edu.fatecpg.app_gerenciamento_aulas.model.Agendamento

class MinhasAulasAdapter(
    private val agendamentos: MutableList<Agendamento>
) : RecyclerView.Adapter<MinhasAulasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDataHora: TextView = view.findViewById(R.id.tvDataHora)
        val tvDisciplina: TextView = view.findViewById(R.id.tvDisciplina)
        val btnCancelar: ImageButton = view.findViewById(R.id.btnCancelar)
        val btnInfo: ImageButton = view.findViewById(R.id.btnInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meus_agendamentos, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = agendamentos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agendamento = agendamentos[position]
        holder.tvDataHora.text = "${agendamento.data} - ${agendamento.hora}"
        holder.tvDisciplina.text = agendamento.disciplina

        holder.btnCancelar.setOnClickListener {
            AgendamentoDao.cancelarAgendamento(agendamento.id!!) { sucesso ->
                if (sucesso) {
                    HorarioDao.atualizarDisponibilidade(agendamento.horarioId, true) { atualizou ->
                        if (atualizou) {
                            Toast.makeText(holder.itemView.context, "Agendamento cancelado!", Toast.LENGTH_SHORT).show()

                            val pos = holder.adapterPosition
                            if (pos != RecyclerView.NO_POSITION && pos < agendamentos.size) {
                                agendamentos.removeAt(pos)
                                notifyItemRemoved(pos)
                            }

                        } else {
                            Toast.makeText(holder.itemView.context, "Erro ao atualizar horário", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(holder.itemView.context, "Erro ao cancelar agendamento", Toast.LENGTH_SHORT).show()
                }
            }
        }



        holder.btnInfo.setOnClickListener {
            HorarioDao.buscarPorId(agendamento.horarioId) { horario ->
                if (horario != null) {
                    val materiais = horario.materiais
                    val info = """
                Disciplina: ${agendamento.disciplina}
                Data: ${agendamento.data}
                Hora: ${agendamento.hora}
                Materiais: ${if (materiais.isEmpty()) "Nenhum disponível" else materiais.joinToString("\n")}
            """.trimIndent()

                    val builder = AlertDialog.Builder(holder.itemView.context)
                        .setTitle("Informações da Aula")
                        .setMessage(info)
                        .setPositiveButton("OK", null)

                    // Se houver materiais, adiciona botão para abrir todos
                    if (materiais.isNotEmpty()) {
                        builder.setNeutralButton("Ver Materiais") { _, _ ->
                            val context = holder.itemView.context
                            val materiaisArray = materiais.toTypedArray()

                            AlertDialog.Builder(context)
                                .setTitle("Escolha um material para abrir")
                                .setItems(materiaisArray) { _, which ->
                                    val selectedLink = materiaisArray[which]
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedLink))
                                    context.startActivity(intent)
                                }
                                .setNegativeButton("Cancelar", null)
                                .show()
                        }

                    }

                    builder.show()
                } else {
                    Toast.makeText(holder.itemView.context, "Erro ao buscar horário", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
}
