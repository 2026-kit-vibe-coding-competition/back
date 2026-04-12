package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateReportJobsRequest;
import hackerton.educationentity.application.dto.response.ReportJobStatusResponse;
import hackerton.educationentity.application.dto.response.ReportJobsCreateResponse;
import hackerton.educationentity.application.service.ReportJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report-jobs")
public class ReportJobController {
    private final ReportJobService reportJobService;

    @PostMapping
    public ReportJobsCreateResponse createReportJobs(@RequestBody CreateReportJobsRequest request) {
        return reportJobService.createReportJobs(request);
    }

    @GetMapping("/{jobId}")
    public ReportJobStatusResponse getReportJobStatus(@PathVariable String jobId) {
        return reportJobService.getReportJobStatus(jobId);
    }
}
