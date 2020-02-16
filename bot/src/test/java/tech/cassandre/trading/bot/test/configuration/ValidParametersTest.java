package tech.cassandre.trading.bot.test.configuration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.cassandre.trading.bot.configuration.ExchangeParameters;
import tech.cassandre.trading.bot.test.util.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Valid application parameters tests.
 */
@SpringBootTest
@DisplayName("Valid application parameters tests")
public class ValidParametersTest extends BaseTest {

	@BeforeAll
	static void beforeAll() {
		System.setProperty("cassandre.trading.bot.exchange.name", "kucoin");
		System.setProperty("cassandre.trading.bot.exchange.sandbox", "true");
		System.setProperty("cassandre.trading.bot.exchange.username", "cassandre.crypto.bot@gmail.com");
		System.setProperty("cassandre.trading.bot.exchange.passphrase", "cassandre");
		System.setProperty("cassandre.trading.bot.exchange.key", "5df8eea30092f40009cb3c6a");
		System.setProperty("cassandre.trading.bot.exchange.secret", "WRONG_SECRET");
		System.setProperty("cassandre.trading.bot.exchange.rates.account", "100");
		System.setProperty("cassandre.trading.bot.exchange.rates.ticker", "101");
		System.setProperty("cassandre.trading.bot.exchange.rates.order", "102");
	}

	@Test
	@DisplayName("Retrieved values")
	void exchangeParameters() {
		ExchangeParameters exchange = getContext().getBean(ExchangeParameters.class);
		assertEquals("kucoin", exchange.getName());
		assertTrue(exchange.isSandbox());
		assertEquals("cassandre.crypto.bot@gmail.com", exchange.getUsername());
		assertEquals("cassandre", exchange.getPassphrase());
		assertEquals("5df8eea30092f40009cb3c6a", exchange.getKey());
		assertEquals("WRONG_SECRET", exchange.getSecret());
		final int expectedAccountRate = 100;
		final int expectedTickerRate = 101;
		final int expectedOrderRate = 102;
		assertEquals(expectedAccountRate, exchange.getRates().getAccount());
		assertEquals(expectedTickerRate, exchange.getRates().getTicker());
		assertEquals(expectedOrderRate, exchange.getRates().getOrder());
	}

}
