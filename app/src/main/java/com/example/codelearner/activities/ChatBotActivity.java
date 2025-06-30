package com.example.codelearner.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.codelearner.Models.ChatRequest;
import com.example.codelearner.Models.ChatResponse;
import com.example.codelearner.R;
import com.example.codelearner.api.GeminiApiService;
import com.example.codelearner.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotActivity extends AppCompatActivity {
    private EditText messageInput;
    private LinearLayout chatMessagesContainer;
    private ScrollView chatScrollView;
    private GeminiApiService geminiApi;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private Uri pendingImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(0);

        String studentName = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("studentName", "Student");

        messageInput = findViewById(R.id.messageInput);
        ImageView sendIcon = findViewById(R.id.sendIcon);
        ImageView plusIcon = findViewById(R.id.plusIcon);
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

            if (pendingImageUri != null && question.toLowerCase().contains("analyze")) {
                inflateUserMessage(question);
                messageInput.setText("");
                showAIMessage("Analyzing the image... (Placeholder response)");
                pendingImageUri = null;
                return;
            }

            inflateUserMessage(question);
            messageInput.setText("");

            ChatRequest chatRequest = new ChatRequest(question);
            geminiApi.askGemini(chatRequest).enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String formattedAnswer = formatListNicely(response.body().getAnswer());
                        showAIMessage(formattedAnswer);
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

        plusIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pendingImageUri = data.getData();
            showPendingImageMessage(pendingImageUri);
        }
    }

    private void showPendingImageMessage(Uri imageUri) {
        View view = getLayoutInflater().inflate(R.layout.item_user_image_message, chatMessagesContainer, false);
        ImageView userImage = view.findViewById(R.id.userImageView);
        TextView label = view.findViewById(R.id.userImageLabel);
        userImage.setImageURI(imageUri);
        label.setText("Image uploaded. Type a command to process it.");
        chatMessagesContainer.addView(view);
        scrollToBottom();
    }

    private void inflateUserMessage(String messageText) {
        View view = getLayoutInflater().inflate(R.layout.item_user_message, chatMessagesContainer, false);
        TextView messageTextView = view.findViewById(R.id.userMessageText);
        ImageView btnCopy = view.findViewById(R.id.btnUserCopy);
        messageTextView.setText(messageText);
        btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("User Message", messageText);
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
        chatMessagesContainer.addView(view);
        scrollToBottom();
        animateTyping(aiResponseText, responseText);
        btnCopy.setOnClickListener(v -> {
            String textToCopy = aiResponseText.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("AI Response", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void animateTyping(TextView textView, String message) {
        final int[] index = {0};
        final int delay = 30;
        textView.setText("");
        new Handler().postDelayed(new Runnable() {
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

    private String formatListNicely(String original) {
        original = original.replaceAll("(?m)^[-•]\\s*", "-> ");
        original = original.replaceAll("(?m)^\\d+\\.\\s*", "$0");
        return original;
    }
}
