package com.example.android.room

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.room.databinding.FragmentTitleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class TitleFragment : Fragment() {

    private var nivel : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)
        nivel=-1
        //Botón de Play que lleva al fragmento GameFragment.
        binding.playButton.setOnClickListener { view : View ->
            if(nivel==-1) {
                Snackbar.make(requireContext(), view, resources.getString(R.string.debes_selec_nivel), Snackbar.LENGTH_SHORT).show()
            } else {
                view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment(nivel))
            }

        }

        binding.btnAbout.setOnClickListener {
            it.findNavController().navigate(R.id.action_titleFragment_to_aboutFragment)
        }

        binding.btnReglas.setOnClickListener {
            it.findNavController().navigate(R.id.action_titleFragment_to_rulesFragment)
        }

        binding.btnLevel.setOnClickListener {
            mostrarLevelDialog()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_opciones, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun mostrarLevelDialog() {
        val singleItems = resources.getStringArray(R.array.niveles)
        val checkedItem = nivel
        var intermedio = nivel

        MaterialAlertDialogBuilder(requireView().context)
            .setTitle(R.string.seleccion_nivel)
            .setNeutralButton(R.string.cancelar) { dialog, which ->
                // Respond to neutral button press
                nivel=-1
            }
            .setPositiveButton(R.string.aceptar) { dialog, which ->
                // Respond to positive button press
                nivel=(intermedio+1)*2
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                // Respond to item chosen
                intermedio=which
            }
            .show()
    }
}