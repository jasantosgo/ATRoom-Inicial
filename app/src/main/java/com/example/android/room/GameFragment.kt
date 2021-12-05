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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.room.databinding.FragmentGameBinding
import com.google.android.material.snackbar.Snackbar

class GameFragment : Fragment() {
    data class Question(
        val text: String,
        val answers: List<String>,
        val hint: String
    )

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf()

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numQuestions = 0
    private var hint = ""
    private var hintUsed = false
    private var score = 0
    private var streak = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false)

        //obtiene las preguntas de strings.xml y las carga en questions
        obtienePreguntas()

        //Toma el valor mínimo del número de preguntas+1 dividido entre 2 o el número que llega segun el nivel.
        val args = GameFragmentArgs.fromBundle(requireArguments())
        numQuestions = Math.min(questions.size, args.numPreguntas)
        var nivel : String
        when(args.numPreguntas) {
            2-> nivel = resources.getStringArray(R.array.niveles)[0]
            4-> nivel = resources.getStringArray(R.array.niveles)[1]
            6-> nivel = resources.getStringArray(R.array.niveles)[2]
            else-> nivel="No determinado"
        }

        binding.tvLevel.apply {
            text= resources.getString(R.string.nivel_puntuacion,nivel)
        }

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        binding.btnHint.setOnClickListener {
            mostrarPista(it)
        }

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    //actualizamos la puntuacion...
                    score+=if(hintUsed) 10*streak/2 else 10*streak
                    streak++
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(questionIndex,numQuestions,score))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment(questionIndex,numQuestions,score))
                }
            }
        }

        return binding.root
    }

    private fun obtienePreguntas() {
        val listaPreguntas = resources.getStringArray(R.array.preguntas)
        var i=0
        //questions.add(Question(text = "Pregunta1", answers = listOf<String>("respuesta1","respuesta2","respuesta3","respuesta4"),hint = "Pista1"))
        while (i<listaPreguntas.size) {
            questions.add(Question(text = listaPreguntas[i],answers = listOf(listaPreguntas[i+1],listaPreguntas[i+2],listaPreguntas[i+3],listaPreguntas[i+4]),hint = listaPreguntas[i+5]))
            i+=6
        }
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions, score)
        //actualizar pista a false
        hint = currentQuestion.hint
        hintUsed=false
    }

    private fun mostrarPista(view: View) {
        Snackbar.make(requireContext(),view,hint,Snackbar.LENGTH_LONG).show()
        hintUsed=true
    }
}
