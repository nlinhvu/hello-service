package vn.cloud.helloservice;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Configuration
public class MetricsFilterConfig {

//    @Bean
//    public MeterFilter commonTagsMeterFilter() {
//        return MeterFilter.commonTags(
//                List.of(
//                        Tag.of("instance.uuid", UUID.randomUUID().toString()),
//                        Tag.of("zone.id", ZoneId.of("Europe/Berlin").toString())
//                )
//        );
//    }
}
