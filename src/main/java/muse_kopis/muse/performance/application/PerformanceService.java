package muse_kopis.muse.performance.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Set;

public interface PerformanceService {

    @Transactional
    PerformanceResponse findById(Long performanceId);
    @Transactional
    List<PerformanceResponse> findAllPerformance();
    @Transactional
    List<PerformanceResponse> findAllPerformanceBySearch(String search);
    @Transactional
    List<PerformanceResponse> recommendPerformance(Long memberId);
    @Transactional
    Set<PerformanceResponse> getRandomPerformance(Long memberId);
    @Transactional
    ByteArrayResource getPosterImage(Long performanceId);
    @Transactional
    List<PerformanceResponse> fetchPopularPerformance();
    @Transactional
    void fetchPerformances(String startDate, String endDate, String currentPage, String rows, String state, String genre) throws JsonProcessingException;
}
