package muse_kopis.muse.performance.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.oauth.domain.OauthMember;
import muse_kopis.muse.auth.oauth.domain.OauthMemberRepository;
import muse_kopis.muse.common.api.FetchFailException;
import muse_kopis.muse.common.performance.NotFoundPerformanceException;
import muse_kopis.muse.performance.domain.Performance;
import muse_kopis.muse.performance.domain.PerformanceRepository;
import muse_kopis.muse.actor.domain.CastMember;
import muse_kopis.muse.actor.domain.CastMemberRepository;
import muse_kopis.muse.performance.domain.dto.Boxofs;
import muse_kopis.muse.performance.domain.dto.Boxofs.Boxof;
import muse_kopis.muse.performance.domain.dto.KOPISPerformanceDetailResponse.Detail;
import muse_kopis.muse.performance.domain.dto.KOPISPerformanceResponse;
import muse_kopis.muse.performance.domain.dto.KOPISPerformanceResponse.DB;
import muse_kopis.muse.performance.domain.dto.KOPISPerformanceDetailResponse;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.genre.domain.Genre;
import muse_kopis.muse.genre.domain.GenreRepository;
import muse_kopis.muse.genre.domain.GenreType;
import muse_kopis.muse.usergenre.domain.UserGenre;
import muse_kopis.muse.usergenre.domain.UserGenreRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@EnableScheduling
public class PerformanceService {

    @Value("${KOPIS_API_KEY}")
    private String kopisKey;

    @Value("${API_URL}")
    private String API_URL;

    @Value("${API_URL_BOX_OFFICE}")
    private String API_URL_BOX_OFFICE;

    private final PerformanceRepository performanceRepository;
    private final CastMemberRepository castMemberRepository;
    private final UserGenreRepository userGenreRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final GenreRepository genreRepository;
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    private final static String CURRENT = "공연중";
    private final static String UPCOMING = "공연예정";
    private final static String COMPLETE = "공연완료";
    private final static String GENRE = "GGGA";
    private final static String BLANK_OR_COMMA = "[,\\s]+";
    public static final String BLANK_OR_PARENTHESIS = "[\\s()]";

    public PerformanceService(
            PerformanceRepository performanceRepository,
            CastMemberRepository castMemberRepository,
            UserGenreRepository userGenreRepository,
            OauthMemberRepository oauthMemberRepository,
            GenreRepository genreRepository
    ) {
        this.performanceRepository = performanceRepository;
        this.castMemberRepository = castMemberRepository;
        this.userGenreRepository = userGenreRepository;
        this.oauthMemberRepository = oauthMemberRepository;
        this.genreRepository = genreRepository;
        this.restTemplate = new RestTemplate();
        this.xmlMapper = new XmlMapper();
    }

    @Transactional
    public PerformanceResponse findById(Long performanceId) {
        return PerformanceResponse.from(performanceRepository.getByPerformanceId(performanceId));
    }

    @Transactional
    public List<PerformanceResponse> findAllPerformance(){
        return performanceRepository.findPerformancesByDate(LocalDate.now())
                .stream()
                .map(PerformanceResponse::from)
                .limit(7)
                .collect(Collectors.toList());
    }

    @Transactional
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
                    .map(name -> new CastMember(name.replace("\"",""), "", performance))
                    .toList();
            castMemberRepository.saveAll(castMembers);
        }
    }   // 동명이인 방지를 위한 actorId 생김에 따른 수정 필요

    @Transactional
    public List<PerformanceResponse> fetchPopularPerformance() {
        String url = API_URL_BOX_OFFICE + "?service=" + kopisKey + "&stdate="+ LocalDate.now().minusDays(31).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+ "&eddate=" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&catecode=" + GENRE;
        log.info("url = {}", url);
        String response = restTemplate.getForObject(url, String.class);
        log.info("{}", LocalDate.now());
        try {
            Boxofs boxofs = xmlMapper.readValue(response, Boxofs.class);
            List<Boxof> boxofList = boxofs.boxof()
                    .stream()
                    .limit(7)
                    .toList();
            LevenshteinDistance levenshtein = new LevenshteinDistance();
            List<Performance> collect = boxofList.stream().map(it -> performanceRepository.findAllByStateOrState(CURRENT, UPCOMING).stream()
                            .filter(p -> p.getPerformanceName().equals(it.prfnm())
                                    && levenshtein.apply(p.getVenue().replaceAll(BLANK_OR_PARENTHESIS, ""), it.prfplcnm().replaceAll(BLANK_OR_PARENTHESIS, "")) <= 5)
                            .findFirst())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            log.info("{}", collect.size());
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

    @Transactional
    public List<PerformanceResponse> recommendPerformance(Long memberId) {
        OauthMember oauthMember = oauthMemberRepository.getByOauthMemberId(memberId);
        UserGenre userGenre = userGenreRepository.getUserGenreByOauthMember(oauthMember);
        GenreType favorite = userGenre.favorite();
        GenreType second = userGenre.second();
        GenreType third = userGenre.third();
        List<Performance> result = performanceRepository.findAllByGenreType(favorite).stream()
                .distinct()
                .filter(performance -> performance.getState().equals(CURRENT))
                .collect(Collectors.toList());
        fillPerformanceList(result, second);
        fillPerformanceList(result, third);
        return result.stream()
                .map(PerformanceResponse::from)
                .limit(7)
                .toList();
    }

    private void fillPerformanceList(List<Performance> result, GenreType genre) {
        List<Performance> performances = performanceRepository.findAllByGenreType(genre);
        if (result.size() < 7 && !performances.isEmpty()) {
            result.addAll(performances.stream()
                    .distinct()
                    .filter(p -> p.getState().equals(CURRENT))
                    .limit(7 - result.size())
                    .toList());
        }
    }

    @Transactional
    public Set<PerformanceResponse> getRandomPerformance(Long memberId) {
        oauthMemberRepository.getByOauthMemberId(memberId);
        List<Performance> performances = performanceRepository.findAllByState(CURRENT);
        Set<PerformanceResponse> responses = new HashSet<>();
        while (responses.size() < 7){
            if (!performances.isEmpty()) {
                Random random = new Random();
                responses.add(PerformanceResponse.from(performances.get(random.nextInt(performances.size()))));
            }
        }
        return responses;
    }

    public ByteArrayResource getPosterImage(Long performanceId) {
        Performance performance = performanceRepository.getByPerformanceId(performanceId);
        String url = performance.getPoster();
        try {
            byte[] imageByte = restTemplate.getForObject(url, byte[].class);
            return new ByteArrayResource(imageByte);
        } catch (Exception e) {
            throw new FetchFailException("이미지를 불러오지 못했습니다.");
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 2 ? * SUN", zone = "Asia/Seoul")
    public void performanceStateUpdate() {
        List<Performance> performances = performanceRepository.findAllByState(CURRENT);
        performances.forEach(performance -> {
            if(performance.getEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                performance.updateState(COMPLETE);
            }
        });
        performanceRepository.saveAll(performances);
    }
}
