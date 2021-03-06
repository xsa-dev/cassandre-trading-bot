package tech.cassandre.trading.bot.test.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import tech.cassandre.trading.bot.batch.OrderFlux;
import tech.cassandre.trading.bot.batch.TradeFlux;
import tech.cassandre.trading.bot.domain.Strategy;
import tech.cassandre.trading.bot.domain.Trade;
import tech.cassandre.trading.bot.dto.strategy.StrategyDTO;
import tech.cassandre.trading.bot.dto.trade.OrderDTO;
import tech.cassandre.trading.bot.dto.trade.TradeDTO;
import tech.cassandre.trading.bot.dto.util.CurrencyPairDTO;
import tech.cassandre.trading.bot.repository.OrderRepository;
import tech.cassandre.trading.bot.repository.StrategyRepository;
import tech.cassandre.trading.bot.repository.TradeRepository;
import tech.cassandre.trading.bot.test.util.junit.BaseTest;
import tech.cassandre.trading.bot.test.util.junit.configuration.Configuration;
import tech.cassandre.trading.bot.test.util.junit.configuration.Property;
import tech.cassandre.trading.bot.test.util.strategies.TestableCassandreStrategy;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static tech.cassandre.trading.bot.dto.trade.OrderStatusDTO.NEW;
import static tech.cassandre.trading.bot.dto.trade.OrderTypeDTO.ASK;
import static tech.cassandre.trading.bot.dto.trade.OrderTypeDTO.BID;
import static tech.cassandre.trading.bot.dto.util.CurrencyDTO.BTC;
import static tech.cassandre.trading.bot.dto.util.CurrencyDTO.ETH;
import static tech.cassandre.trading.bot.dto.util.CurrencyDTO.USD;
import static tech.cassandre.trading.bot.dto.util.CurrencyDTO.USDT;

@SpringBootTest
@DisplayName("Domain - Trade")
@Configuration({
        @Property(key = "spring.datasource.data", value = "classpath:/backup.sql")
})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("schedule-disabled")
public class TradeTest extends BaseTest {

    @Autowired
    private TestableCassandreStrategy strategy;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeFlux tradeFlux;

    @Autowired
    private OrderFlux orderFlux;

    @Test
    @DisplayName("Check load trade from database")
    public void checkLoadTradeFromDatabase() {
        // =============================================================================================================
        // Check that positions, orders and trades in database doesn't trigger strategy events.
        assertTrue(strategy.getPositionsUpdateReceived().isEmpty());
        assertTrue(strategy.getTradesUpdateReceived().isEmpty());
        assertTrue(strategy.getOrdersUpdateReceived().isEmpty());

        // Check trade 01.
        TradeDTO trade = strategy.getTrades().get("BACKUP_TRADE_01");
        assertNotNull(trade);
        assertEquals("BACKUP_TRADE_01", trade.getId());
        assertEquals("BACKUP_OPENING_ORDER_02", trade.getOrderId());
        assertEquals(BID, trade.getType());
        assertEquals(0, new BigDecimal("20").compareTo(trade.getOriginalAmount()));
        assertEquals(new CurrencyPairDTO(BTC, USDT), trade.getCurrencyPair());
        assertEquals(0, new BigDecimal("10").compareTo(trade.getPrice()));
        assertEquals(createZonedDateTime("01-08-2020"), trade.getTimestamp());
        assertEquals(0, new BigDecimal("1").compareTo(trade.getFee().getValue()));
        assertEquals(USDT, trade.getFee().getCurrency());
        // Check trade 02.
        trade = strategy.getTrades().get("BACKUP_TRADE_02");
        assertNotNull(trade);
        assertEquals("BACKUP_TRADE_02", trade.getId());
        assertEquals("BACKUP_OPENING_ORDER_03", trade.getOrderId());
        assertEquals(BID, trade.getType());
        assertEquals(0, new BigDecimal("30").compareTo(trade.getOriginalAmount()));
        assertEquals(new CurrencyPairDTO(BTC, USDT), trade.getCurrencyPair());
        assertEquals(0, new BigDecimal("20").compareTo(trade.getPrice()));
        assertEquals(createZonedDateTime("02-08-2020"), trade.getTimestamp());
        assertEquals(0, new BigDecimal("2").compareTo(trade.getFee().getValue()));
        assertEquals(USDT, trade.getFee().getCurrency());
        // Check trade 03.
        trade = strategy.getTrades().get("BACKUP_TRADE_03");
        assertNotNull(trade);
        assertEquals("BACKUP_TRADE_03", trade.getId());
        assertEquals("BACKUP_OPENING_ORDER_04", trade.getOrderId());
        assertEquals(BID, trade.getType());
        assertEquals(0, new BigDecimal("40").compareTo(trade.getOriginalAmount()));
        assertEquals(new CurrencyPairDTO(BTC, USDT), trade.getCurrencyPair());
        assertEquals(0, new BigDecimal("30").compareTo(trade.getPrice()));
        assertEquals(createZonedDateTime("03-08-2020"), trade.getTimestamp());
        assertEquals(0, new BigDecimal("3").compareTo(trade.getFee().getValue()));
        assertEquals(USDT, trade.getFee().getCurrency());
        // Check trade 04.
        trade = strategy.getTrades().get("BACKUP_TRADE_04");
        assertNotNull(trade);
        assertEquals("BACKUP_TRADE_04", trade.getId());
        assertEquals("BACKUP_CLOSING_ORDER_01", trade.getOrderId());
        assertEquals(ASK, trade.getType());
        assertEquals(0, new BigDecimal("40").compareTo(trade.getOriginalAmount()));
        assertEquals(new CurrencyPairDTO(BTC, USDT), trade.getCurrencyPair());
        assertEquals(0, new BigDecimal("40").compareTo(trade.getPrice()));
        assertEquals(createZonedDateTime("04-08-2020"), trade.getTimestamp());
        assertEquals(0, new BigDecimal("4").compareTo(trade.getFee().getValue()));
        assertEquals(USDT, trade.getFee().getCurrency());
        // Check trade 05.
        trade = strategy.getTrades().get("BACKUP_TRADE_05");
        assertNotNull(trade);
        assertEquals("BACKUP_TRADE_05", trade.getId());
        assertEquals("BACKUP_CLOSING_ORDER_02", trade.getOrderId());
        assertEquals(ASK, trade.getType());
        assertEquals(0, new BigDecimal("50").compareTo(trade.getOriginalAmount()));
        assertEquals(new CurrencyPairDTO(ETH, USD), trade.getCurrencyPair());
        assertEquals(0, new BigDecimal("50").compareTo(trade.getPrice()));
        assertEquals(createZonedDateTime("05-08-2020"), trade.getTimestamp());
        assertEquals(0, new BigDecimal("5").compareTo(trade.getFee().getValue()));
        assertEquals(USD, trade.getFee().getCurrency());
    }

