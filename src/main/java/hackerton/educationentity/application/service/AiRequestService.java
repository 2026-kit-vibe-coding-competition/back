package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateAiRequestRequest;
import hackerton.educationentity.application.dto.response.AiRequestResponse;
import hackerton.educationentity.domain.ai_request.entity.AiRequest;
import hackerton.educationentity.domain.ai_request.repository.AiRequestRepository;
import hackerton.educationentity.domain.ai_request.type.AiRequestStatus;
import hackerton.educationentity.domain.ai_request.type.AiRequestType;
import hackerton.educationentity.domain.feedback_report.entity.FeedbackReport;
import hackerton.educationentity.domain.feedback_report.repository.FeedbackReportRepository;
import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.repository.GuidelineRepository;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;
import hackerton.educationentity.domain.session.entity.Session;
import hackerton.educationentity.domain.session.repository.SessionRepository;
import hackerton.educationentity.domain.student.entity.Student;
import hackerton.educationentity.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiRequestService {
    private final AiRequestRepository aiRequestRepository;
    private final FeedbackReportRepository feedbackReportRepository;
    private final GuidelineRepository guidelineRepository;
    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public AiRequestResponse createAiRequest(CreateAiRequestRequest request) {
        Session session = getSession(request.sessionId());
        Student student = getStudent(request.studentId());
        validateClassroomIntegrity(session, student);

        Map<String, Object> inputPayload = new HashMap<>();
        inputPayload.put("dataRef", request.dataRef());
        inputPayload.put("understandingLevel", request.understandingLevel());
        inputPayload.put("assignmentLevel", request.assignmentLevel());
        inputPayload.put("weaknessTags", request.weaknessTags());
        inputPayload.put("teacherMemo", request.teacherMemo());
        inputPayload.put("ocrSummary", request.ocrSummary());
        inputPayload.put("trend", request.trend());

        Map<String, Object> ragContext = Map.of(
                "subject", session.getSubject().getName(),
                "lessonTopic", session.getTitle()
        );

        Map<String, Object> parsedResponse = Map.of(
                "priorityGap", derivePriorityGap(request.weaknessTags()),
                "summary", buildSummary(request.understandingLevel(), request.assignmentLevel(), request.teacherMemo()),
                "nextStep", buildNextStep(request.weaknessTags())
        );

        AiRequest aiRequest = aiRequestRepository.save(AiRequest.builder()
                .jobId(UUID.randomUUID().toString())
                .student(student)
                .session(session)
                .requestType(AiRequestType.ANALYSIS)
                .modelName("stub-model")
                .promptVersion("v1")
                .inputPayload(inputPayload)
                .ragContext(ragContext)
                .rawResponse(Map.of("mode", "stub", "status", "generated"))
                .parsedResponse(parsedResponse)
                .status(AiRequestStatus.SUCCESS)
                .latencySeconds(new BigDecimal("0.001"))
                .promptEvalCount(0)
                .evalCount(0)
                .errorMessage(null)
                .build());

        FeedbackReport feedbackReport = feedbackReportRepository.save(FeedbackReport.builder()
                .student(student)
                .session(session)
                .aiRequest(aiRequest)
                .studentName(student.getName())
                .subject(session.getSubject().getName())
                .lessonTopic(session.getTitle())
                .understandingLevel(request.understandingLevel())
                .assignmentLevel(request.assignmentLevel())
                .weaknessTags(request.weaknessTags())
                .teacherMemo(request.teacherMemo())
                .ocrSummary(request.ocrSummary())
                .trend(request.trend())
                .build());

        Guideline guideline = guidelineRepository.save(Guideline.builder()
                .student(student)
                .session(session)
                .aiRequest(aiRequest)
                .priorityGap(derivePriorityGap(request.weaknessTags()))
                .summary(buildSummary(request.understandingLevel(), request.assignmentLevel(), request.teacherMemo()))
                .strength(buildStrength(request.assignmentLevel(), request.teacherMemo()))
                .improvement(buildImprovement(request.weaknessTags(), request.ocrSummary()))
                .nextStep(buildNextStep(request.weaknessTags()))
                .schoolAction("???濚밸Ŧ援????癲ル슢??????????????????ル봿??????轅붽틓????レ????됀????癲ル슢?®쳥?껊닱?????롪퍓肉???????댟?????筌???鶯ㅺ동??筌믡룓愿???鶯ㅺ동???醫듽렒 ?????살퓢???")
                .homeAction("?轅붽틓???寃멸섶?誘↔틒???癲ル슢??????轅붽틓????レ?? ???ㅼ뒧?戮ル탶?????筌????2~3???ル봿??誘⑸쿋??λ쑏????ル봿??? ?????ㅻ쿋?????ш끽維뽳쭩????? ?????썹땟?⑤┛???꿔꺂??틝??????癲ル슢?????")
                .nextCheck("???濚밸Ŧ援????癲ル슢???????꿔꺂??????????????⑤베???????援?????筌????????????????ㅻ쿋????꿔꺂??틝??????癲ル슢?????")
                .audience(GuidelineAudience.TEACHER)
                .status(GuidelineStatus.DRAFT)
                .teacherReviewNote(null)
                .build());

        return new AiRequestResponse(
                aiRequest.getId(),
                session.getId(),
                student.getId(),
                aiRequest.getJobId(),
                aiRequest.getRequestType(),
                aiRequest.getStatus(),
                feedbackReport.getId(),
                guideline.getId()
        );
    }

    public AiRequestResponse getAiRequest(Long id) {
        AiRequest aiRequest = aiRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AiRequest not found"));
        Long feedbackReportId = feedbackReportRepository.findByAiRequestId(id).map(FeedbackReport::getId).orElse(null);
        Long guidelineId = guidelineRepository.findByAiRequestId(id).map(Guideline::getId).orElse(null);

        return new AiRequestResponse(
                aiRequest.getId(),
                aiRequest.getSession().getId(),
                aiRequest.getStudent().getId(),
                aiRequest.getJobId(),
                aiRequest.getRequestType(),
                aiRequest.getStatus(),
                feedbackReportId,
                guidelineId
        );
    }

    private Session getSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private void validateClassroomIntegrity(Session session, Student student) {
        if (!session.getSubject().getClassroom().getId().equals(student.getClassroom().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student classroom does not match session classroom");
        }
    }

    private String derivePriorityGap(List<String> weaknessTags) {
        return weaknessTags != null && !weaknessTags.isEmpty() ? weaknessTags.get(0) : "???????????ル봿???????";
    }

    private String buildSummary(Integer understandingLevel, Integer assignmentLevel, String teacherMemo) {
        return "??????筌?? ?????썹땟???" + understandingLevel + "??鶯ㅺ동?????? ??潁?????????彛??" + assignmentLevel + "??鶯ㅺ동????????????????????ㅼ뒧??????ы꺍?? ?????밸븶???轅붽틓????????????? ????????ㅼ뒧??????????밸븶???癲ル슢????? " + teacherMemo;
    }

    private String buildStrength(Integer assignmentLevel, String teacherMemo) {
        return assignmentLevel != null && assignmentLevel >= 3
                ? "??潁?????????彛?????????????????????????곕춴???れ뫊鸚?????蹂κ텤?????????????낆젵. " + teacherMemo
                : "???????????筌?????????????곕춴?????筌뤾쑵??????蹂κ텤?????ㅼ뒧??????ы꺍???????甕???????????ш끽維뽳쭩??????롪퍓肉?????????????낆젵.";
    }

    private String buildImprovement(List<String> weaknessTags, String ocrSummary) {
        List<String> tags = (weaknessTags == null || weaknessTags.isEmpty())
                ? List.of("basic concept")
                : weaknessTags;
        return "Areas for improvement: " + String.join(", ", tags) + ". " + ocrSummary;
    }

    private String buildNextStep(List<String> weaknessTags) {
        return "???濚밸Ŧ援????癲ル슢??????????" + derivePriorityGap(weaknessTags) + " ?μ떝?띄몭??袁㏉떋??????????轅붽틓????レ?? ?????쑩?댆?뷂펲????몃뒆????꿔꺂??틝????????筌???鶯ㅺ동??筌믡룓愿??轅붽틓????筌뤾쑴???癲ル슢?????";
    }
}
