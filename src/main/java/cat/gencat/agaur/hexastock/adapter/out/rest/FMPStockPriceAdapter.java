package cat.gencat.agaur.hexastock.adapter.out.rest;

import cat.gencat.agaur.hexastock.application.port.out.StockPriceProviderPort;
import cat.gencat.agaur.hexastock.model.StockPrice;
import cat.gencat.agaur.hexastock.model.Ticker;
import cat.gencat.agaur.hexastock.model.exception.ExternalApiException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.ZoneId;

// (Ferran i Pol)
@Component
@Profile("FMP")
public class FMPStockPriceAdapter implements StockPriceProviderPort {

    @Value("${fmp.api.key}")
    private String fmpApiKey;
    @Value("${fmp.api.base-url}")
    private String fmpBaseUrl;

    // Throttles outbound API calls to avoid hitting free-tier rate limits.
    // We intentionally sleep the current thread before performing the request.
    // NOTE: If you move to reactive/non-blocking I/O in the future, replace this with a non-blocking delay.
    private static final long THROTTLE_MS = 500L;

    private void throttle() {
        try {
            Thread.sleep(THROTTLE_MS);
        } catch (InterruptedException ie) {
            // Restore the interrupted status so higher-level code can react if needed.
            Thread.currentThread().interrupt();
            // Optionally log the interruption; we do NOT rethrow to avoid breaking the call path.
        }
    }

    @Override
    public StockPrice fetchStockPrice(Ticker ticker) {
        // Throttle to stay within free-tier rate limits (Financial Modeling Prep).
        throttle();

        String url = String.format("%s/stable/quote?symbol=%s&apikey=%s", fmpBaseUrl, ticker.value(), fmpApiKey);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000); // milliseconds
        factory.setReadTimeout(5_000);    // milliseconds

        RestClient restClient = RestClient.builder()
                .baseUrl(fmpBaseUrl)
                .requestFactory(factory)
                .build();

        // Fetch the quote from the Financial Modeling Prep

        JsonNode quoteJson;
        try {
            quoteJson = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(JsonNode.class);
            if (quoteJson == null || !quoteJson.isArray() || quoteJson.get(0).get("price") == null || !quoteJson.get(0).get("price").isNumber()) {
                throw new ExternalApiException("Invalid response from Financial Modeling Prep API: missing or malformed price data");
            }
        } catch (Exception e) {
            throw new ExternalApiException("Error communicating with Financial Modeling Prep API. Please check the value for Financial Modeling Prep in application.properties ", e);
        }
        double currentPrice = quoteJson.get(0).get("price").asDouble();
        return new StockPrice(ticker, currentPrice, LocalDateTime.now()
                .atZone(ZoneId.of("Europe/Madrid")).toInstant(), "USD");
    }
}
