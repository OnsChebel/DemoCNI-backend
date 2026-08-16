package com.enicar.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    @Value("${openrouter.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(
            @RequestBody Map<String, String> payload) {

        String userQuestion = payload.get("question");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("response", reponseDeSecours(userQuestion))
            );
        }

        String url = "https://openrouter.ai/api/v1/chat/completions";

        String systemPrompt =
                "أنت مساعد افتراضي ذكي لمركز تدريب وتكوين. "
                        + "أجب دائماً باللغة العربية. "
                        + "اجعل إجاباتك قصيرة وواضحة ومباشرة. "
                        + "ساعد المستخدم في الأسئلة المتعلقة بالتسجيل "
                        + "والدورات وإلغاء التسجيل والأرشيف. "
                        + "إذا لم تعرف الإجابة، أخبر المستخدم بذلك "
                        + "بدلاً من اختلاق معلومات.";

        Map<String, String> systemMessage = Map.of(
                "role", "system",
                "content", systemPrompt
        );

        Map<String, String> userMessage = Map.of(
                "role", "user",
                "content", userQuestion != null ? userQuestion : ""
        );

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", "openrouter/free");

        requestBody.put(
                "messages",
                List.of(systemMessage, userMessage)
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(
                "Authorization",
                "Bearer " + apiKey
        );

        headers.set(
                "X-Title",
                "Chatbot Centre de Formation"
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            url,
                            entity,
                            Map.class
                    );

            Map body = response.getBody();

            if (body != null && body.containsKey("choices")) {

                List choices = (List) body.get("choices");

                if (choices != null && !choices.isEmpty()) {

                    Map firstChoice = (Map) choices.get(0);

                    Map message = (Map) firstChoice.get("message");

                    if (message != null) {

                        String aiResponse =
                                (String) message.get("content");

                        if (aiResponse != null &&
                                !aiResponse.trim().isEmpty()) {

                            return ResponseEntity.ok(
                                    Map.of("response", aiResponse)
                            );
                        }
                    }
                }
            }

            return ResponseEntity.ok(
                    Map.of("response", reponseDeSecours(userQuestion))
            );

        } catch (HttpStatusCodeException e) {

            System.err.println(
                    "OPENROUTER ERREUR : " +
                            e.getStatusCode()
            );

            System.err.println(
                    e.getResponseBodyAsString()
            );

            return ResponseEntity.ok(
                    Map.of("response", reponseDeSecours(userQuestion))
            );

        } catch (Exception e) {

            System.err.println(
                    "ERREUR OPENROUTER : " +
                            e.getMessage()
            );

            return ResponseEntity.ok(
                    Map.of("response", reponseDeSecours(userQuestion))
            );
        }
    }

    private String reponseDeSecours(String q) {

        if (q == null) {
            return "أهلاً بك! كيف يمكنني مساعدتك؟";
        }

        String query = q.toLowerCase();

        if (query.contains("تسجيل") ||
                query.contains("أسجل")) {

            return "للتسجيل في دورة: اختر الدورة المناسبة واضغط على زر "
                    + "'التسجيل في الدورة' ثم قم بتأكيد بياناتك";
        }

        if (query.contains("إلغاء") ||
                query.contains("ألغي")) {

            return "يمكنك إلغاء التسجيل بالضغط على زر "
                    + "'إلغاء التسجيل' الموجود في بطاقة الدورة الخاصة بك";
        }

        if (query.contains("أرشيف") ||
                query.contains("سابقة")) {

            return "يمكنك الاطلاع على الدورات السابقة عبر الضغط على زر "
                    + "'أرشيف الدورات السابقة' أعلى الصفحة";
        }

        return "أهلاً بك! أنا المساعد الافتراضي لمركز التكوين. "
                + "يمكنك الاستفسار عن التسجيل، إلغاء التسجيل، "
                + "أو الأرشيف";
    }
}
