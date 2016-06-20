package com.pbednarz.transactionviewer.providers.exchange;

import android.support.annotation.NonNull;

import com.pbednarz.transactionviewer.models.Rate;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the currency converter.
 * Final exchange rates are computed using Dijkstra's shortest path algorithm.
 *
 * @author pbednarz
 * @see <a href="https://github.com/mvaz/CurrencyConverter">Based on Miguel Vaz CurrencyConverter</a>
 */

public class CurrencyConverterImpl implements CurrencyConverter {
    private final String goalCurrencyCode;
    private final SimpleDirectedGraph<String, Rate> currencyGraph;
    private final Map<String, BigDecimal> ratesCache;

    /**
     * Simple constructor. Initializes graph and rates cache
     */
    public CurrencyConverterImpl(@NonNull String goalCurrencyCode, @NonNull List<Rate> rates) {
        this.goalCurrencyCode = goalCurrencyCode;
        this.currencyGraph = new SimpleDirectedGraph<>(Rate.class);
        this.ratesCache = new HashMap<>();
        initRates(rates);
    }

    /**
     * Initializes graph node and edges, added known rates to cache
     *
     * @param rates List of rates
     */
    private void initRates(@NonNull List<Rate> rates) {
        for (Rate rate : rates) {
            setExchangeRateToGraph(rate);
            if (goalCurrencyCode.equals(rate.getTo())) {
                ratesCache.put(rate.getFrom(), rate.getRate());
            }
        }
    }

    /**
     * Converts a given amount of an origin currency to a goal currency.
     *
     * @param inValue      the amount of an origin currency
     * @param currencyFrom the string identifier of the origin currency
     * @return the amount of converted currency
     * @throws ExchangeRateUndefinedException
     * @throws ArithmeticException
     */
    @Override
    public BigDecimal convertCurrency(@NonNull BigDecimal inValue, @NonNull String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException {
        if (goalCurrencyCode.equals(currencyFrom)) {
            return inValue;
        }

        BigDecimal rate = getRate(currencyFrom);
        BigDecimal outValue = inValue.multiply(rate);
        outValue = outValue.setScale(2, BigDecimal.ROUND_HALF_UP);
        return outValue;
    }

    /**
     * Finds rate between goal and origin currency
     *
     * @param currencyFrom Origin currency
     * @return BigDecimal rate
     * @throws ExchangeRateUndefinedException
     */
    private BigDecimal getRate(@NonNull String currencyFrom) throws ExchangeRateUndefinedException {
        BigDecimal rate = ratesCache.get(currencyFrom);
        if (rate == null) {
            rate = findCurrencyRateInGraph(currencyFrom, goalCurrencyCode);
            ratesCache.put(currencyFrom, rate);
        }
        return rate;
    }

    /**
     * Defines (or changes) the exchange rate between the origin and goal currencies in Graph.
     *
     * @param rate Rate between origin and goal currency
     * @return boolean flag of correctly added rates
     */
    private boolean setExchangeRateToGraph(@NonNull Rate rate) {
        // add the vertices (currencies) if they do not exist
        String origin = rate.getFrom();
        String goal = rate.getTo();
        currencyGraph.addVertex(origin);
        currencyGraph.addVertex(goal);

        // check whether the edge already exists
        // if so, remove it, in order to add it
        if (currencyGraph.containsEdge(origin, goal)) {
            currencyGraph.removeEdge(origin, goal);
            currencyGraph.removeEdge(goal, origin);
        }
        // add the edge
        boolean addDirectCurrency = currencyGraph.addEdge(origin, goal, rate);
        // and the direct inverse edge, with the inverse weight
        boolean addReverseCurrency = currencyGraph.addEdge(goal, origin, rate.invert());

        return addDirectCurrency && addReverseCurrency;
    }

    /**
     * Finds rate in graph from an origin currency to a goal currency.
     *
     * @param origin the string identifier of the currency to be exchanged
     * @param goal   the string identifier of the target currency
     * @return the amount of converted currency
     * @throws ExchangeRateUndefinedException
     */
    private BigDecimal findCurrencyRateInGraph(@NonNull String origin, @NonNull String goal) throws ExchangeRateUndefinedException {
        // find the shortest path between the two currencies
        List<Rate> l = DijkstraShortestPath.findPathBetween(currencyGraph, origin, goal);

        // when there is no path between the 2 nodes / vertices / currencies
        // DijkstraShortestPath returns null
        if (l == null)
            throw new ExchangeRateUndefinedException();

        // navigate the edges and iteratively compute the exchange rate
        BigDecimal rate = BigDecimal.ONE;
        for (Rate edge : l) {
            rate = rate.multiply(edge.getRate());
        }

        // compute and return the currency value
        return rate;
    }
}