package muse_kopis.muse.performance.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muse_kopis.muse.auth.Auth;
import muse_kopis.muse.auth.oauth.application.OauthService;
import muse_kopis.muse.performance.application.PerformanceService;
import muse_kopis.muse.performance.domain.dto.Onboarding;
import muse_kopis.muse.performance.domain.dto.PerformanceRequest;
import muse_kopis.muse.performance.domain.dto.PerformanceResponse;
import muse_kopis.muse.performance.domain.genre.application.GenreService;
import muse_kopis.muse.performance.domain.genre.domain.GenreType;
import muse_kopis.muse.performance.domain.usergenre.application.UserGenreService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;
    private final GenreService genreService;
    private final UserGenreService userGenreService;
    private final OauthService oauthService;

    /**
     * @apiNote Search Performance
     * @param String search
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "검색",
            description = "검색어를 통해 공연을 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<PerformanceResponse>> searchPerformance(@RequestParam String search) {
        return ResponseEntity.ok().body(performanceService.findAllPerformanceBySearch(search));
    }

    /**
     * @apiNote Current Performances
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "공연중", description = "현재 진행중인 공연을 보여줍니다.")
    @GetMapping("/state")
    public ResponseEntity<List<PerformanceResponse>> getPerformances() {
        return ResponseEntity.ok().body(performanceService.findAllPerformance());
    }

    /**
     * @apiNote Popular Performances
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "인기 작품 6편",
            description = "최근 예매율이 높은 6편의 공연을 보여줍니다.")
    @GetMapping("/popular")
    public ResponseEntity<List<PerformanceResponse>> getPerformancePopular() {
        return ResponseEntity.ok().body(performanceService.fetchPopularPerformance());
    }

    /**
     * @apiNote Performance Detail
     * @param Long performanceId
     * @return PerformanceResponse
     */
    @Operation(summary = "공연 상세",
            description = "특정 공연의 정보를 보여줍니다.")
    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceResponse> getPerformance(@PathVariable Long performanceId) {
        return ResponseEntity.ok().body(performanceService.findById(performanceId));
    }

    /**
     * @apiNote Recommend Performances
     * @param Long memberId (JWT Token)
     * @return List<PerformanceResponse>
     */
    @GetMapping("/recommend")
    @Operation(summary = "사용자 맞춤 추천",
            description = "사용자 맞춤 공연을 추천합니다.")
    public ResponseEntity<List<PerformanceResponse>> recommendPerformance(@Auth Long memberId){
        return ResponseEntity.ok().body(performanceService.recommendPerformance(memberId));
    }

    /**
     * @apiNote Random Performances
     * @param Long memberId (JWT Token)
     * @return Set<PerformanceResponse>
     */
    @Operation(summary = "무작위 추천",
            description = "무작위로 공연을 추천합니다.")
    @GetMapping("/random")
    public ResponseEntity<Set<PerformanceResponse>> randomPerformance(@Auth Long memberId) {
        return ResponseEntity.ok().body(performanceService.getRandomPerformance(memberId));
    }

    /**
     * @apiNote Performance Onboarding
     * @return List<PerformanceResponse>
     */
    @Operation(summary = "온보딩",
            description = "온보딩 화면을 보여줍니다.")
    @GetMapping("/onboarding")
    public ResponseEntity<List<PerformanceResponse>> showOnboarding() {
        return ResponseEntity.ok().body(userGenreService.showOnboarding());
    }

    /**
     * @apiNote Write Performance Onboarding
     * @param Long memberId (JWT Token)
     * @param Onboarding onboarding
     * @return String
     */
    @Operation(summary = "온보딩",
            description = "온보딩 내용을 등록합니다., 공연 아이디를 받아옵니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<String> updateUserGenre(@Auth Long memberId, @RequestBody Onboarding onboarding) {
        String username = oauthService.updateUsername(memberId, onboarding.username());
        oauthService.updateUserState(memberId);
        userGenreService.updateGenres(onboarding.performanceId(), memberId);
        return ResponseEntity.ok().body(username);
    }

    /**
     * @apiNote Performance Poster
     * @param Long performanceId
     * @return ByteArrayResource
     */
    @Operation(summary = "공연 포스터 조회",
            description = "해당 공연의 포스터 이미지를 바이트배열로 제공합니다.")
    @GetMapping("/poster/{performanceId}")
    public ResponseEntity<ByteArrayResource> getPosterImage(@PathVariable Long performanceId) {
        ByteArrayResource image = performanceService.getPosterImage(performanceId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/gif"));
        return ResponseEntity.ok().headers(headers).body(image);
    }

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

//    @GetMapping("/genre")
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

        genreService.saveGenre("난타 [명동]", GenreType.NON_VERBAL);
        genreService.saveGenre("난타 [명동]", GenreType.COMEDY);
        genreService.saveGenre("난타 [명동]", GenreType.PERFORMANCE);

        genreService.saveGenre("아기 돼지 삼형제 [창원]", GenreType.FAIRY_TALE);
        genreService.saveGenre("아기 돼지 삼형제 [창원]", GenreType.CHILD);

        genreService.saveGenre("더 맨 얼라이브, 초이스", GenreType.FEMALE_ONLY);
        genreService.saveGenre("더 맨 얼라이브, 초이스", GenreType.PERFORMANCE);

        genreService.saveGenre("우연히 봄을 만나다", GenreType.BAND_MUSICAL);
        genreService.saveGenre("우연히 봄을 만나다", GenreType.HEALING);

        genreService.saveGenre("인사이드 미", GenreType.DRAMA);
        genreService.saveGenre("인사이드 미", GenreType.ROMANCE);
        genreService.saveGenre("인사이드 미", GenreType.HEALING);

        genreService.saveGenre("최고다 호기심딱지 시즌2: 빵빵 이야기 속으로 [안성]", GenreType.FAMILY);
        genreService.saveGenre("최고다 호기심딱지 시즌2: 빵빵 이야기 속으로 [안성]", GenreType.FAIRY_TALE);
        genreService.saveGenre("최고다 호기심딱지 시즌2: 빵빵 이야기 속으로 [안성]", GenreType.CHILD);

        genreService.saveGenre("ABBA: 아바", GenreType.DRAMA);
        genreService.saveGenre("ABBA: 아바", GenreType.RELIGION);
        genreService.saveGenre("ABBA: 아바", GenreType.COMEDY);
        genreService.saveGenre("ABBA: 아바", GenreType.FANTASY);

        genreService.saveGenre("ACC 공동기획 렛츠플레이, 삼양동화", GenreType.FAIRY_TALE);
        genreService.saveGenre("ACC 공동기획 렛츠플레이, 삼양동화", GenreType.CHILD);
        genreService.saveGenre("ACC 공동기획 렛츠플레이, 삼양동화", GenreType.FANTASY);

        genreService.saveGenre("베어만: 마지막잎새", GenreType.DRAMA);

        genreService.saveGenre("이쇼 with 연남장 캬바레", GenreType.INTERACTIVE);
        genreService.saveGenre("이쇼 with 연남장 캬바레", GenreType.CABARET);

        genreService.saveGenre("알라딘과 요술램프 [과천]", GenreType.FAIRY_TALE);
        genreService.saveGenre("알라딘과 요술램프 [과천]", GenreType.FANTASY);

        genreService.saveGenre("반짝반짝 백설공주", GenreType.FAIRY_TALE);
        genreService.saveGenre("반짝반짝 백설공주", GenreType.FANTASY);

        genreService.saveGenre("천로역정", GenreType.DRAMA);
        genreService.saveGenre("천로역정", GenreType.RELIGION);

        genreService.saveGenre("언제는 행복하지 않은 순간이 있었나요 (언행순) [부산]", GenreType.DRAMA);
        genreService.saveGenre("언제는 행복하지 않은 순간이 있었나요 (언행순) [부산]", GenreType.ROMANCE);

        genreService.saveGenre("빨강머리 토리: 토리의 수상한 가방 [수원]", GenreType.FAMILY);
        genreService.saveGenre("빨강머리 토리: 토리의 수상한 가방 [수원]", GenreType.CHILD);
        genreService.saveGenre("빨강머리 토리: 토리의 수상한 가방 [수원]", GenreType.FANTASY);

        genreService.saveGenre("흔해빠진 일", GenreType.EMOTION);
        genreService.saveGenre("흔해빠진 일", GenreType.DRAMA);

        genreService.saveGenre("셰프 [서울]", GenreType.DRAMA);
        genreService.saveGenre("셰프 [서울]", GenreType.COMEDY);

        genreService.saveGenre("넌 특별하단다 [서울]", GenreType.FAMILY);
        genreService.saveGenre("넌 특별하단다 [서울]", GenreType.EMOTION);
        genreService.saveGenre("넌 특별하단다 [서울]", GenreType.CHILD);

        genreService.saveGenre("햇님이 달님이", GenreType.FAMILY);
        genreService.saveGenre("햇님이 달님이", GenreType.FAIRY_TALE);

        genreService.saveGenre("홍련", GenreType.CLASSIC_NOVEL);
        genreService.saveGenre("홍련", GenreType.FANTASY);

        genreService.saveGenre("신데렐라 [동두천]", GenreType.FAIRY_TALE);
        genreService.saveGenre("신데렐라 [동두천]", GenreType.ROMANCE);
        genreService.saveGenre("신데렐라 [동두천]", GenreType.FANTASY);

        genreService.saveGenre("김종욱 찾기", GenreType.EMOTION);
        genreService.saveGenre("김종욱 찾기", GenreType.ROMANCE);
        genreService.saveGenre("김종욱 찾기", GenreType.COMEDY);

        genreService.saveGenre("앤서니 브라운의 난 책이 좋아요", GenreType.ADVENTURE);
        genreService.saveGenre("앤서니 브라운의 난 책이 좋아요", GenreType.CHILD);
        genreService.saveGenre("앤서니 브라운의 난 책이 좋아요", GenreType.FANTASY);
        genreService.saveGenre("앤서니 브라운의 난 책이 좋아요", GenreType.HEALING);

        genreService.saveGenre("아기돼지야 준비해!", GenreType.FAIRY_TALE);
        genreService.saveGenre("아기돼지야 준비해!", GenreType.CHILD);

        genreService.saveGenre("빨간모자소녀와 늑대 [천안]", GenreType.FAIRY_TALE);
        genreService.saveGenre("빨간모자소녀와 늑대 [천안]", GenreType.CHILD);

        genreService.saveGenre("써니텐", GenreType.ROMANCE);
        genreService.saveGenre("써니텐", GenreType.COMEDY);

        genreService.saveGenre("비밀의 화원", GenreType.EMOTION);
        genreService.saveGenre("비밀의 화원", GenreType.HEALING);

        genreService.saveGenre("스파이", GenreType.DRAMA);
        genreService.saveGenre("스파이", GenreType.HISTORY);
        genreService.saveGenre("스파이", GenreType.ACTION);

        genreService.saveGenre("두들팝 [서울]", GenreType.FAMILY);
        genreService.saveGenre("두들팝 [서울]", GenreType.FANTASY);

        genreService.saveGenre("스크루테이프의 편지", GenreType.COMEDY);
        genreService.saveGenre("스크루테이프의 편지", GenreType.PARODY);

        genreService.saveGenre("예수 그리스도, 더바이블 Ⅱ", GenreType.RELIGION);

        genreService.saveGenre("접변 [대학로]", GenreType.CRIME);
        genreService.saveGenre("접변 [대학로]", GenreType.HISTORY);
        genreService.saveGenre("접변 [대학로]", GenreType.JAZZ);

        genreService.saveGenre("장수탕 선녀님 [서울숲]", GenreType.FANTASY);
        genreService.saveGenre("장수탕 선녀님 [서울숲]", GenreType.HEALING);

        genreService.saveGenre("트루스토리 [대학로]", GenreType.BAND_MUSICAL);

        genreService.saveGenre("반짝반짝 인어공주", GenreType.FAMILY);
        genreService.saveGenre("반짝반짝 인어공주", GenreType.FAIRY_TALE);
        genreService.saveGenre("반짝반짝 인어공주", GenreType.CHILD);
        genreService.saveGenre("반짝반짝 인어공주", GenreType.FANTASY);

        genreService.saveGenre("종로 가족공연축제, 달님이 주신 아이", GenreType.FAMILY);
        genreService.saveGenre("종로 가족공연축제, 달님이 주신 아이", GenreType.ADVENTURE);
        genreService.saveGenre("종로 가족공연축제, 달님이 주신 아이", GenreType.FANTASY);

        genreService.saveGenre("알라딘 [인천]", GenreType.FAIRY_TALE);
        genreService.saveGenre("알라딘 [인천]", GenreType.FANTASY);

        genreService.saveGenre("프리즌 [H씨어터]", GenreType.FAMILY);
        genreService.saveGenre("프리즌 [H씨어터]", GenreType.BAND_MUSICAL);
        genreService.saveGenre("프리즌 [H씨어터]", GenreType.COMEDY);

        genreService.saveGenre("라푼젤 [검단]", GenreType.FAIRY_TALE);
        genreService.saveGenre("라푼젤 [검단]", GenreType.FANTASY);

        genreService.saveGenre("빌의 구둣방", GenreType.HISTORY);
        genreService.saveGenre("빌의 구둣방", GenreType.HUMOR);
        genreService.saveGenre("빌의 구둣방", GenreType.HEALING);

        genreService.saveGenre("내가 아빠고 아빠가 나라면 [대구]", GenreType.FAMILY);
        genreService.saveGenre("내가 아빠고 아빠가 나라면 [대구]", GenreType.CHILD);
        genreService.saveGenre("내가 아빠고 아빠가 나라면 [대구]", GenreType.HEALING);

        genreService.saveGenre("세례요한", GenreType.RELIGION);

        genreService.saveGenre("라면에 파송송", GenreType.COMEDY);
        genreService.saveGenre("라면에 파송송", GenreType.HEALING);

        genreService.saveGenre("유진과 유진 [대학로]", GenreType.EMOTION);
        genreService.saveGenre("유진과 유진 [대학로]", GenreType.HEALING);

        genreService.saveGenre("빨간모자야 조심해! [청주, 세종]", GenreType.FAMILY);
        genreService.saveGenre("빨간모자야 조심해! [청주, 세종]", GenreType.FAIRY_TALE);
        genreService.saveGenre("빨간모자야 조심해! [청주, 세종]", GenreType.CHILD);

        genreService.saveGenre("캐치! 티니핑: 두근두근 싱어롱 콘서트! [서울 (앵콜)]", GenreType.FAMILY);
        genreService.saveGenre("캐치! 티니핑: 두근두근 싱어롱 콘서트! [서울 (앵콜)]", GenreType.CHILD);

        genreService.saveGenre("반짝반짝 라푼젤", GenreType.FAIRY_TALE);
        genreService.saveGenre("반짝반짝 라푼젤", GenreType.FANTASY);

        genreService.saveGenre("인피니티 플라잉 (INFINITY FLYING) [경주]", GenreType.NON_VERBAL);
        genreService.saveGenre("인피니티 플라잉 (INFINITY FLYING) [경주]", GenreType.ACTION);
        genreService.saveGenre("인피니티 플라잉 (INFINITY FLYING) [경주]", GenreType.FANTASY);

        genreService.saveGenre("고고다이노: 해양구조대 [과천]", GenreType.FAMILY);
        genreService.saveGenre("고고다이노: 해양구조대 [과천]", GenreType.ADVENTURE);
        genreService.saveGenre("고고다이노: 해양구조대 [과천]", GenreType.CHILD);

        genreService.saveGenre("시데레우스", GenreType.HISTORY);
        genreService.saveGenre("시데레우스", GenreType.PHILOSOPHY);

        genreService.saveGenre("구름빵 [서울 광진구]", GenreType.FAMILY);
        genreService.saveGenre("구름빵 [서울 광진구]", GenreType.INTERACTIVE);
        genreService.saveGenre("구름빵 [서울 광진구]", GenreType.HEALING);

        genreService.saveGenre("종이아빠 [대학로]", GenreType.ADVENTURE);
        genreService.saveGenre("종이아빠 [대학로]", GenreType.CHILD);
        genreService.saveGenre("종이아빠 [대학로]", GenreType.FANTASY);

        genreService.saveGenre("투가이즈쇼 #2. GENERATION", GenreType.HISTORY);
        genreService.saveGenre("투가이즈쇼 #2. GENERATION", GenreType.TALK_CONCERT);

        genreService.saveGenre("겨울왕국: 겨울이야기 [서울 서대문]", GenreType.FAIRY_TALE);
        genreService.saveGenre("겨울왕국: 겨울이야기 [서울 서대문]", GenreType.CHILD);
        genreService.saveGenre("겨울왕국: 겨울이야기 [서울 서대문]", GenreType.FANTASY);
        genreService.saveGenre("겨울왕국: 겨울이야기 [서울 서대문]", GenreType.PERFORMANCE);

        genreService.saveGenre("콩쥐팥쥐 [광명]", GenreType.FAIRY_TALE);
        genreService.saveGenre("콩쥐팥쥐 [광명]", GenreType.CHILD);

        genreService.saveGenre("고릴라 [김포]", GenreType.FANTASY);
        genreService.saveGenre("고릴라 [김포]", GenreType.HEALING);

        genreService.saveGenre("페인터즈 [서대문전용관]", GenreType.NON_VERBAL);
        genreService.saveGenre("페인터즈 [서대문전용관]", GenreType.ART);
        genreService.saveGenre("페인터즈 [서대문전용관]", GenreType.PERFORMANCE);

        genreService.saveGenre("엄마까투리는 슈퍼맘 [천안]", GenreType.CHILD);

        genreService.saveGenre("아이위시 [서울]", GenreType.INTERACTIVE);
        genreService.saveGenre("아이위시 [서울]", GenreType.CABARET);

        genreService.saveGenre("연남장 캬바레, 오늘 처음 만드는 뮤지컬 [서울]", GenreType.INTERACTIVE);
        genreService.saveGenre("연남장 캬바레, 오늘 처음 만드는 뮤지컬 [서울]", GenreType.CABARET);

        genreService.saveGenre("사의찬미 [대학로]", GenreType.DRAMA);
        genreService.saveGenre("사의찬미 [대학로]", GenreType.HISTORY);

        genreService.saveGenre("박열 [대학로]", GenreType.DRAMA);
        genreService.saveGenre("박열 [대학로]", GenreType.HISTORY);

        genreService.saveGenre("로보카폴리: 잡아라! 황금트로피! [대구]", GenreType.CHILD);

        genreService.saveGenre("조선 이야기꾼 전기수", GenreType.CLASSIC_NOVEL);

        genreService.saveGenre("헬로카봇 시즌8, 미래연구소를 지켜라!", GenreType.CHILD);

        genreService.saveGenre("베르사유의 장미", GenreType.DRAMA);
        genreService.saveGenre("베르사유의 장미", GenreType.ROMANCE);
        genreService.saveGenre("베르사유의 장미", GenreType.HISTORY);

        genreService.saveGenre("엄마는 안 가르쳐 줘", GenreType.CHILD);

        genreService.saveGenre("또봇: 대도시의 영웅들 [부천]", GenreType.ADVENTURE);
        genreService.saveGenre("또봇: 대도시의 영웅들 [부천]", GenreType.CHILD);

        genreService.saveGenre("두들팝 [대전]", GenreType.FAMILY);
        genreService.saveGenre("두들팝 [대전]", GenreType.FANTASY);

        genreService.saveGenre("인어공주 [서울 목동]", GenreType.FAIRY_TALE);
        genreService.saveGenre("인어공주 [서울 목동]", GenreType.FANTASY);

        genreService.saveGenre("바다 100층짜리 집", GenreType.ADVENTURE);
        genreService.saveGenre("바다 100층짜리 집", GenreType.CHILD);

        genreService.saveGenre("한글용사 아이야: 사라진 한글을 찾아라! [서울 (앵콜)]", GenreType.ADVENTURE);
        genreService.saveGenre("한글용사 아이야: 사라진 한글을 찾아라! [서울 (앵콜)]", GenreType.CHILD);

        genreService.saveGenre("젠틀맨스 가이드: 사랑과 살인편", GenreType.DRAMA);
        genreService.saveGenre("젠틀맨스 가이드: 사랑과 살인편", GenreType.HISTORY);

        genreService.saveGenre("설민석의 한국사 대모험: 영웅의 시간", GenreType.CHILD);
        genreService.saveGenre("설민석의 한국사 대모험: 영웅의 시간", GenreType.HISTORY);

        genreService.saveGenre("앤서니 브라운의 우리가족 [서울]", GenreType.FAMILY);
        genreService.saveGenre("앤서니 브라운의 우리가족 [서울]", GenreType.HEALING);

        genreService.saveGenre("살리에르", GenreType.DRAMA);
        genreService.saveGenre("살리에르", GenreType.CRIME);
        genreService.saveGenre("살리에르", GenreType.HISTORY);

        genreService.saveGenre("조선셰프 한상궁 [전주]", GenreType.PERFORMANCE);

        genreService.saveGenre("4월은 너의 거짓말", GenreType.ROMANCE);
        genreService.saveGenre("4월은 너의 거짓말", GenreType.YOUTHFUL_GROWTH);

        genreService.saveGenre("매직 판타지아, 도로시 리턴즈", GenreType.FAIRY_TALE);
        genreService.saveGenre("매직 판타지아, 도로시 리턴즈", GenreType.FANTASY);

        genreService.saveGenre("하데스타운 [서울]", GenreType.ROMANCE);
        genreService.saveGenre("하데스타운 [서울]", GenreType.RELIGION);
        genreService.saveGenre("하데스타운 [서울]", GenreType.PHILOSOPHY);
        genreService.saveGenre("하데스타운 [서울]", GenreType.HEALING);

        genreService.saveGenre("카르밀라", GenreType.DRAMA);
        genreService.saveGenre("카르밀라", GenreType.CRIME);

        genreService.saveGenre("베베핀, 우당탕탕 패밀리 [과천]", GenreType.FAMILY);
        genreService.saveGenre("베베핀, 우당탕탕 패밀리 [과천]", GenreType.CHILD);
        genreService.saveGenre("베베핀, 우당탕탕 패밀리 [과천]", GenreType.PERFORMANCE);

        genreService.saveGenre("등등곡", GenreType.HISTORY);
        genreService.saveGenre("등등곡", GenreType.HISTORY);

        genreService.saveGenre("어쩌면 해피엔딩 [대학로]", GenreType.EMOTION);
        genreService.saveGenre("어쩌면 해피엔딩 [대학로]", GenreType.ROMANCE);
        genreService.saveGenre("어쩌면 해피엔딩 [대학로]", GenreType.HEALING);

        genreService.saveGenre("빨래 [대학로]", GenreType.EMOTION);
        genreService.saveGenre("빨래 [대학로]", GenreType.HEALING);

        genreService.saveGenre("이블데드", GenreType.BAND_MUSICAL);
        genreService.saveGenre("이블데드", GenreType.THRILLER);
        genreService.saveGenre("이블데드", GenreType.INTERACTIVE);

        genreService.saveGenre("메노포즈", GenreType.FEMALE_ONLY);
        genreService.saveGenre("메노포즈", GenreType.COMEDY);

        genreService.saveGenre("프리즌 [대전 평송]", GenreType.FAMILY);
        genreService.saveGenre("프리즌 [대전 평송]", GenreType.BAND_MUSICAL);
        genreService.saveGenre("프리즌 [대전 평송]", GenreType.COMEDY);

        genreService.saveGenre("클럽 드바이", GenreType.ROCK);

        genreService.saveGenre("새벽의 입구에서", GenreType.HISTORY);
        genreService.saveGenre("새벽의 입구에서", GenreType.ART);

        genreService.saveGenre("에밀 [대학로]", GenreType.DRAMA);
        genreService.saveGenre("에밀 [대학로]", GenreType.CRIME);
        genreService.saveGenre("에밀 [대학로]", GenreType.HISTORY);

        genreService.saveGenre("오만방자 전라감사 길들이기 [전주]", GenreType.HISTORY);

        genreService.saveGenre("두들팝 VER.3, 하이팝", GenreType.FAMILY);
        genreService.saveGenre("두들팝 VER.3, 하이팝", GenreType.FANTASY);

        genreService.saveGenre("수박수영장 [서울]", GenreType.FAMILY);
        genreService.saveGenre("수박수영장 [서울]", GenreType.HEALING);

        genreService.saveGenre("프랑켄슈타인", GenreType.HISTORY);
        genreService.saveGenre("프랑켄슈타인", GenreType.DRAMA);

        genreService.saveGenre("미오 프라텔로 [대학로]", GenreType.DRAMA);
        genreService.saveGenre("미오 프라텔로 [대학로]", GenreType.ROMANCE);
        genreService.saveGenre("미오 프라텔로 [대학로]", GenreType.CRIME);
    }

    // KOPIS 전체 공연 목록 저장
//    @GetMapping("/kopis")
    public ResponseEntity<Void> getPerformances(@ModelAttribute PerformanceRequest performanceRequest)
            throws JsonProcessingException {
        performanceService.fetchPerformances(performanceRequest.startDate(), performanceRequest.endDate(), performanceRequest.currentPage(),
                performanceRequest.rows(), performanceRequest.state(), performanceRequest.genre());
        return ResponseEntity.ok().build();
    }
}
