package muse_kopis.muse.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.performance.DBS.DB;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PerformanceService {

    @Value("${KOPIS_API_KEY}")
    private String kopisKey;
    private final String API_URL = "http://www.kopis.or.kr/openApi/restful/pblprfr";
    private final PerformanceRepository performanceRepository;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;


    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public List<PerformanceResponse> findAllPerformanceBySearch(String search) {
        return performanceRepository.findAllByPerformanceNameContains(search)
                .orElseThrow(() -> new NotFoundPerformanceException("공연을 찾지 못했습니다."))
                .stream()
                .map(performance -> PerformanceResponse.builder()
                        .performanceName(performance.getPerformanceName())
                        .startDate(performance.getStartDate())
                        .endDate(performance.getEndDate())
                        .venue(performance.getVenue())
                        .poster(performance.getPoster())
                        .performanceTime(performance.getPerformanceTime())
                        .limitAge(performance.getLimitAge())
                        .performanceCrews(performance.getPerformanceCrews())
                        .entertainment(performance.getEntertainment())
                        .build()).collect(Collectors.toList());
    }
    public void fetchPerformances(String startDate, String endDate, String currentPage, String rows, String state, String genre) {
        String url = API_URL + "?service=" + kopisKey + "&stdate=" + startDate + "&eddate=" + endDate +
                "&cpage=" + currentPage + "&rows=" + rows + "&prfstate=" + state+"&shcate=" + genre;
        log.info("데이터 요청 {}, {}", currentPage, state);
        String response = restTemplate.getForObject(url, String.class);
        try {
            DBS dbs = xmlMapper.readValue(response, DBS.class);
            saveAllPerformance(dbs);
            log.info("저장 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAllPerformance(DBS dbs) throws JsonProcessingException {
        List<DB> list = Optional.ofNullable(dbs.db()).orElse(Collections.emptyList());
        for (DB db : list) {
            String performanceId = db.mt20id();
            DBSDetail performance = fetchPerformanceDetail(performanceId);
            if(performance.db().isEmpty()) {
                return;
            }
            DBSDetail.DB performanceDetail = performance.db().getFirst();
            performanceRepository.save(Performance.builder()
                            .performanceTime(performanceDetail.performanceTime())
                            .price(performanceDetail.price())
                            .poster(performanceDetail.poster())
                            .performanceTime(performanceDetail.performanceTime())
                            .performanceName(performanceDetail.performanceName())
                            .venue(performanceDetail.venue())
                            .startDate(LocalDate.parse(performanceDetail.startDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .endDate(LocalDate.parse(performanceDetail.endDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .limitAge(performanceDetail.limitAge())
                            .entertainment(performanceDetail.entertainment())
                            .performanceCrews(performanceDetail.crews())
                            .state(performanceDetail.state())
                    .build());
            log.info(performanceDetail.state());
        }
    }

    private DBSDetail fetchPerformanceDetail(String performanceId) throws JsonProcessingException {
        String url = API_URL + "/" + performanceId +"?service="+kopisKey;
        String response = restTemplate.getForObject(url, String.class);
        return xmlMapper.readValue(response, DBSDetail.class);
    }
}
