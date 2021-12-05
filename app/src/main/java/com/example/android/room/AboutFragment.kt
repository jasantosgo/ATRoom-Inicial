/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android.room.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //creamos un binding y cargamos la interfaz
        val binding = DataBindingUtil.inflate<FragmentAboutBinding>(
            inflater,R.layout.fragment_about,container,false
        )

        //incluimos click listener al boton de volver al titulo y navega hasta él.
        binding.btnAboutToTitle.setOnClickListener{
            //navegación estándar.
            //it.findNavController().navigate(AboutFragmentDirections.actionAboutFragmentToTitleFragment())
            //otro modo para que la pila quede vacía y se cierre la aplicación al pulsar el botón de retroceso en la pantalla de título.
            it.findNavController().popBackStack()
        }
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_about, container, false)
        return binding.root
    }
}
