package muse_kopis.muse.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.performance.dto.PerformanceRequest;
import muse_kopis.muse.performance.dto.PerformanceResponse;
import muse_kopis.muse.performance.dto.PopularPerformanceRequest;
import muse_kopis.muse.performance.dto.PopularPerformanceResponse;
import muse_kopis.muse.performance.genre.GenreService;
import muse_kopis.muse.performance.genre.GenreType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;
    private final GenreService genreService;

    @GetMapping("/performances/kopis")
    public ResponseEntity<Void> getPerformances(@ModelAttribute PerformanceRequest performanceRequest)
            throws JsonProcessingException {
        performanceService.fetchPerformances(performanceRequest.startDate(), performanceRequest.endDate(), performanceRequest.currentPage(),
                performanceRequest.rows(), performanceRequest.state(), performanceRequest.genre());
        return ResponseEntity.ok().build();
    } // KOPIS 전체 공연 목록

    @GetMapping("/performances/search")
    public ResponseEntity<List<PerformanceResponse>> searchPerformance(@RequestParam String search) {
        return ResponseEntity.ok().body(performanceService.findAllPerformanceBySearch(search));
    } // 검색어를 통한 공연 목록

    @GetMapping("/performances/state")
    public ResponseEntity<List<PerformanceResponse>> getPerformance(@RequestParam String state) {
        return ResponseEntity.ok().body(performanceService.findAllPerformance(state));
    } // 공연 목록

    @GetMapping("/performances/popular")
    public ResponseEntity<List<PopularPerformanceResponse>> getPerformancePopular(@ModelAttribute PopularPerformanceRequest popularPerformanceRequest) {
        return ResponseEntity.ok().body(performanceService.fetchPopularPerformance(popularPerformanceRequest.type(), popularPerformanceRequest.date(),
                popularPerformanceRequest.genre()));
    } // 인기있는 공연 목록

