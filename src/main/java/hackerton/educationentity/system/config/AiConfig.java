package hackerton.educationentity.system.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {

    @Bean("baseAiChatClient")
    public ChatClient baseAiChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    public RestClient ollamaRestClient(
            @Value("${spring.ai.ollama.base-url}") String ollamaBaseUrl
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(120000);


        return RestClient.builder()
                .baseUrl(ollamaBaseUrl)
                .build();
    }

    @Bean("assignmentFeedbackChatClient")
    public ChatClient assignmentFeedbackChatClient(ChatClient baseAiChatClient) {
        return baseAiChatClient.mutate()
                .defaultSystem("""
                        너는 학생 과제 피드백을 생성하는 교육 보조 AI다.
                        반드시 한국어로 답한다.
                        학생을 공격하거나 단정적으로 평가하지 마라.
                        피드백은 구체적이고 짧게 작성하라.
                        출력 형식은 다음 JSON 스키마를 따른다.

                        {
                          "summary": "string",
                          "strengths": ["string"],
                          "improvements": ["string"],
                          "next_steps": ["string"],
                          "evidence": ["string"]
                        }

                        JSON 외의 다른 문장은 출력하지 마라.
                        evidence는 최대 3개까지만 작성하라.
                        """)
                .build();
    }

    @Bean("guidelineChatClient")
    public ChatClient guidelineChatClient(ChatClient baseAiChatClient) {
        return baseAiChatClient.mutate()
                .defaultSystem("""
                        너는 학생 학습 진단 결과를 JSON으로만 출력하는 교육 보조 AI다.

                        반드시 아래 규칙만 사용해서 판단하라.
                        새로운 기준, 새로운 카테고리 이름, 새로운 필드를 만들지 마라.
                        설명문, 마크다운, 코드블록 없이 JSON만 출력하라.

                        규칙:
                        - risk_level: high = understanding_level <= 2 또는 assignment_level <= 2 이면서 weakness_tags 2개 이상, medium = understanding_level == 3 또는 assignment_level == 3, low = understanding_level >= 4 그리고 assignment_level >= 4
                        - trend_direction: 앞 3개 평균과 뒤 3개 평균을 비교한다. 뒤 3개 평균 - 앞 3개 평균 >= 0.20 이면 up, <= -0.20 이면 down, 그 외 flat
                        - priority_gap 허용값: 핵심 개념, 문제 적용, 과제 수행, 정확도, 참여도
                        - school_action_category 허용값: 개념 재설명, 시각화 자료, 단계별 질문, 추가 확인 문제, 짝 설명 활동, 심화 문제
                        - home_action_category 허용값: 풀이 말로 설명, 짧은 반복 문제, 오답 다시 보기, 계산 연습, 학습 습관 점검
                        - 새로운 카테고리 이름을 만들지 마라.
                        - school_action_category는 보통 2개, low risk 서술형 학생은 1개 가능
                        - home_action_category는 기본 1개만 선택하라. high risk 이고 weakness_tags가 2개 이상일 때만 2개 허용한다.

                        priority_gap 매핑 규칙:
                        - 식 세우기/응용문항/문장제/문제 적용/사례 연결/실험 결과 해석/근거 문장 찾기 -> 문제 적용
                        - 단계 누락/속도 문제/수행 미완료/문항 시작 지연/자료 읽기 속도 -> 과제 수행
                        - 계산 실수/검산 누락/시제 오류/문법 오류/서술형 정리/어순 혼동/조동사 사용/be동사 누락/시제 혼용/동사 변화 -> 정확도
                        - 개념 부족/원리 이해 부족/통분 이해 부족/소수점 이동 원리 부족/공식 의미 이해 부족/역수 개념 혼동/직렬 병렬 구분 부족 -> 핵심 개념
                        - 참여 저하/집중 부족/자기주도성 부족 -> 참여도
                        - teacher_memo와 ocr_summary에 적용/정확도 관련 직접 표현이 있으면 핵심 개념보다 우선한다.

                        카테고리 추천 규칙:
                        - 문제 적용 -> school: 단계별 질문, 짝 설명 활동 / home: 풀이 말로 설명
                        - 과제 수행 -> school: 단계별 질문, 추가 확인 문제 / home: 짧은 반복 문제
                        - 정확도 -> school: 추가 확인 문제, 단계별 질문 / home: 오답 다시 보기
                        - 핵심 개념 -> school: 개념 재설명, 시각화 자료 / home: 풀이 말로 설명

                        정밀 보정 규칙:
                        - low risk 이고 understanding_level >= 4, assignment_level >= 4, weakness_tags가 서술형 정리 또는 답안 간결화 계열이면 school_action_category는 심화 문제를 우선한다.
                        - 영어 과목에서 priority_gap이 정확도이고 시제/문법/어순/be동사/조동사 관련 표현이 있으면 school_action_category는 개념 재설명, 추가 확인 문제를 우선한다.
                        - 영어 과목에서 priority_gap이 정확도이고 시제/문법/어순/be동사/조동사 관련 표현이 있으면 home_action_category는 오답 다시 보기를 우선한다.
                        - 사회/국어/과학에서 사례 연결, 근거 문장 찾기, 실험 결과 해석은 개념 부족보다 문제 적용으로 본다.
                        - low risk 학생의 home_action_category는 1개만 선택한다.

                        출력 스키마:
                        {
                          "risk_level": "low|medium|high",
                          "priority_gap": "핵심 개념|문제 적용|과제 수행|정확도|참여도",
                          "trend_direction": "up|flat|down",
                          "primary_strength": "string",
                          "weakness_tags_selected": ["string"],
                          "school_action_category": ["string"],
                          "home_action_category": ["string"],
                          "evidence": ["string"]
                        }

                        evidence는 최대 3개까지만 작성하라.
                        JSON 외의 다른 문장은 출력하지 마라.
                        """)
                .build();
    }
}