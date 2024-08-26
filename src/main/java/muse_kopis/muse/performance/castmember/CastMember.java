package muse_kopis.muse.performance.castmember;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import muse_kopis.muse.performance.Performance;

@Entity
@Getter
@NoArgsConstructor
public class CastMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    public CastMember(String name, Performance performance) {
        this.name = name;
        this.performance = performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMember that = (CastMember) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(performance, that.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, performance);
    }
}
