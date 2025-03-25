package com.example.mathgameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("start") }
    var totalQuestions by remember { mutableStateOf(5) }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }

    when (currentScreen) {
        "start" -> StartScreen(onStartClicked = {
            totalQuestions = it
            currentScreen = "questions"
        })
        "questions" -> QuestionScreen(totalQuestions = totalQuestions, onGameFinished = { correct, incorrect ->
            correctAnswers = correct
            incorrectAnswers = incorrect
            currentScreen = "result"
        })
        "result" -> ResultScreen(correctAnswers = correctAnswers, incorrectAnswers = incorrectAnswers)
    }
}

@Composable
fun StartScreen(onStartClicked: (Int) -> Unit) {
    var numberOfQuestions by remember { mutableStateOf("5") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter Number of Questions")
        OutlinedTextField(
            value = numberOfQuestions,
            onValueChange = { numberOfQuestions = it },
            label = { Text("Number of Questions") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val questions = numberOfQuestions.toIntOrNull() ?: 5
            onStartClicked(questions)
        }) {
            Text("Start Game")
        }
    }
}

@Composable
fun QuestionScreen(
    totalQuestions: Int,
    onGameFinished: (Int, Int) -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var currentQuestion by remember { mutableStateOf(generateQuestion()) }
    var userAnswer by remember { mutableStateOf("") }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Question ${currentQuestionIndex + 1}/$totalQuestions")
        Text("${currentQuestion.first} + ${currentQuestion.second} = ?")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Your Answer") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val correct = currentQuestion.first + currentQuestion.second
            if (userAnswer.toIntOrNull() == correct) correctAnswers++ else incorrectAnswers++
            if (currentQuestionIndex + 1 < totalQuestions) {
                currentQuestionIndex++
                currentQuestion = generateQuestion()
                userAnswer = ""
            } else {
                onGameFinished(correctAnswers, incorrectAnswers)
            }
        }) {
            Text("Next")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Correct: $correctAnswers  Incorrect: $incorrectAnswers")
    }
}

fun generateQuestion(): Pair<Int, Int> {
    val a = (1..10).random()
    val b = (1..10).random()
    return Pair(a, b)
}

@Composable
fun ResultScreen(correctAnswers: Int, incorrectAnswers: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Over")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Correct Answers: $correctAnswers")
        Text("Incorrect Answers: $incorrectAnswers")
    }
}
