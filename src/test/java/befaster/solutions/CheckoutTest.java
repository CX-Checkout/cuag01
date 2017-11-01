package befaster.solutions;

import org.junit.Test;


import java.util.Optional;

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
    public void checkoutAReturnValue() throws Exception{
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
        assertEquals(OFFER_A_COST, checkout);
    }

    @Test
    public void checkoutDReturnDValue() throws Exception{
        final int checkout = checkout(FOURTH_PROD);
        assertEquals(FOURTH_PROD_SINGLE_COST, checkout);
    }

    @Test
    public void getOfferFromAReturnsNotEmpty() throws Exception{
        final Optional<Offer> offer = Checkout.getOffer(FIRST_PROD);
        assertTrue(offer.isPresent());
    }

    @Test
    public void calculatePriceA1ReturnAPrice() throws Exception {
        final int price = calculatePrice(FIRST_PROD, 1);
        assertEquals(FIRST_PROD_SINGLE_COST, price);
    }

    @Test
    public void checkoutOfferAReturnOfferValue() throws Exception{
        final int checkout = checkout(3 + FIRST_PROD);
        assertEquals(OFFER_A_COST, checkout);
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
