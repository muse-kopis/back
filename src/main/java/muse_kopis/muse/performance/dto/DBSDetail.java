package muse_kopis.muse.performance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

@JacksonXmlRootElement(localName = "dbs")
public record DBSDetail(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    List<DB> db
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DB(
        @JacksonXmlProperty(localName = "mt20id") String mt20id,
        @JacksonXmlProperty(localName = "prfnm") String performanceName,
        @JacksonXmlProperty(localName = "prfpdfrom") String startDate,
        @JacksonXmlProperty(localName = "prfpdto") String endDate,
        @JacksonXmlProperty(localName = "fcltynm") String venue, // 공연장
        @JacksonXmlProperty(localName = "prfcrew") String crews,
        @JacksonXmlProperty(localName = "prfage") String limitAge,
        @JacksonXmlProperty(localName = "entrpsnm") String entertainment, // 제작사
        @JacksonXmlProperty(localName = "pcseguidance") String price,
        @JacksonXmlProperty(localName = "poster") String poster,
        @JacksonXmlProperty(localName = "prfstate") String state,
        @JacksonXmlProperty(localName = "dtguidance") String performanceTime
    ) {}
}
