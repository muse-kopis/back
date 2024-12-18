package muse_kopis.muse.actor.domain;

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
import muse_kopis.muse.performance.domain.Performance;

@Entity
@Getter
@NoArgsConstructor
public class CastMember extends Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;
    private String role;

    public CastMember(String name, String actorId, Performance performance, String role) {
        super(name, actorId);
        this.performance = performance;
        this.role = role;
    }

    @Override
    public String toString() {
        return super.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMember that = (CastMember) o;
        return Objects.equals(super.getName(), that.getName()) &&
                Objects.equals(performance, that.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), performance);
    }
}
