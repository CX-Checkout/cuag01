package befaster.solutions;

import org.junit.Test;


import static befaster.solutions.Checkout.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CheckoutTest {

    @Test
    public void checkoutBadDataReturnError() throws Exception{
        final int checkout = checkout("*");
        assertEquals(ERROR, checkout);
    }

    @Test
    public void checkoutGoodDataReturnNoError() throws Exception{
        final int checkout = checkout(FIRST_PROD);
        assertNotEquals(ERROR, checkout);
    }

    @Test
    public void checkoutAReturnAValue() throws Exception{
        final int checkout = checkout(FIRST_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutASpaceBReturnAPlusBValue() throws Exception{
        final int checkout = checkout(FIRST_PROD + " " + SECOND_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST + SECOND_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutACommaBReturnAPlusBValue() throws Exception{
        final int checkout = checkout(FIRST_PROD + "," + SECOND_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST + SECOND_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkout2AReturnAPlusAValue() throws Exception{
        final int checkout = checkout(2 + FIRST_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST + FIRST_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutAAAReturnOfferAValue() throws Exception{
        final int checkout = checkout(FIRST_PROD+FIRST_PROD+FIRST_PROD);
        assertEquals(OFFER_A3_COST, checkout);
    }

    @Test
    public void checkoutDReturnDValue() throws Exception{
        final int checkout = checkout(FOURTH_PROD);
        assertEquals(FOURTH_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void calculatePriceA1ReturnAPrice() throws Exception {
        final int price = calculateBasketPrice(FIRST_PROD, 1);
        assertEquals(FIRST_PROD_SINGLE_COST, price);
    }

    @Test
    public void checkoutTwoOffersIGetTheBestOne() throws Exception{
        final int checkout = checkout(5 + FIRST_PROD);
        assertEquals(OFFER_A5_COST, checkout);
    }

    @Test
    public void checkoutTwoFreeOffersIBoth() throws Exception{
        final int checkout = checkout(4 + FIFTH_PROD + " " + 2 + SECOND_PROD);
        assertEquals(4 * FIFTH_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutTwoFreeOffersPlusOneReturnOffersPricePlusOne() throws Exception{
        final int checkout = checkout(4 + FIFTH_PROD + " " + 3 + SECOND_PROD);
        assertEquals(4 * FIFTH_PROD_SINGLE_COST + SECOND_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutOneFreeOffersPlusOneReturnOffersPricePlusOne() throws Exception{
        final int checkout = checkout(2 + FIFTH_PROD + " " + 2 + SECOND_PROD);
        assertEquals(2 * FIFTH_PROD_SINGLE_COST + SECOND_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutOneFreeOffersPlusOneReducedOffersPricePlusOne() throws Exception{
        final int checkout = checkout(2 + FIFTH_PROD + " " + 3 + SECOND_PROD);
        assertEquals(2 * FIFTH_PROD_SINGLE_COST + OFFER_B2_COST, checkout);
    }

    @Test
    public void checkoutOfferAReturnOfferValue() throws Exception{
        final int checkout = checkout(3 + FIRST_PROD);
        assertEquals(OFFER_A3_COST, checkout);
    }

    @Test
    public void checkoutACommaAReturnAPlusAValue() throws Exception{
        final int checkout = checkout(FIRST_PROD + "," + FIRST_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST + FIRST_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void checkoutACommaBSpaceCReturnAPlusBPlusCValue() throws Exception{
        final int checkout = checkout(FIRST_PROD + "," + SECOND_PROD + " " + THIRD_PROD);
        assertEquals(FIRST_PROD_SINGLE_COST + SECOND_PROD_SINGLE_COST + THIRD_PROD_SINGLE_COST, checkout);
    }


}
