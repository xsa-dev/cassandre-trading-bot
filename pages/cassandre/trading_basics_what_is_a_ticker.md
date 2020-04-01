---
title: What is a ticker ?
sidebar: cassandre_sidebar
permalink: trading_basics_what_is_a_ticker.html
---

A ticker, synonym for stock symbol, is the short form of full security (asset) name (e.g. full: Bitcoin, ticker: BTC). A ticker has a stream of quotes (AKA prices) attached to it, continuously updated throughout a trading session by the various exchanges. A “tick” is any change in the price of the security as observed from one trade to the next, whether that movement is up or down.

These are the fields you can find on a ticker quote:

| Field  | Description  |
|-------|---------|
| <code>currencyPair</code>  | Currency pair  |
| <code>open</code>  | The opening price is the first trade price that was recorded during the day’s trading  |
| <code>last</code>  | Last trade field is the price at which the last trade was executed  |
| <code>bid</code>  | The bid price shown represents the highest price  |
| <code>ask</code>  | The ask price shown represents the lowest price  |
| <code>high</code>  | The day’s high price  |
| <code>low</code>  | The day’s low price  |
| <code>vwap</code>  | Volume-weighted average price (VWAP) is the ratio of the value traded to total volume traded over a particular time horizon (usually one day)  |
| <code>volume</code>  | Volume is the number of shares or contracts traded  |
| <code>quoteVolume</code>  | Quote volume  |
| <code>bidSize</code>  | The bid size represents the quantity of a security that investors are willing to purchase at a specified bid price  |
| <code>askSize</code>  | The ask size represents the quantity of a security that investors are willing to sell at a specified selling price  |
| <code>timestamp</code>  | The moment at which the account information was retrieved  |