import static io.restassured.RestAssured.*;
import static java.lang.System.out;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APITests {

    @Test
    void TestCardsDraw() {

        int deckCount = 1;
        int remainingShuffleTheCards = 0;
        int remainingShuffleTheCardsExpected = 52;
        int remainingDrawACardFirst = 0;
        int remainingDrawACardSecond = 0;
        int drawCount1 = 3;
        int drawCount2 = 3;
        String deckId = "";
        String drawPathFirst = "";
        String drawPathSecond = "";
        String cardsOne = "";
        String cardsTwo = "";
        String pilesCardsFirst = "";
        String pilesCardsSecond = "";
        String addToPilePathOne = "";
        String addToPilePathTwo = "";
        String pilesFirst = "Hrpa";
        String pilesSecond = "Hrpica";
        int remainingAddToPilesOne = 0;
        int remainingAddToPilesTwo = 0;
        String ListingCardsInPilesFirstPath = "";
        String ListingCardsInPilesSecondPath = "";

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        Response responseShuffleTheCards = get("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=" + deckCount);

        try {
            jsonObject = (JSONObject) parser.parse(responseShuffleTheCards.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        deckId = jsonObject.get("deck_id").toString();
        remainingShuffleTheCards = Integer.parseInt(jsonObject.get("remaining").toString());

        out.println(deckId);
        out.println(remainingShuffleTheCards);

        Assert.assertEquals(remainingShuffleTheCards, remainingShuffleTheCardsExpected);

        drawPathFirst = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=" + drawCount1;

        Response responseDrawACardOne = get(drawPathFirst);

        try {
            jsonObject = (JSONObject) parser.parse(responseDrawACardOne.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        remainingDrawACardFirst = Integer.parseInt(jsonObject.get("remaining").toString());

        out.println(remainingDrawACardFirst);

        JSONArray cardsJson1 = new JSONArray();
        JSONObject cardJson1 = new JSONObject();

        try {
            cardsJson1 = (JSONArray) parser.parse(jsonObject.get("cards").toString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        out.println(cardsJson1.get(0));

        for (int i = 0; i < drawCount1; i++) {
            try {
                cardJson1 = (JSONObject) parser.parse(cardsJson1.get(i).toString());
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            cardsOne = cardsOne + cardJson1.get("code").toString() + ",";
        }
        out.println(cardsOne);

        Assert.assertEquals(remainingDrawACardFirst, remainingShuffleTheCards - drawCount1);

        drawPathSecond = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=" + drawCount2;

        Response responseDrawACardTwice = get(drawPathSecond);

        try {
            jsonObject = (JSONObject) parser.parse(responseDrawACardTwice.getBody().asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        remainingDrawACardSecond = Integer.parseInt(jsonObject.get("remaining").toString());

        out.println(remainingDrawACardSecond);

        JSONArray cardsJson2 = new JSONArray();
        try {
            cardsJson2 = (JSONArray) parser.parse(jsonObject.get("cards").toString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        out.println(cardsJson2.get(0));

        JSONObject cardJson2 = new JSONObject();

        for (int i = 0; i < drawCount1; i++) {
            try {
                cardJson2 = (JSONObject) parser.parse(cardsJson2.get(i).toString());
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            cardsTwo = cardsTwo + cardJson2.get("code").toString() + ",";
        }
        out.println(cardsTwo);

        Assert.assertEquals(remainingDrawACardSecond, remainingShuffleTheCards - (drawCount2 + drawCount1));

        addToPilePathOne = "https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pilesFirst + "/add/?cards=" + cardsOne;
        addToPilePathTwo = "https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pilesSecond + "/add/?cards=" + cardsTwo;

        Response responseAddToPileOne = get( addToPilePathOne);

        try {
            jsonObject = (JSONObject) parser.parse(responseAddToPileOne.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        Response responseAddToPileTwo = get( addToPilePathTwo);

        try {
            jsonObject = (JSONObject) parser.parse(responseAddToPileTwo.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        JSONObject pilesFirstOne = getJSONObject(jsonObject, "piles");
        JSONObject discardOne = getJSONObject(pilesFirstOne, "Hrpa");

        remainingAddToPilesOne = Integer.parseInt(discardOne.get("remaining").toString());
        out.println(remainingAddToPilesOne);

        JSONObject pilesSecondTwo = getJSONObject(jsonObject, "piles");
        JSONObject discardTwo = getJSONObject(pilesFirstOne, "Hrpica");

        remainingAddToPilesTwo = Integer.parseInt(discardTwo.get("remaining").toString());
        out.println(remainingAddToPilesTwo);

        ListingCardsInPilesFirstPath = "https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pilesFirst + "/list/";
        ListingCardsInPilesSecondPath = "https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pilesSecond + "/list/";

        Response responseListingCardsInPilesFirst = get(ListingCardsInPilesFirstPath);

        try {
            jsonObject = (JSONObject) parser.parse(responseListingCardsInPilesFirst.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        JSONObject pilesFirstOneListing = getJSONObject(jsonObject, "piles");
        JSONObject discardOneListing = getJSONObject(pilesFirstOneListing, "Hrpa");

        JSONArray pilesJsonOneListing = new JSONArray();
        try {
            pilesJsonOneListing = (JSONArray) parser.parse(discardOneListing.get("cards").toString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        JSONObject pilesJsonOneObjectListing = new JSONObject();

        for (int i = 0; i < drawCount1; i++) {
            try {
                pilesJsonOneObjectListing= (JSONObject) parser.parse(pilesJsonOneListing.get(i).toString());
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            pilesCardsFirst = pilesCardsFirst + pilesJsonOneObjectListing.get("code").toString() + ",";
        }
        out.println(pilesCardsFirst);

        Assert.assertEquals(pilesCardsFirst, cardsOne);




        Response responseListingCardsInPilesSecond = get(ListingCardsInPilesSecondPath);
        try {
            jsonObject = (JSONObject) parser.parse(responseListingCardsInPilesSecond.asString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        JSONObject pilesSecondListing = getJSONObject(jsonObject, "piles");
        JSONObject discardSecondListing = getJSONObject(pilesSecondListing, "Hrpica");

        JSONArray pilesJsonSecondListing = new JSONArray();
        try {
            pilesJsonSecondListing = (JSONArray) parser.parse(discardSecondListing.get("cards").toString());
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        JSONObject pilesJsonSecondObjectListing = new JSONObject();

        for (int i = 0; i < drawCount2; i++) {
            try {
                pilesJsonSecondObjectListing= (JSONObject) parser.parse(pilesJsonSecondListing.get(i).toString());
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            pilesCardsSecond = pilesCardsSecond + pilesJsonSecondObjectListing.get("code").toString() + ",";
        }
        out.println(pilesCardsSecond);

        Assert.assertEquals(pilesCardsSecond, cardsTwo);

    }

    public JSONObject getJSONObject (JSONObject obj, String arg) {

        JSONObject res = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            res = (JSONObject) parser.parse(obj.get(arg).toString());
        }
        catch (Exception e)
        {
            out.println(e.getMessage());
        }
        return res;
    }


}