    @Test
    @DisplayName("Check save trade in database")
    public void checkSaveTradeInDatabase() {
        // =============================================================================================================
        // Check that positions, orders and trades in database doesn't trigger strategy events.
        assertTrue(strategy.getPositionsUpdateReceived().isEmpty());
        assertTrue(strategy.getTradesUpdateReceived().isEmpty());
        assertTrue(strategy.getOrdersUpdateReceived().isEmpty());

        // =============================================================================================================
        // Loading strategy.
        final Optional<Strategy> optionalStrategy = strategyRepository.findById("001");
        assertTrue(optionalStrategy.isPresent());
        final Strategy strategyDTO = optionalStrategy.get();

        // =============================================================================================================
        // Add a trade and check that it's correctly saved in database.
        TradeDTO t1 = TradeDTO.builder()
                .id("BACKUP_TRADE_11")
                .orderId("BACKUP_ORDER_01")
                .type(BID)
                .originalAmount(new BigDecimal("1.100001"))
                .currencyPair(cp1)
                .price(new BigDecimal("2.200002"))
                .timestamp(createZonedDateTime("01-09-2020"))
                .feeAmount(new BigDecimal("3.300003"))
                .feeCurrency(BTC)
                .create();
        tradeFlux.emitValue(t1);
        await().untilAsserted(() -> assertEquals(1, strategy.getTradesUpdateReceived().size()));

        // =============================================================================================================
        // Trade - Check created order (domain).
        Optional<Trade> tradeInDatabase = tradeRepository.findById("BACKUP_TRADE_11");
        assertTrue(tradeInDatabase.isPresent());
        assertEquals("BACKUP_TRADE_11", tradeInDatabase.get().getId());
        assertEquals("BACKUP_ORDER_01", tradeInDatabase.get().getOrderId());
        assertEquals(BID, tradeInDatabase.get().getType());
        assertEquals(0, tradeInDatabase.get().getOriginalAmount().compareTo(new BigDecimal("1.100001")));
        assertEquals("ETH/BTC", tradeInDatabase.get().getCurrencyPair());
        assertEquals(0, tradeInDatabase.get().getPrice().compareTo(new BigDecimal("2.200002")));
        assertEquals(createZonedDateTime("01-09-2020"), tradeInDatabase.get().getTimestamp());
        assertEquals(0, tradeInDatabase.get().getFeeAmount().compareTo(new BigDecimal("3.300003")));
        assertEquals("BTC", tradeInDatabase.get().getFeeCurrency());
        // Tests for created on and updated on fields.
        ZonedDateTime createdOn = tradeInDatabase.get().getCreatedOn();
        assertNotNull(createdOn);
        assertNull(tradeInDatabase.get().getUpdatedOn());

        // =============================================================================================================
        // TradeDTO - Check created trade (dto).
        final TradeDTO tradeDTO = strategy.getTrades().get("BACKUP_TRADE_11");
        assertNotNull(tradeDTO);
        assertEquals("BACKUP_TRADE_11", tradeDTO.getId());
        assertEquals("BACKUP_ORDER_01", tradeDTO.getOrderId());
        assertEquals(BID, tradeDTO.getType());
        assertEquals(0, tradeDTO.getOriginalAmount().compareTo(new BigDecimal("1.100001")));
        assertEquals(cp1, tradeDTO.getCurrencyPair());
        assertEquals(0, tradeDTO.getPrice().compareTo(new BigDecimal("2.200002")));
        assertEquals(createZonedDateTime("01-09-2020"), tradeDTO.getTimestamp());
        assertEquals(0, tradeDTO.getFee().getValue().compareTo(new BigDecimal("3.300003")));
        assertEquals(BTC, tradeDTO.getFee().getCurrency());

        // =============================================================================================================
        // Updating the trade - first time.
        tradeFlux.emitValue(TradeDTO.builder()
                .id("BACKUP_TRADE_11")
                .orderId("BACKUP_ORDER_01")
                .type(BID)
                .originalAmount(new BigDecimal("1.100002"))
                .currencyPair(cp1)
                .price(new BigDecimal("2.200002"))
                .timestamp(createZonedDateTime("01-09-2020"))
                .feeAmount(new BigDecimal("3.300003"))
                .feeCurrency(BTC)
                .create());
        await().untilAsserted(() -> assertEquals(2, strategy.getTradesUpdateReceived().size()));
        await().untilAsserted(() -> assertNotNull(tradeRepository.findById("BACKUP_TRADE_11").get().getUpdatedOn()));
        assertEquals(createdOn, tradeRepository.findById("BACKUP_TRADE_11").get().getCreatedOn());
        ZonedDateTime updatedOn = tradeInDatabase.get().getCreatedOn();

        // =============================================================================================================
        // Updating the order - second time.
        tradeFlux.emitValue(TradeDTO.builder()
                .id("BACKUP_TRADE_11")
                .orderId("BACKUP_ORDER_01")
                .type(BID)
                .originalAmount(new BigDecimal("1.100003"))
                .currencyPair(cp1)
                .price(new BigDecimal("2.200002"))
                .timestamp(createZonedDateTime("01-09-2020"))
                .feeAmount(new BigDecimal("3.300003"))
                .feeCurrency(BTC)
                .create());
        await().untilAsserted(() -> assertTrue(updatedOn.isBefore(tradeRepository.findById("BACKUP_TRADE_11").get().getUpdatedOn())));
        assertEquals(createdOn, tradeRepository.findById("BACKUP_TRADE_11").get().getCreatedOn());
        // We check if we still have the strategy set.
        final Optional<TradeDTO> optionalTrade = strategy.getTradeById("BACKUP_TRADE_11");
        assertTrue(optionalTrade.isPresent());
    }

