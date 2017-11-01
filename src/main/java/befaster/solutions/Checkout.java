package befaster.solutions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class Checkout {

    public static final int ERROR = -1;
    public static final String FIRST_PROD = "A";
    public static final String SECOND_PROD = "B";
    public static final String THIRD_PROD = "C";
    public static final String FOURTH_PROD = "D";


    public static class Offer {
        String product;
        Integer quantity;

        public Offer(String product, Integer quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Offer offer = (Offer) o;
            return Objects.equals(product, offer.product) &&
                    Objects.equals(quantity, offer.quantity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, quantity);
        }
    }

    public static class OfferMatch {
        Integer nMatches;
        Offer offer;

        public OfferMatch(Offer offer, Integer nMatches) {
            this.offer = offer;
            this.nMatches = nMatches;
        }
    }

    public static final Map<String, Integer> PROD_COSTS = new HashMap<>();
    public static final Map<Offer, Integer> OFFERS_COSTS = new HashMap<>();

    public static final int FIRST_PROD_SINGLE_COST = 50;
    public static final int SECOND_PROD_SINGLE_COST = 30;
    public static final int THIRD_PROD_SINGLE_COST = 20;
    public static final int FOURTH_PROD_SINGLE_COST = 15;

    public static final int OFFER_A_COST = 130;
    protected static final String DIGITS = "[\\d]";

    static {
        PROD_COSTS.put(FIRST_PROD, FIRST_PROD_SINGLE_COST);
        PROD_COSTS.put(SECOND_PROD, SECOND_PROD_SINGLE_COST);
        PROD_COSTS.put(THIRD_PROD, THIRD_PROD_SINGLE_COST);
        PROD_COSTS.put(FOURTH_PROD, FOURTH_PROD_SINGLE_COST);
        OFFERS_COSTS.put(new Offer(FIRST_PROD, 3), OFFER_A_COST);
        OFFERS_COSTS.put(new Offer(SECOND_PROD, 2), 45);
    }
    public static final Set<String> PRODUCTS = PROD_COSTS.keySet();

    public static Integer checkout(String skus) {
        int value;
        if(skus == null || skus.isEmpty()) return 0;
        Map<String, Long> skuMap = skusToMap(skus);
        if(!isValid(skuMap.keySet())) return ERROR;
        value = calculatePrice(skuMap);
        return value;
    }

    private static int calculatePrice(Map<String, Long> skuMap) {
        return skuMap.entrySet().stream()
                .map(entry -> calculatePrice(entry.getKey(), entry.getValue().intValue()))
                .mapToInt(i -> i)
                .sum();
    }

    static int calculatePrice(String prod, int value) {
        final Optional<OfferMatch> matchingOffer = getMatchingOffer(prod, value);
        if(!matchingOffer.isPresent()) return getSimplePrice(prod) * value;
        final OfferMatch offerMatch = matchingOffer.get();
        int remaining = (int) (value - offerMatch.nMatches * offerMatch.offer.quantity);
        return offerMatch.nMatches * OFFERS_COSTS.get(offerMatch.offer) + getSimplePrice(prod) * remaining;
    }

    private static Optional<OfferMatch> getMatchingOffer(String prod, int nProds) {
        final Optional<Offer> offer = getOffer(prod);
        return offer.flatMap(offerVal -> makeOfferMatch(offerVal, nProds));
    }

    private static Optional<OfferMatch> makeOfferMatch(Offer offer, int nProds) {
        return nProds >= offer.quantity? Optional.of(new OfferMatch(offer, (int) (nProds / offer.quantity))) : Optional.empty();
    }

    static Optional<Offer> getOffer(String prod){
        return OFFERS_COSTS.keySet()
                .stream()
                .filter(offer -> offer.product.equals(prod))
                .findFirst();
    }

    private static Integer getSimplePrice(String prod) {
        return PROD_COSTS.get(prod);
    }

    private static Map<String, Long> skusToMap(String skus) {
        if (skus == null) return new HashMap<>();
        final String[] split = skus.split(",");
        return Arrays.asList(split).stream()
                .map(val -> val.split(" ")).map(Arrays::asList)
                .flatMap(Collection::stream)
                .map(Checkout::transformNumbersToMultiples)
                .flatMap(Collection::stream)
                .peek(a-> test(a))
                .map(string -> Chars.asList(string.toCharArray()))
                .peek(a-> test(a))
                .flatMap(Collection::stream)
                .map(v -> v.toString())
                .collect(Collectors.groupingBy(Function.identity(), counting()));
    }

    private static void test(Object a) {
        System.out.println(a);
    }

    private static List<String> transformNumbersToMultiples(String string) {
        if(!Character.isDigit(string.charAt(0))) return Lists.newArrayList(string);
        List<String> multiples = new ArrayList<>();
        Integer occurences = Integer.valueOf(string.replaceAll("[^\\d]", ""));
        String value = string.replaceAll(DIGITS, "");
        for(int i = 0; i < occurences; i++) {
            multiples.add(value);
        }
        return multiples;
    }

    public static boolean isValid(Set<String> skus){
        return PRODUCTS.containsAll(skus);
    }
}
