package com.lyennon.prometheus;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yong.liang 2019/5/17
 */
public class PrometheusServer {

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        prometheusRegistry.config().commonTags("application", "prometheus_demo");
        counter(prometheusRegistry);
        guage(prometheusRegistry);
        timer(prometheusRegistry);
        summary(prometheusRegistry);
        histograms(prometheusRegistry);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {
                prometheusRegistry.counter("http.request", Tags.of("url", "/prometheus")).increment();
                String response = prometheusRegistry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void counter(MeterRegistry meterRegistry) {
        Counter counter = meterRegistry.counter("page.visitors", "age", "20s");
        counter.increment();

        Counter counter2 = Counter.builder("page.visitors2")
            .tag("age", "20s")
            .register(meterRegistry);
        counter2.increment();
    }

    private static void timer(MeterRegistry meterRegistry) {
        Timer timer = meterRegistry.timer("app.event");
        timer.record(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException ignored) {
            }
        });

        timer.record(3000, TimeUnit.MILLISECONDS);
    }

    private static void guage(MeterRegistry meterRegistry) {
        List<String> list = new ArrayList<>(4);

        Gauge.builder("cache.size", list, List::size)
            .register(meterRegistry);
    }

    private static void summary(MeterRegistry meterRegistry){
        DistributionSummary distributionSummary = DistributionSummary
            .builder("request.size")
            .baseUnit("bytes")
            .register(meterRegistry);

        distributionSummary.record(3);
        distributionSummary.record(4);
        distributionSummary.record(5);
    }

    private static void histograms(MeterRegistry meterRegistry){
        DistributionSummary hist = DistributionSummary
            .builder("summary")
            .publishPercentileHistogram()
            .register(meterRegistry);

        hist.record(3);
        hist.record(8);
        hist.record(20);
        hist.record(40);
        hist.record(13);
        hist.record(26);
        hist.record(46);
        hist.record(12);
        hist.record(21);
        hist.record(45);
    }
}
