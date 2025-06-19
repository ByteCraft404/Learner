package com.example.codelearner.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codelearner.R;
import com.example.codelearner.api.GeminiApiService;
import com.example.codelearner.Models.ChatRequest;
import com.example.codelearner.Models.ChatResponse;
import com.example.codelearner.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotActivity extends AppCompatActivity {
    private EditText messageInput;
    private ImageView sendIcon;
    private LinearLayout chatMessagesContainer;
    private ScrollView chatScrollView;
    private GeminiApiService geminiApi;
    private String studentName;
    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }


        setContentView(R.layout.activity_chat_bot);




        studentName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("studentName", "Student");

        messageInput = findViewById(R.id.messageInput);
        sendIcon = findViewById(R.id.sendIcon);
        chatMessagesContainer = findViewById(R.id.chatMessagesContainer);
        chatScrollView = findViewById(R.id.chatScrollView);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        geminiApi = ApiClient.getClient().create(GeminiApiService.class);

        showAIMessage("Hello " + studentName + ", I'm Tiffany. How can I assist you today?");

        sendIcon.setOnClickListener(v -> {
            String question = messageInput.getText().toString().trim();
            if (question.isEmpty()) {
                Toast.makeText(this, "Please enter your question", Toast.LENGTH_SHORT).show();
                return;
            }

            inflateUserMessage(question);
            messageInput.setText("");

            ChatRequest chatRequest = new ChatRequest(question);
            geminiApi.askGemini(chatRequest).enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        showAIMessage(response.body().getAnswer());
                    } else {
                        showAIMessage("Sorry, I couldn't understand that.");
                    }
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    showAIMessage("Failed to connect. Try again.");
                }
            });
        });
    }

    private void inflateUserMessage(String messageText) {
        View view = getLayoutInflater().inflate(R.layout.item_user_message, chatMessagesContainer, false);
        TextView messageTextView = view.findViewById(R.id.userMessageText);
        ImageView btnCopy = view.findViewById(R.id.btnUserCopy);

        messageTextView.setText(messageText);

        btnCopy.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("User Message", messageText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        chatMessagesContainer.addView(view);
        scrollToBottom();
    }


    private void showAIMessage(String responseText) {
        View view = getLayoutInflater().inflate(R.layout.item_ai_message, chatMessagesContainer, false);

        TextView aiResponseText = view.findViewById(R.id.aiResponseText);
        ImageView btnCopy = view.findViewById(R.id.btnCopy);
        ImageView btnRefresh = view.findViewById(R.id.btnRefresh);
        ImageView btnLike = view.findViewById(R.id.btnLike);
        ImageView btnVolume = view.findViewById(R.id.btnVolume);

        chatMessagesContainer.addView(view);
        scrollToBottom();

        // --- Animate the text like typing ---
        animateTyping(aiResponseText, responseText);

        // --- COPY Button ---
        btnCopy.setOnClickListener(v -> {
            String textToCopy = aiResponseText.getText().toString();
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("AI Response", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        // --- REFRESH Button ---
        btnRefresh.setOnClickListener(v -> {
            String question = messageInput.getText().toString().trim();
            if (question.isEmpty()) {
                Toast.makeText(this, "Type the question again to regenerate", Toast.LENGTH_SHORT).show();
                return;
            }

            geminiApi.askGemini(new ChatRequest(question)).enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        aiResponseText.setText(response.body().getAnswer());
                    } else {
                        aiResponseText.setText("Sorry, I couldn't regenerate that.");
                    }
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    aiResponseText.setText("Failed to refresh. Try again.");
                }
            });
        });

        btnLike.setOnClickListener(v -> Toast.makeText(this, "Thank you For Your Feedback", Toast.LENGTH_SHORT).show());
        btnVolume.setOnClickListener(v -> Toast.makeText(this, "Volume button (feature coming soon)", Toast.LENGTH_SHORT).show());
    }



    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void animateTyping(TextView textView, String message) {
        final int[] index = {0};
        final int delay = 30; // milliseconds between characters

        textView.setText("");

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index[0] < message.length()) {
                    textView.append(String.valueOf(message.charAt(index[0])));
                    index[0]++;
                    scrollToBottom();
                    textView.postDelayed(this, delay);
                }
            }
        }, delay);
    }


}