    @Test
    @DisplayName("Check strategy value in trade")
    public void checkStrategyValueInTrade() {
        // =============================================================================================================
        // Loading strategy.
        StrategyDTO strategyDTO = new StrategyDTO();
        strategyDTO.setId("1");

        // =============================================================================================================
        // First, we have an order (NEW_ORDER) that arrives with a strategy.
        long orderCount = orderRepository.count();
        orderFlux.emitValue(OrderDTO.builder()
                .id("NEW_ORDER")
                .type(ASK)
                .originalAmount(new BigDecimal("1.00001"))
                .currencyPair(cp1)
                .userReference("MY_REF_3")
                .timestamp(createZonedDateTime("01-01-2020"))
                .status(NEW)
                .cumulativeAmount(new BigDecimal("1.00002"))
                .averagePrice(new BigDecimal("1.00003"))
                .fee(new BigDecimal("1.00004"))
                .leverage("leverage3")
                .limitPrice(new BigDecimal("1.00005"))
                .strategy(strategyDTO)
                .create());
        await().untilAsserted(() -> assertEquals(orderCount + 1, orderRepository.count()));

        // =============================================================================================================
        // Then a new trade arrives for order NEW_ORDER and weh check that the strategy is set.
        long tradeCount = tradeRepository.count();
        tradeFlux.emitValue(TradeDTO.builder()
                .id("NEW_TRADE")
                .orderId("NEW_ORDER")
                .type(BID)
                .originalAmount(new BigDecimal("1.100003"))
                .currencyPair(cp1)
                .price(new BigDecimal("2.200002"))
                .timestamp(createZonedDateTime("01-09-2020"))
                .feeAmount(new BigDecimal("3.300003"))
                .feeCurrency(BTC)
                .create());
        await().untilAsserted(() -> assertEquals(tradeCount + 1, tradeRepository.count()));
        final Optional<Trade> optionalTrade = tradeRepository.findById("NEW_TRADE");
        assertTrue(optionalTrade.isPresent());
    }

}
