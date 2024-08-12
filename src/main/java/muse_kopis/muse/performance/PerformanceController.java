package muse_kopis.muse.performance;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.performance.dto.PerformanceRequest;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostConstruct
    public void init() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 1; i < 101; i++) {
            final int currentPage = i;
            log.info(String.valueOf(currentPage));
            executorService.submit(() -> performanceService.fetchPerformances("20180101","20241231", String.valueOf(currentPage), "100", "02", "GGGA"));
            executorService.submit(() -> performanceService.fetchPerformances("20180101", "20241231", String.valueOf(currentPage), "100", "03","GGGA"));
            executorService.submit(() -> performanceService.fetchPerformances("20180101", "20241231", String.valueOf(currentPage), "100", "01", "GGGA"));
        }
        executorService.shutdown();
    }

    @GetMapping("/kopis/performances")
    public ResponseEntity<Void> getPerformances(@ModelAttribute PerformanceRequest performanceRequest) {
        performanceService.fetchPerformances(performanceRequest.startDate(), performanceRequest.endDate(), performanceRequest.currentPage(),
                performanceRequest.rows(), performanceRequest.state(), performanceRequest.genre());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/performances")
    public ResponseEntity<List<PerformanceResponse>> searchPerformance(@RequestParam String search) {
        return ResponseEntity.ok().body(performanceService.findAllPerformanceBySearch(search));
    }
}
