package muse_kopis.muse.performance.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JacksonXmlRootElement(localName = "dbs")
public record DBS(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    List<DB> db
) {
    public record DB(
        @JacksonXmlProperty(localName = "mt20id") String mt20id,
        @JacksonXmlProperty(localName = "prfnm") String prfnm,
        @JacksonXmlProperty(localName = "prfpdfrom") String prfpdfrom,
        @JacksonXmlProperty(localName = "prfpdto") String prfpdto,
        @JacksonXmlProperty(localName = "fcltynm") String fcltynm,
        @JacksonXmlProperty(localName = "poster") String poster,
        @JacksonXmlProperty(localName = "area") String area,
        @JacksonXmlProperty(localName = "genrenm") String genrenm,
        @JacksonXmlProperty(localName = "openrun") String openrun,
        @JacksonXmlProperty(localName = "prfstate") String prfstate
    ) {}
}
