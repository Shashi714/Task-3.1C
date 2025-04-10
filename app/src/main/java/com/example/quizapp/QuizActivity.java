package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView progressPercentageTextView;
    private RadioGroup radioGroup;
    private Button submitButton;
    private ProgressBar progressBar;

    private String[] questions = {
            "What is the capital of Australia?",
            "Which planet is known as the Red Planet?",
            "What is the largest ocean on Earth?",
            "Who wrote 'Hamlet'?",
            "What is the smallest country in the world?",
            "Which country is known as the Land of the Rising Sun?",
            "What is the chemical symbol for water?",
            "What is the tallest mountain in the world?",
            "How many continents are there?",
            "What is the speed of light in a vacuum?"
    };

    private String[][] options = {
            {"Sydney", "Melbourne", "Canberra", "Perth"},
            {"Earth", "Mars", "Venus", "Jupiter"},
            {"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"},
            {"Shakespeare", "Dickens", "Austen", "Tolkien"},
            {"Vatican City", "Monaco", "Nauru", "San Marino"},
            {"China", "Japan", "South Korea", "Thailand"},
            {"H2O", "CO2", "O2", "N2"},
            {"Mount Everest", "K2", "Kangchenjunga", "Mount Fuji"},
            {"5", "6", "7", "8"},
            {"300,000 km/s", "150,000 km/s", "500,000 km/s", "1,000,000 km/s"}
    };

    private int[] correctAnswers = {2, 1, 3, 0, 0, 1, 0, 0, 2, 0};

    private int currentQuestionIndex = 0;
    private int score = 0;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        progressPercentageTextView = findViewById(R.id.progressPercentageTextView);
        radioGroup = findViewById(R.id.radioGroup);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        userName = getIntent().getStringExtra("USER_NAME");

        loadQuestion();
        updateProgressBar();

        submitButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = findViewById(selectedId);
            int selectedIndex = radioGroup.indexOfChild(selectedRadio);

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton btn = (RadioButton) radioGroup.getChildAt(i);
                if (i == correctAnswers[currentQuestionIndex]) {
                    btn.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else if (i == selectedIndex) {
                    btn.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                btn.setEnabled(false);
            }

            if (selectedIndex == correctAnswers[currentQuestionIndex]) {
                score++;
            }

            new Handler().postDelayed(() -> {
                currentQuestionIndex++;

                if (currentQuestionIndex < questions.length) {
                    loadQuestion();
                    updateProgressBar();
                } else {
                    Intent intent = new Intent(QuizActivity.this, FinalScoreActivity.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        });
    }

    private void loadQuestion() {
        radioGroup.clearCheck();

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton btn = (RadioButton) radioGroup.getChildAt(i);
            btn.setText(options[currentQuestionIndex][i]);
            btn.setTextColor(getResources().getColor(android.R.color.black));
            btn.setEnabled(true);
        }

        questionTextView.setText(questions[currentQuestionIndex]);
    }

    private void updateProgressBar() {
        int progress = (int) (((float) (currentQuestionIndex + 1) / questions.length) * 100);
        progressBar.setProgress(progress);

        // Update the percentage text
        progressPercentageTextView.setText(progress + "%");
    }
}

