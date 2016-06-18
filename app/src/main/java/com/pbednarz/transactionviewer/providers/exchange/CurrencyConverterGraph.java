package com.pbednarz.transactionviewer.providers.exchange;

import com.pbednarz.transactionviewer.models.Rate;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedMultigraph;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class implements the a currency converter.
 * Because not every exchange rate is directly defined, the basis data structure is an
 * unweighted, directed graph, with strings as vertices and edges holding the exchange rate.
 * SimpleDirectedGraph<String, BigDecimal>
 * Final exchange rates are computed using Dijkstra's shortest path algorithm.
 * <p>
 * a) In realistic cases, paths between currencies should be relatively small (max 2,3 edges),
 * so that no major performance issues should exist (application dependent).
 * If that is the case, a possibility straightforward improvement could be made by
 * using a cache structure, e.g of type TreeMap< String, TreeMap< String, Double> >, where all
 * possible exchange rates are precomputed and stored.
 * It would be updated every time the graph is changed.
 * <p>
 * b) The class is not yet thread-safe. As soon as a concrete application where that is an issue,
 * the issue will be addressed.
 * <p>
 * c) Vertices are String because of the initial definition of the problem. Ideally, however, one
 * could use java.util.Currency, which implements ISO-4217
 *
 * @author Miguel Vaz
 * @author pbednarz
 */

public class CurrencyConverterGraph {

    /*
     * directed, because the exchange rate is direction specific
     * unweighted, because the exchange rates are the edges themselves
     */
    private DirectedMultigraph<String, Rate> currencyGraph;

    /**
     * Simple constructor. Initializes nothing.
     */
    public CurrencyConverterGraph() {
        currencyGraph = new DirectedMultigraph<>(Rate.class);
    }

    /**
     * Defines (or changes) the exchange rate between the origin and goal currencies.
     * TODO: not thread-safe
     *
     * @param rate
     * @return
     */
    public boolean setExchangeRate(Rate rate) {
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
     * Converts a given amount of an origin currency to a goal currency.
     * TODO: not thread-safe
     *
     * @param origin the string identifier of the currency to be exchanged
     * @param goal   the string identifier of the target currency
     * @param amount the amount of the origin currency to be exchanged
     * @return the amount of converted currency
     * @throws ExchangeRateUndefinedException
     */
    public BigDecimal convertCurrency(String origin, String goal, BigDecimal amount) throws ExchangeRateUndefinedException {
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
        return amount.multiply(rate);
    }

    /**
     * Checks whether the currency is already represented.
     *
     * @param currency
     * @return
     */
    public boolean containsCurrency(String currency) {
        return currencyGraph.containsVertex(currency);
    }

    @Override
    public String toString() {
        return currencyGraph.toString();
    }
}