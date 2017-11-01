package befaster.solutions;

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
    public static final String FIFTH_PROD = "E";
    public static final String SIXTH_PROD = "F";

    public static class Basket{
        final HashMap<String, Long> skus;
        int cost;

        public Basket(Map<String, Long> skus) {
            this.skus = new HashMap<>(skus);
            this.cost = 0;
        }

        boolean isEmpty(){
            return skus.isEmpty();
        }

        public int getCost() {
            return cost;
        }
    }

    public abstract static class Benefit implements Comparable<Benefit>{
        private final int priority;

        protected Benefit(int priority) {
            this.priority = priority;
        }

        protected abstract void applyOnBasket(Basket basket);

        @Override
        public int compareTo(Benefit that) {
            return (this.priority - that.priority);
        }

        protected abstract boolean matches(Basket basket);
    }

    public static class ReducedPrice extends Benefit {
        protected static final int PRIORITY = 2;
        final String product;
        Integer quantity;
        final int totalPrice;

        public static ReducedPrice of(String product, int quantity, int totalPrice) {
            return new ReducedPrice(product, quantity, totalPrice);
        }

        public ReducedPrice(String product, int quantity, int totalPrice) {
            super(PRIORITY);
            this.product = product;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        @Override
        protected void applyOnBasket(Basket basket) {
            if(matches(basket)) {
                basket.skus.computeIfPresent(product, (key, value) -> value - quantity);
                basket.cost += totalPrice;
            }
        }

        @Override
        protected boolean matches(Basket basket) {
            return basket.skus.getOrDefault(product, 0L) >= quantity;
        }
    }

    public static class FreeProduct extends Benefit {
        protected static final int PRIORITY = 1;
        final String product;

        public FreeProduct(String product) {
            super(PRIORITY);
            this.product = product;
        }

        public static FreeProduct of(String product) {
            return new FreeProduct(product);
        }

        @Override
        protected void applyOnBasket(Basket basket) {
            if(matches(basket)) {
                basket.skus.computeIfPresent(product, (key, value) -> value > 0 ? value - 1 : 0);
            }
        }

        @Override
        protected boolean matches(Basket basket) {
            return basket.skus.getOrDefault(product, 0L) > 0;
        }
    }

    public static class Offer implements Comparable<Offer>{
        private ProductMatch productMatch;
        private Benefit benefit;

        public static Offer of(ProductMatch productMatch, Benefit benefit) {
            return new Offer(productMatch, benefit);
        }

        public Offer(ProductMatch productMatch, Benefit benefit) {
            this.productMatch = productMatch;
            this.benefit = benefit;
        }

        public int nMatches(String product, int nProds) {
            return productMatch.nMatches(product, nProds);
        }

        void applyOnBasket(Basket basket) {
            if(benefit.matches(basket)) {
                benefit.applyOnBasket(basket);
            }
        }

        @Override
        public int compareTo(Offer that) {
            final int benefitPriority = this.benefit.compareTo(that.benefit);
            return benefitPriority != 0 ? benefitPriority : this.productMatch.compareTo(that.productMatch);
        }
    }


    public static class ProductMatch implements Comparable<ProductMatch>{
        String product;
        Integer quantity;

        public static ProductMatch of(String product, Integer quantity) {
            return new ProductMatch(product, quantity);
        }

        public ProductMatch(String product, Integer quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductMatch productMatch = (ProductMatch) o;
            return Objects.equals(product, productMatch.product) &&
                    Objects.equals(quantity, productMatch.quantity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, quantity);
        }

        public int nMatches(String product, int nProds) {
            return !this.product.equals(product) ? 0 : nProds / quantity;
        }

        @Override
        public int compareTo(ProductMatch that) {
            final int productCompare = this.product.compareTo(that.product);
            return productCompare != 0 ? productCompare : -1 * this.quantity.compareTo(that.quantity);
        }
    }

    public static class OfferMatch {
        Integer nMatches;
        ProductMatch productMatch;

        public OfferMatch(ProductMatch productMatch, Integer nMatches) {
            this.productMatch = productMatch;
            this.nMatches = nMatches;
        }
    }
    protected static final String DIGITS = "[\\d]";

    public static final Map<String, Integer> PROD_COSTS = new HashMap<>();

    public static final int FIRST_PROD_SINGLE_COST = 50;
    public static final int SECOND_PROD_SINGLE_COST = 30;
    public static final int THIRD_PROD_SINGLE_COST = 20;
    public static final int FOURTH_PROD_SINGLE_COST = 15;
    public static final int FIFTH_PROD_SINGLE_COST = 40;
    public static final int SIXTH_PROD_SINGLE_COST = 10;

    public static final int OFFER_A3_COST = 130;
    public static final int OFFER_A5_COST = 200;
    public static final int OFFER_B2_COST = 45;


    static {
        PROD_COSTS.put("A",50);
        PROD_COSTS.put("B",30);
        PROD_COSTS.put("C",20);
        PROD_COSTS.put("D",15);
        PROD_COSTS.put("E",40);
        PROD_COSTS.put("F",10);
        PROD_COSTS.put("G",20);
        PROD_COSTS.put("H",10);
        PROD_COSTS.put("I",35);
        PROD_COSTS.put("J",60);
        PROD_COSTS.put("K",80);
        PROD_COSTS.put("L",90);
        PROD_COSTS.put("M",15);
        PROD_COSTS.put("N",40);
        PROD_COSTS.put("O",10);
        PROD_COSTS.put("P",50);
        PROD_COSTS.put("Q",30);
        PROD_COSTS.put("R",50);
        PROD_COSTS.put("S",30);
        PROD_COSTS.put("T",20);
        PROD_COSTS.put("U",40);
        PROD_COSTS.put("V",50);
        PROD_COSTS.put("W",20);
        PROD_COSTS.put("X",90);
        PROD_COSTS.put("Y",10);
        PROD_COSTS.put("Z",50);
    }

    public static final Set<String> PRODUCTS = PROD_COSTS.keySet();
    public static final List<Offer> OFFER =
            Lists.newArrayList(
                    Offer.of(ProductMatch.of(FIRST_PROD, 3), ReducedPrice.of(FIRST_PROD, 3,OFFER_A3_COST)),
                    Offer.of(ProductMatch.of(FIRST_PROD, 5), ReducedPrice.of(FIRST_PROD, 5, OFFER_A5_COST)),
                    Offer.of(ProductMatch.of(SECOND_PROD, 2), ReducedPrice.of(SECOND_PROD, 2, OFFER_B2_COST)),
                    Offer.of(ProductMatch.of(FIFTH_PROD, 2), FreeProduct.of(SECOND_PROD)),
                    Offer.of(ProductMatch.of(SIXTH_PROD, 3), FreeProduct.of(SIXTH_PROD)),
                    Offer.of(ProductMatch.of("H", 5), ReducedPrice.of("H", 5, 45)),
                    Offer.of(ProductMatch.of("H", 10), ReducedPrice.of("H", 10, 80)),
                    Offer.of(ProductMatch.of("K", 2), ReducedPrice.of("K", 2, 150)),
                    Offer.of(ProductMatch.of("N", 3), FreeProduct.of("M")),
                    Offer.of(ProductMatch.of("P", 5), ReducedPrice.of("P", 5, 200)),
                    Offer.of(ProductMatch.of("Q", 3), ReducedPrice.of("Q", 3, 80)),
                    Offer.of(ProductMatch.of("R", 3), FreeProduct.of("Q")),
                    Offer.of(ProductMatch.of("U", 4), FreeProduct.of("U")),
                    Offer.of(ProductMatch.of("V", 2), ReducedPrice.of("V", 2, 90)),
                    Offer.of(ProductMatch.of("V", 3), ReducedPrice.of("V", 3, 130))

            );


    public static Integer checkout(String skus) {
        int value;
        if (skus == null || skus.isEmpty()) return 0;
        Map<String, Long> skuMap = skusToMap(skus);
        if (!isValid(skuMap.keySet())) return ERROR;
        final Basket basket = new Basket(skuMap);
        value = calculateBasketPrice(basket);
        return value;
    }

    private static int calculateBasketPrice(final Basket basket) {
        final List<Offer> offers = findMatchingOffers(new HashMap<>(basket.skus));
        offers.stream().sorted()
                .forEach(offer -> offer.applyOnBasket(basket));
        PROD_COSTS.forEach((product, cost) -> applyCostOnBasket(basket, product, cost));
        return basket.cost;
    }

    private static void applyCostOnBasket(Basket basket, String product, Integer cost) {
        final int nProds = basket.skus.getOrDefault(product, 0L).intValue();
        basket.cost = basket.cost + nProds * cost;
        basket.skus.put(product,0L);
    }

    static List<Offer> findMatchingOffers(Map<String, Long> skuMap){
        if(skuMap.isEmpty()) return Collections.emptyList();
        return skuMap.entrySet()
                .stream()
                .map(entry -> findMatchingOffers(entry.getKey(), entry.getValue().intValue()))
                .flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());
    }

    private static List<Offer> findMatchingOffers(String key, int value) {
        return OFFER.parallelStream()
                .map(offer -> getOffers(key, value, offer))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static List<Offer> getOffers(String key, int value, Offer offer) {
        final int nMatches = offer.nMatches(key, value);
        if(nMatches == 0) return Collections.emptyList();
        final ArrayList<Offer> offers = Lists.newArrayListWithExpectedSize(nMatches);
        for (int i = 0; i < nMatches; i++){
            offers.add(offer);
        }
        return offers;
    }


    private static Optional<OfferMatch> makeOfferMatch(ProductMatch productMatch, int nProds) {
        return nProds >= productMatch.quantity ? Optional.of(new OfferMatch(productMatch, (int) (nProds / productMatch.quantity))) : Optional.empty();
    }

    private static Map<String, Long> skusToMap(String skus) {
        if (skus == null) return new HashMap<>();
        final String[] split = skus.split(",");
        return Arrays.asList(split).stream()
                .map(val -> val.split(" ")).map(Arrays::asList)
                .flatMap(Collection::stream)
                .map(Checkout::transformNumbersToMultiples)
                .flatMap(Collection::stream)
                .peek(Checkout::test)
                .map(string -> Chars.asList(string.toCharArray()))
                .peek(Checkout::test)
                .flatMap(Collection::stream)
                .map(Object::toString)
                .collect(Collectors.groupingBy(Function.identity(), counting()));
    }

    private static void test(Object a) {
        System.out.println(a);
    }

    private static List<String> transformNumbersToMultiples(String string) {
        if (!Character.isDigit(string.charAt(0))) return Lists.newArrayList(string);
        List<String> multiples = new ArrayList<>();
        Integer occurences = Integer.valueOf(string.replaceAll("[^\\d]", ""));
        String value = string.replaceAll(DIGITS, "");
        for (int i = 0; i < occurences; i++) {
            multiples.add(value);
        }
        return multiples;
    }

    public static boolean isValid(Set<String> skus) {
        return PRODUCTS.containsAll(skus);
    }


}
