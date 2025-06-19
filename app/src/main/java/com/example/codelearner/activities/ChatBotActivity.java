package com.example.codelearner.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

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
    private ImageView sendIcon, plusIcon;
    private LinearLayout chatMessagesContainer;
    private ScrollView chatScrollView;
    private GeminiApiService geminiApi;
    private String studentName;
    private static final int PICK_IMAGE_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // Styling the system UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(0);
        }

        studentName = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("studentName", "Student");

        messageInput = findViewById(R.id.messageInput);
        sendIcon = findViewById(R.id.sendIcon);
        plusIcon = findViewById(R.id.plusIcon);
        chatMessagesContainer = findViewById(R.id.chatMessagesContainer);
        chatScrollView = findViewById(R.id.chatScrollView);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        geminiApi = ApiClient.getClient().create(GeminiApiService.class);

        showAIMessage("Hello " + studentName + ", I'm Tiffany. How can I assist you today?");

        // Send Message Click
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

        // Image Attachment Click
        plusIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    // Handle Gallery Image Selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            showSelectedImage(imageUri);
        }
    }

    private void showSelectedImage(Uri imageUri) {
        View view = getLayoutInflater().inflate(R.layout.item_user_image_message, chatMessagesContainer, false);
        ImageView userImage = view.findViewById(R.id.userImageView);
        userImage.setImageURI(imageUri);

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
        ImageView btnRefresh = view.findViewById(R.id.btnRefresh);
        ImageView btnLike = view.findViewById(R.id.btnLike);
        ImageView btnVolume = view.findViewById(R.id.btnVolume);

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

        btnLike.setOnClickListener(v -> Toast.makeText(this, "Thank you for your feedback", Toast.LENGTH_SHORT).show());
        btnVolume.setOnClickListener(v -> Toast.makeText(this, "Volume button (feature coming soon)", Toast.LENGTH_SHORT).show());
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
}
