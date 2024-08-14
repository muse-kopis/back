package muse_kopis.muse.performance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String performanceName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String venue; // 공연장
    private String performanceCrews;
    private String limitAge;
    private String price;
    private String poster;
    private String state; // 공연완료(03), 공연중(02), 공연예정(01)
    private String entertainment;
    private String performanceTime;
}
