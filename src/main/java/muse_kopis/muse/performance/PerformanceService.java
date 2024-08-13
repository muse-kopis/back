package muse_kopis.muse.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.common.FetchFailException;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.performance.dto.Boxofs;
import muse_kopis.muse.performance.dto.Boxofs.Boxof;
import muse_kopis.muse.performance.dto.DBS;
import muse_kopis.muse.performance.dto.DBS.DB;
import muse_kopis.muse.performance.dto.DBSDetail;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import muse_kopis.muse.performance.dto.PopularPerformanceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PerformanceService {

    @Value("${KOPIS_API_KEY}")
    private String kopisKey;
    private final String API_URL = "http://www.kopis.or.kr/openApi/restful/pblprfr";
    private final String API_URL_BOX_OFFICE = "http://kopis.or.kr/openApi/restful/boxoffice";
    private final PerformanceRepository performanceRepository;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;


    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public List<PerformanceResponse> findAllPerformance(String state){
        return performanceRepository.findAllByState(state)
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
            log.info("저장 완료 {}, {}", currentPage, state);
        } catch (Exception e) {
            throw new FetchFailException("데이터 패치에 실패했습니다.");
        }
    }

    public List<PopularPerformanceResponse> fetchPopularPerformance(String type, String date, String genre) {
        String url = API_URL_BOX_OFFICE + "?service=" + kopisKey + "&ststype=" + type + "&date=" + date + "&catecode=" + genre;
        String response = restTemplate.getForObject(url, String.class);
        try {
            Boxofs boxofs = xmlMapper.readValue(response, Boxofs.class);
            List<Boxof> boxofList = boxofs.boxof()
                    .stream()
                    .limit(10)
                    .toList();
            log.info(boxofList.getFirst().mt20id());
            return boxofList.stream()
                    .map(performance -> PopularPerformanceResponse.builder()
                            .venue(performance.prfplcnm())
                            .rank(performance.rnum())
                            .poster(performance.poster())
                            .performancePeriod(performance.prfpd())
                            .performanceName(performance.prfnm())
                            .performanceCount(performance.prfdtcnt())
                            .area(performance.area())
                            .build()
                    ).collect(Collectors.toList());
        } catch (Exception e) {
            throw new NotFoundPerformanceException("공연을 찾을 수 없습니다.");
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
        }
    }

    private DBSDetail fetchPerformanceDetail(String performanceId) throws JsonProcessingException {
        String url = API_URL + "/" + performanceId +"?service="+kopisKey;
        String response = restTemplate.getForObject(url, String.class);
        return xmlMapper.readValue(response, DBSDetail.class);
    }
}
