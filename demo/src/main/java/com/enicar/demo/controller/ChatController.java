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

    @Value("${gemini.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
        String userQuestion = payload.get("question");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            return ResponseEntity.ok(Map.of("response", reponseDeSecours(userQuestion)));
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        String systemContext = "أنت مساعد افتراضي ذكي لمركز تدريب وتكوين. "
                + "إجاباتك يجب أن تكون باللغة العربية، قصيرة، واضحة ومباشرة. "
                + "سؤال المشارك: " + userQuestion;

        Map<String, Object> textObj = Map.of("text", systemContext);
        Map<String, Object> partsObj = Map.of("parts", List.of(textObj));
        Map<String, Object> requestBody = Map.of("contents", List.of(partsObj));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map body = response.getBody();

            if (body != null && body.containsKey("candidates")) {
                List candidates = (List) body.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map firstCandidate = (Map) candidates.get(0);
                    Map content = (Map) firstCandidate.get("content");
                    List parts = (List) content.get("parts");
                    Map firstPart = (Map) parts.get(0);
                    String aiResponse = (String) firstPart.get("text");

                    return ResponseEntity.ok(Map.of("response", aiResponse));
                }
            }

            return ResponseEntity.ok(Map.of("response", reponseDeSecours(userQuestion)));

        } catch (HttpStatusCodeException e) {
            System.err.println("API GEMINI INDISPONIBLE (" + e.getStatusCode() + ") -> Bascule sur le mode statique.");
            return ResponseEntity.ok(Map.of("response", reponseDeSecours(userQuestion)));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("response", reponseDeSecours(userQuestion)));
        }
    }

    private String reponseDeSecours(String q) {
        if (q == null) return "أهلاً بك! كيف يمكنني مساعدتك؟";
        String query = q.toLowerCase();

        if (query.contains("تسجيل") || query.contains("أسجل")) {
            return "للتسجيل في دورة: اختر الدورة المناسبة واضغط على زر 'التسجيل في الدورة' ثم قم بتأكيد بياناتك";
        }
        if (query.contains("إلغاء") || query.contains("ألغي")) {
            return "يمكنك إلغاء التسجيل بالضغط على زر 'إلغاء التسجيل' الموجود في بطاقة الدورة الخاصة بك";
        }
        if (query.contains("أرشيف") || query.contains("سابقة")) {
            return "يمكنك الاطلاع على الدورات السابقة عبر الضغط على زر 'أرشيف الدورات السابقة' أعلى الصفحة";
        }
        return "أهلاً بك! أنا المساعد الافتراضي لمركز التكوين. يمكنك الاستفسار عن التسجيل، إلغاء التسجيل، أو الأرشيف";
    }
}