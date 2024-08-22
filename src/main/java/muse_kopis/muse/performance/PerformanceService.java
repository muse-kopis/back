package muse_kopis.muse.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.common.FetchFailException;
import muse_kopis.muse.common.NotFoundPerformanceException;
import muse_kopis.muse.performance.castmember.CastMember;
import muse_kopis.muse.performance.castmember.CastMemberRepository;
import muse_kopis.muse.performance.dto.Boxofs;
import muse_kopis.muse.performance.dto.Boxofs.Boxof;
import muse_kopis.muse.performance.dto.KOPISPerformanceDetailResponse.Detail;
import muse_kopis.muse.performance.dto.KOPISPerformanceResponse;
import muse_kopis.muse.performance.dto.KOPISPerformanceResponse.DB;
import muse_kopis.muse.performance.dto.KOPISPerformanceDetailResponse;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import org.apache.commons.text.similarity.LevenshteinDistance;
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
    private final CastMemberRepository castMemberRepository;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    private final static String CURRENT = "공연중";
    private final static String UPCOMING = "공연예정";
    private final static String BLANK_OR_COMMA = "[,\\s]+";
    public static final String BLANK_OR_PARENTHESIS = "[\\s()]";

    public PerformanceService(PerformanceRepository performanceRepository, CastMemberRepository castMemberRepository) {
        this.performanceRepository = performanceRepository;
        this.castMemberRepository = castMemberRepository;
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    public PerformanceResponse findById(Long performanceId) {
        return PerformanceResponse.from(performanceRepository.getByPerformanceId(performanceId));
    }

    public List<PerformanceResponse> findAllPerformance(String state){
        return performanceRepository.findAllByState(state)
                .stream()
                .map(PerformanceResponse::from)
                .collect(Collectors.toList());
    }

    public List<PerformanceResponse> findAllPerformanceBySearch(String search) {
        return performanceRepository.findAllByPerformanceNameContains(search)
                .stream()
                .map(PerformanceResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void fetchPerformances(String startDate, String endDate, String currentPage, String rows, String state, String genre)
            throws JsonProcessingException {
        String url = API_URL + "?service=" + kopisKey + "&stdate=" + startDate + "&eddate=" + endDate +
                "&cpage=" + currentPage + "&rows=" + rows + "&prfstate=" + state+"&shcate=" + genre;
        log.info("데이터 요청 {}, {}", currentPage, state);
        String response = restTemplate.getForObject(url, String.class);
        try {
            KOPISPerformanceResponse KOPISPerformanceResponse = xmlMapper.readValue(response, KOPISPerformanceResponse.class);
            saveAllPerformance(KOPISPerformanceResponse);
            log.info("저장 완료 {}, {}", currentPage, state);
        } catch (Exception e) {
            throw new FetchFailException("데이터 패치에 실패했습니다.");
        }
    }

    private void saveAllPerformance(KOPISPerformanceResponse KOPISPerformanceResponse) throws JsonProcessingException {
        List<DB> list = Optional.ofNullable(KOPISPerformanceResponse.db()).orElse(Collections.emptyList());
        for (DB db : list) {
            String performanceId = db.mt20id();
            KOPISPerformanceDetailResponse performanceResponse = fetchPerformanceDetail(performanceId);
            if(performanceResponse.detail().isEmpty()) {
                return;
            }
            Detail performanceDetail = performanceResponse.detail().getFirst();
            Performance performance = Performance.from(performanceDetail);
            performanceRepository.save(performance);
            List<CastMember> castMembers = Arrays.stream(performanceDetail.crews().split(BLANK_OR_COMMA))
                    .map(String::trim)
                    .map(name -> name.endsWith("등") ? name.substring(0, name.length() - 1).trim() : name)
                    .filter(name -> !name.isEmpty())  // 빈 문자열 필터링
                    .map(name -> new CastMember(name,performance))
                    .toList();
            castMemberRepository.saveAll(castMembers);
        }
    }

    public List<PerformanceResponse> fetchPopularPerformance(String type, String date, String genre) {
        String url = API_URL_BOX_OFFICE + "?service=" + kopisKey + "&ststype=" + type + "&date=" + date + "&catecode=" + genre;
        String response = restTemplate.getForObject(url, String.class);
        try {
            Boxofs boxofs = xmlMapper.readValue(response, Boxofs.class);
            List<Boxof> boxofList = boxofs.boxof()
                    .stream()
                    .limit(10)
                    .toList();
            LevenshteinDistance levenshtein = new LevenshteinDistance();
            List<Performance> collect = boxofList.stream().map(it -> performanceRepository.findAllByStateOrState(CURRENT, UPCOMING).stream()
                            .filter(p -> p.getPerformanceName().equals(it.prfnm())
                                    && levenshtein.apply(p.getVenue().replaceAll(BLANK_OR_PARENTHESIS, ""), it.prfplcnm().replaceAll(BLANK_OR_PARENTHESIS, "")) <= 5)
                            .findFirst())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return collect.stream().map(PerformanceResponse::from).collect(Collectors.toList());
        } catch (Exception e) {
            throw new NotFoundPerformanceException("공연을 찾을 수 없습니다.");
        }
    }

    private KOPISPerformanceDetailResponse fetchPerformanceDetail(String performanceId) throws JsonProcessingException {
        String url = API_URL + "/" + performanceId +"?service="+kopisKey;
        String response = restTemplate.getForObject(url, String.class);
        return xmlMapper.readValue(response, KOPISPerformanceDetailResponse.class);
    }
}
