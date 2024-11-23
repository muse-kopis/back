package muse_kopis.muse.performance.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

@JacksonXmlRootElement(localName = "boxofs")
@JsonIgnoreProperties(ignoreUnknown = true)
public record Boxofs(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "boxof")
        List<Boxof> boxof
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Boxof(
            @JacksonXmlProperty(localName = "prfplcnm") String prfplcnm,
            @JacksonXmlProperty(localName = "seatcnt") int seatcnt,
            @JacksonXmlProperty(localName = "rnum") int rnum,
            @JacksonXmlProperty(localName = "poster") String poster,
            @JacksonXmlProperty(localName = "prfpd") String prfpd,
            @JacksonXmlProperty(localName = "mt20id") String mt20id,
            @JacksonXmlProperty(localName = "prfnm") String prfnm,
            @JacksonXmlProperty(localName = "cate") String cate,
            @JacksonXmlProperty(localName = "prfdtcnt") int prfdtcnt,
            @JacksonXmlProperty(localName = "area") String area
    ) {}
}