//    @PostConstruct
    public void init() {
        log.info("Initialization started");
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 1; i < 10; i++) {
            final int currentPage = i;
            log.info("Processing page: " + currentPage);
            if (i < 3) {
                executorService.submit(() -> {
                    try {
                        performanceService.fetchPerformances("20180101", "20241231",
                                String.valueOf(currentPage), "100", "02", "GGGA");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (i < 9) {
                executorService.submit(() -> {
                    try {
                        performanceService.fetchPerformances("20180101", "20241231",
                                String.valueOf(currentPage), "200", "03", "GGGA");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (i < 5) {
                executorService.submit(() -> {
                    try {
                        performanceService.fetchPerformances("20180101", "20241231",
                                String.valueOf(currentPage), "100", "01", "GGGA");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        log.info("Initialization ended");
    }

//    @GetMapping("/genres")
    public void saveGenres() {
        genreService.saveGenre("진짜나쁜소녀", GenreType.CRIME);
        genreService.saveGenre("진짜나쁜소녀", GenreType.THRILLER);

        genreService.saveGenre("피터팬 [광주]", GenreType.FAIRY_TALE);
        genreService.saveGenre("피터팬 [광주]", GenreType.FANTASY);

        genreService.saveGenre("두들팝 Ver.2, 폴리팝 [분당]", GenreType.FAMILY);
        genreService.saveGenre("두들팝 Ver.2, 폴리팝 [분당]", GenreType.FANTASY);

        genreService.saveGenre("아기 오리의 모험 [청주]", GenreType.ADVENTURE);
        genreService.saveGenre("아기 오리의 모험 [청주]", GenreType.FANTASY);

        genreService.saveGenre("연남동 빙굴빙굴 빨래방", GenreType.HEALING);

        genreService.saveGenre("인어공주 [창원]", GenreType.FAIRY_TALE);
        genreService.saveGenre("인어공주 [창원]", GenreType.ROMANCE);
        genreService.saveGenre("인어공주 [창원]", GenreType.FANTASY);

        genreService.saveGenre("엄마를 찾아라 [김포]", GenreType.CHILD);

        genreService.saveGenre("미운아기오리 [여수]", GenreType.CHILD);

        genreService.saveGenre("미스터 래빗 [서울]", GenreType.ADVENTURE);
        genreService.saveGenre("미스터 래빗 [서울]", GenreType.FANTASY);

        genreService.saveGenre("어린왕자 [부산]", GenreType.PHILOSOPHY);
        genreService.saveGenre("어린왕자 [부산]", GenreType.FANTASY);

        genreService.saveGenre("시카고 [서울]", GenreType.DRAMA);
        genreService.saveGenre("시카고 [서울]", GenreType.CRIME);
        genreService.saveGenre("시카고 [서울]", GenreType.HISTORY);

        genreService.saveGenre("늑대와 빨간모자 [부천]", GenreType.FAIRY_TALE);
        genreService.saveGenre("늑대와 빨간모자 [부천]", GenreType.CHILD);

        genreService.saveGenre("정글북 [서울 송파]", GenreType.ADVENTURE);
        genreService.saveGenre("정글북 [서울 송파]", GenreType.FANTASY);

        genreService.saveGenre("셰프 [홍성]", GenreType.DRAMA);
        genreService.saveGenre("셰프 [홍성]", GenreType.COMEDY);

        genreService.saveGenre("아기돼지삼형제 & 개미와베짱이 [수원]", GenreType.FAIRY_TALE);
        genreService.saveGenre("아기돼지삼형제 & 개미와베짱이 [수원]", GenreType.CHILD);

        genreService.saveGenre("15주년 기념공연, 영웅", GenreType.DRAMA);
        genreService.saveGenre("15주년 기념공연, 영웅", GenreType.HISTORY);

        genreService.saveGenre("베어 더 뮤지컬", GenreType.ROMANCE);
        genreService.saveGenre("베어 더 뮤지컬", GenreType.ROCK);

        genreService.saveGenre("육영수, 그 시절의 아카시아 [대구]", GenreType.HISTORY);
        genreService.saveGenre("육영수, 그 시절의 아카시아 [대구]", GenreType.ACTION);

        genreService.saveGenre("셰프 [서울]", GenreType.DRAMA);
        genreService.saveGenre("셰프 [서울]", GenreType.COMEDY);

        genreService.saveGenre("6시 퇴근 [대학로]", GenreType.DRAMA);
        genreService.saveGenre("6시 퇴근 [대학로]", GenreType.OFFICE);

        genreService.saveGenre("신데렐라 [울산]", GenreType.FAIRY_TALE);
        genreService.saveGenre("신데렐라 [울산]", GenreType.ROMANCE);
        genreService.saveGenre("신데렐라 [울산]", GenreType.FANTASY);

        genreService.saveGenre("누가 내 머리에 똥 쌌어? [수원]", GenreType.CHILD);

        genreService.saveGenre("인어공주 [서울 금천]", GenreType.FAIRY_TALE);
        genreService.saveGenre("인어공주 [서울 금천]", GenreType.ROMANCE);
        genreService.saveGenre("인어공주 [서울 금천]", GenreType.FANTASY);

        genreService.saveGenre("옹알스 [대학로]", GenreType.COMEDY);

        genreService.saveGenre("백설공주 [대전]", GenreType.FAIRY_TALE);
        genreService.saveGenre("백설공주 [대전]", GenreType.ROMANCE);
        genreService.saveGenre("백설공주 [대전]", GenreType.FANTASY);

        genreService.saveGenre("스파이 엑스 [인천]", GenreType.ACTION);
        genreService.saveGenre("스파이 엑스 [인천]", GenreType.ADVENTURE);
        genreService.saveGenre("스파이 엑스 [인천]", GenreType.CHILD);

        genreService.saveGenre("제58회 극단 서울 정기공연, 오즈의 마법사", GenreType.FAIRY_TALE);
        genreService.saveGenre("제58회 극단 서울 정기공연, 오즈의 마법사", GenreType.ADVENTURE);
        genreService.saveGenre("제58회 극단 서울 정기공연, 오즈의 마법사", GenreType.FANTASY);

        genreService.saveGenre("이벤져스 라이브 [서울]", GenreType.CHILD);

        genreService.saveGenre("프린세스 공주뮤지컬쇼 [남양주]", GenreType.CHILD);
        genreService.saveGenre("프린세스 공주뮤지컬쇼 [남양주]", GenreType.COMEDY);
        genreService.saveGenre("프린세스 공주뮤지컬쇼 [남양주]", GenreType.PARODY);

        genreService.saveGenre("책 속에 갇힌 고양이", GenreType.ADVENTURE);
        genreService.saveGenre("책 속에 갇힌 고양이", GenreType.CHILD);

        genreService.saveGenre("뚜들뚜들 선사시대 [대구]", GenreType.FAMILY);
        genreService.saveGenre("뚜들뚜들 선사시대 [대구]", GenreType.NON_VERBAL);
        genreService.saveGenre("뚜들뚜들 선사시대 [대구]", GenreType.CHILD);

        genreService.saveGenre("페인터즈 [경향아트힐]", GenreType.NON_VERBAL);
        genreService.saveGenre("페인터즈 [경향아트힐]", GenreType.ART);
        genreService.saveGenre("페인터즈 [경향아트힐]", GenreType.PERFORMANCE);

        genreService.saveGenre("인어공주 [광주]", GenreType.FAIRY_TALE);
        genreService.saveGenre("인어공주 [광주]", GenreType.ROMANCE);
        genreService.saveGenre("인어공주 [광주]", GenreType.FANTASY);

        genreService.saveGenre("달 샤베트 [서울숲]", GenreType.ADVENTURE);
        genreService.saveGenre("달 샤베트 [서울숲]", GenreType.FANTASY);

        genreService.saveGenre("썸데이 [대학로]", GenreType.FAMILY);
        genreService.saveGenre("썸데이 [대학로]", GenreType.FANTASY);

        genreService.saveGenre("헨젤과 그레텔 [울산]", GenreType.FAIRY_TALE);
        genreService.saveGenre("헨젤과 그레텔 [울산]", GenreType.CHILD);

        genreService.saveGenre("뱀프 X 헌터: 울부짖어라! 피닉스 포포!!", GenreType.ROMANCE);
        genreService.saveGenre("뱀프 X 헌터: 울부짖어라! 피닉스 포포!!", GenreType.ACTION);
        genreService.saveGenre("뱀프 X 헌터: 울부짖어라! 피닉스 포포!!", GenreType.FANTASY);

        genreService.saveGenre("아기돼지삼형제: 늑대숲 또옹돼지 원정대 [대학로]", GenreType.CHILD);
        genreService.saveGenre("아기돼지삼형제: 늑대숲 또옹돼지 원정대 [대학로]", GenreType.INTERACTIVE);

        genreService.saveGenre("오즈의 마법사 [평택]", GenreType.FAIRY_TALE);
        genreService.saveGenre("오즈의 마법사 [평택]", GenreType.ADVENTURE);
        genreService.saveGenre("오즈의 마법사 [평택]", GenreType.FANTASY);

        genreService.saveGenre("인피니티 플라잉 [경주]", GenreType.NON_VERBAL);
        genreService.saveGenre("인피니티 플라잉 [경주]", GenreType.ACTION);
        genreService.saveGenre("인피니티 플라잉 [경주]", GenreType.FANTASY);

        genreService.saveGenre("핑크퐁과 아기상어의 무지개 구출 작전", GenreType.CHILD);
        genreService.saveGenre("핑크퐁과 아기상어의 무지개 구출 작전", GenreType.INTERACTIVE);

        genreService.saveGenre("메리골드 [대학로]", GenreType.HEALING);

        genreService.saveGenre("난타 [제주]", GenreType.NON_VERBAL);
        genreService.saveGenre("난타 [제주]", GenreType.COMEDY);
        genreService.saveGenre("난타 [제주]", GenreType.PERFORMANCE);

        genreService.saveGenre("겁쟁이 빌리 [안산 고잔]", GenreType.FAMILY);
        genreService.saveGenre("겁쟁이 빌리 [안산 고잔]", GenreType.HEALING);

        genreService.saveGenre("난타 [홍대]", GenreType.NON_VERBAL);
        genreService.saveGenre("난타 [홍대]", GenreType.COMEDY);
        genreService.saveGenre("난타 [홍대]", GenreType.PERFORMANCE);

        genreService.saveGenre("소년 이순신: 잃어버린 거북선 설계도를 찾아라!", GenreType.CHILD);
        genreService.saveGenre("소년 이순신: 잃어버린 거북선 설계도를 찾아라!", GenreType.HISTORY);
        genreService.saveGenre("소년 이순신: 잃어버린 거북선 설계도를 찾아라!", GenreType.FANTASY);

        genreService.saveGenre("별빛소리", GenreType.EMOTION);
        genreService.saveGenre("별빛소리", GenreType.DRAMA);
        genreService.saveGenre("별빛소리", GenreType.YOUTHFUL_GROWTH);

        genreService.saveGenre("연남장 캬바레: 김려원의 ON-LY A ONE", GenreType.INTERACTIVE);
        genreService.saveGenre("연남장 캬바레: 김려원의 ON-LY A ONE", GenreType.CABARET);

        genreService.saveGenre("피노키오야 노올자~!", GenreType.FAMILY);

        genreService.saveGenre("스텝: 춤추는 발자국", GenreType.FAMILY);
        genreService.saveGenre("스텝: 춤추는 발자국", GenreType.FANTASY);
        genreService.saveGenre("스텝: 춤추는 발자국", GenreType.HEALING);
    }
}
